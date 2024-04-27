package io.github.rainvaporeon.customenchantments.status;

import com.destroystokyo.paper.event.server.ServerTickStartEvent;
import io.github.rainvaporeon.customenchantments.CustomEnchantments;
import io.github.rainvaporeon.customenchantments.util.particles.Particles;
import io.github.rainvaporeon.customenchantments.util.server.Server;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

public class Poison implements Listener {
    private final Map<LivingEntity, PoisonInfo> poisonMap = new HashMap<>();

    private static final Poison INSTANCE = new Poison();

    private Poison() {}

    public static Poison getInstance() {
        return INSTANCE;
    }

    public static void applyPoison(LivingEntity entity, int seconds, int amplifier) {
        AttributeInstance inst = entity.getAttribute(Attribute.GENERIC_MAX_HEALTH);
        AttributeInstance defaultInst = entity.getType().getDefaultAttributes().getAttribute(Attribute.GENERIC_MAX_HEALTH);
        double maxHealth = inst == null ? (defaultInst == null ? 20.0 : defaultInst.getValue()) : inst.getValue();
        double damage = maxHealth * (amplifier / 100.0) / seconds;
        INSTANCE.poisonMap.compute(entity, (e, i) -> {
            if (i == null) return new PoisonInfo(seconds, damage);
            i.expirationTime = 20 * seconds + 20; return i;
        });
    }

    @EventHandler
    public void onTick(ServerTickStartEvent event) {
        poisonMap.entrySet().removeIf(entry -> entry.getKey().isDead() || --entry.getValue().expirationTime <= 0);
        poisonMap.forEach((entity, info) -> {
            if (info.expirationTime % 20 == 0) {
                Server.runTaskLater(() -> {
                    Particles.playPoisonParticle(entity);
                    double damage = info.damage;
                    if (entity.getHealth() <= damage) {
                        Server.damageInstantly(entity, entity.getHealth() - 1);
                    } else {
                        Server.damageInstantly(entity, damage);
                    }
                }, 1);
            }
        });
    }

    private static class PoisonInfo {
        private int expirationTime;
        private final double damage;

        public PoisonInfo(int expirationTime, double damage) {
            this.expirationTime = 20 * expirationTime + 20;
            this.damage = damage;
        }
    }
}
