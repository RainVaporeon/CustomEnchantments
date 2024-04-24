package io.github.rainvaporeon.customenchantments.status;

import com.destroystokyo.paper.event.server.ServerTickStartEvent;
import io.github.rainvaporeon.customenchantments.CustomEnchantments;
import io.github.rainvaporeon.customenchantments.util.particles.Particles;
import io.github.rainvaporeon.customenchantments.util.particles.Sounds;
import io.github.rainvaporeon.customenchantments.util.server.Server;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

public final class Bleeding implements Listener {
    private final Map<LivingEntity, BleedingInfo> bleedingMap = new HashMap<>();

    private static final Bleeding INSTANCE = new Bleeding();

    private Bleeding() {}

    public static Bleeding getInstance() {
        return INSTANCE;
    }

    public static void applyBleeding(LivingEntity entity, int seconds, int amplifier) {
        CustomEnchantments.PLUGIN.getLogger().log(Level.INFO, "Applying bleeding");
        INSTANCE.bleedingMap.compute(entity, (e, i) -> {
            if (i == null) return new BleedingInfo(seconds, Math.sqrt(amplifier));
            i.expirationTime = 20 * seconds + 20; return i;
        });
        CustomEnchantments.PLUGIN.getLogger().log(Level.INFO, "Bleeding applied");
    }

    @EventHandler
    public void onTick(ServerTickStartEvent event) {
        bleedingMap.entrySet().removeIf(entry -> entry.getKey().isDead() || --entry.getValue().expirationTime <= 0);
        bleedingMap.forEach((entity, info) -> {
            if (info.expirationTime % 20 == 0) {
                Server.runTaskLater(() -> {
                    CustomEnchantments.PLUGIN.getLogger().log(Level.INFO, "Ticking bleeding");
                    Particles.playBleedingParticle(entity);
                    Sounds.playBlockBreakSound(entity);
                    entity.damage(info.damage);
                }, 1);
            }
        });
    }

    private static class BleedingInfo {
        private int expirationTime;
        private final double damage;

        public BleedingInfo(int expirationTime, double damage) {
            this.expirationTime = 20 * expirationTime + 20;
            this.damage = damage;
        }
    }
}
