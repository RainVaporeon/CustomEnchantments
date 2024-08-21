package io.github.rainvaporeon.customenchantments.util.server;

import io.github.rainvaporeon.customenchantments.CustomEnchantments;
import io.github.rainvaporeon.customenchantments.util.io.LocalConfig;
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

    public static void log(Level severity, String message) {
        getLogger().log(severity, message);
    }

    public static void log(Level severity, String message, Throwable thrown) {
        getLogger().log(severity, message, thrown);
    }

    public static void debug(String message) {
        debug(Level.INFO, message);
    }

    public static void debug(Level level, String message) {
        if (LocalConfig.instance().readBoolean(LocalConfig.DEBUG_LOGGING, false)) {
            getLogger().log(level, "Debug >> " + message);
        }
    }

    public static void info(String message) {
        Server.log(Level.INFO, message);
    }

    public static void warn(String message) {
        Server.log(Level.WARNING, message);
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
        Server.debug(Level.INFO, "damageInstantly called");
        int invulTicks = entity.getNoDamageTicks();
        entity.setNoDamageTicks(0);
        entity.damage(damage);
        entity.setNoDamageTicks(invulTicks);
    }

    public static void damageInstantly(LivingEntity entity, Entity cause, double damage) {
        Server.debug(Level.INFO, "damageInstantly(e, c, d) called");
        int invulTicks = entity.getNoDamageTicks();
        entity.setNoDamageTicks(0);
        entity.damage(damage, cause);
        entity.setNoDamageTicks(invulTicks);
    }
}
