package io.github.rainvaporeon.customenchantments.util.server;

import io.github.rainvaporeon.customenchantments.CustomEnchantments;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A collection of convenience methods for interacting with the server
 */
public class Server {
    public static Logger getLogger() {
        return CustomEnchantments.PLUGIN.getLogger();
    }

    public static void log(String message) {
        CustomEnchantments.PLUGIN.getLogger().log(Level.INFO, message);
    }

    public static void warn(String message) {
        CustomEnchantments.PLUGIN.getLogger().log(Level.WARNING, message);
    }

    public static void warn(String message, Throwable t) {
        getLogger().log(Level.WARNING, message, t);
    }

    public static void runTask(Runnable runnable) {
        Server.createRunnable(runnable).runTask(CustomEnchantments.PLUGIN);
    }

    public static void runTaskLater(Runnable runnable, int delayTicks) {
        Server.createRunnable(runnable).runTaskLater(CustomEnchantments.PLUGIN, delayTicks);
    }

    public static BukkitRunnable createRunnable(Runnable runnable) {
        return new BukkitRunnable() {
            @Override
            public void run() {
                runnable.run();
            }
        };
    }

    public static void damageInstantly(LivingEntity entity, double damage) {
        int invulTicks = entity.getNoDamageTicks();
        entity.setNoDamageTicks(0);
        entity.damage(damage);
        entity.setNoDamageTicks(invulTicks);
    }

    public static void damageInstantly(LivingEntity entity, Entity cause, double damage) {
        int invulTicks = entity.getNoDamageTicks();
        entity.setNoDamageTicks(0);
        entity.damage(damage, cause);
        entity.setNoDamageTicks(invulTicks);
    }
}
