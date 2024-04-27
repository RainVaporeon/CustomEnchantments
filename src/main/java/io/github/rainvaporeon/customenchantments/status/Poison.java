package io.github.rainvaporeon.customenchantments.status;

import com.destroystokyo.paper.event.server.ServerTickStartEvent;
import io.github.rainvaporeon.customenchantments.util.particles.Particles;
import io.github.rainvaporeon.customenchantments.util.server.Server;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.HashMap;
import java.util.Map;

public class Poison implements Listener {
    private final Map<LivingEntity, PoisonInfo> bleedingMap = new HashMap<>();

    private static final Poison INSTANCE = new Poison();

    private Poison() {}

    public static Poison getInstance() {
        return INSTANCE;
    }

    public static void applyPoison(LivingEntity entity, int seconds, int amplifier) {
        if (entity.getAttribute(Attribute.GENERIC_MAX_HEALTH) == null) return;
        double damage = entity.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue() * (0.5 * amplifier);
        INSTANCE.bleedingMap.compute(entity, (e, i) -> {
            if (i == null) return new PoisonInfo(seconds, damage);
            i.expirationTime = 20 * seconds + 20; return i;
        });
    }

    @EventHandler
    public void onTick(ServerTickStartEvent event) {
        bleedingMap.entrySet().removeIf(entry -> entry.getKey().isDead() || --entry.getValue().expirationTime <= 0);
        bleedingMap.forEach((entity, info) -> {
            if (info.expirationTime % 20 == 0) {
                Server.runTaskLater(() -> {
                    Particles.playPoisonParticle(entity);
                    double damage = info.damage;
                    if (entity.getHealth() < damage) {
                        entity.setHealth(1);
                    } else {
                        entity.damage(damage);
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
