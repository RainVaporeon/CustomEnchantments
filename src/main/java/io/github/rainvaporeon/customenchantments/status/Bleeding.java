package io.github.rainvaporeon.customenchantments.status;

import com.destroystokyo.paper.event.server.ServerTickStartEvent;
import io.github.rainvaporeon.customenchantments.util.particles.Particles;
import io.github.rainvaporeon.customenchantments.util.particles.Sounds;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.HashMap;
import java.util.Map;

public final class Bleeding implements Listener {
    private final Map<LivingEntity, BleedingInfo> bleedingMap = new HashMap<>();

    private static final Bleeding INSTANCE = new Bleeding();

    private Bleeding() {}

    public static Bleeding getInstance() {
        return INSTANCE;
    }

    public static void applyBleeding(LivingEntity entity, int seconds, int amplifier) {
        INSTANCE.bleedingMap.put(entity, new BleedingInfo(seconds, Math.sqrt(amplifier)));
    }

    @EventHandler
    public void onTick(ServerTickStartEvent event) {
        bleedingMap.forEach((entity, info) -> {
            if (--info.expirationTime <= 0 || entity.isDead()) {
                bleedingMap.remove(entity);
                return;
            }
            if (info.expirationTime % 20 == 0) {
                Particles.playBleedingParticle(entity);
                Sounds.playBlockBreakSound(entity);
                entity.damage(info.damage);
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
