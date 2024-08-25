package io.github.rainvaporeon.customenchantments.util.server;

import io.github.rainvaporeon.customenchantments.CustomEnchantments;
import io.github.rainvaporeon.customenchantments.util.io.LocalConfig;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * A collection of convenience methods for interacting with the server
 */
public class Server {
    private static final AtomicInteger epoch = new AtomicInteger();

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

    /**
     * Runs the given task after some time
     * @param runnable the runnable
     * @param delayTicks the delay in ticks
     * @return the task
     */
    public static BukkitTask runTaskLater(Runnable runnable, int delayTicks) {
        return Server.createRunnable(runnable).runTaskLater(CustomEnchantments.PLUGIN, delayTicks);
    }

    /**
     * Runs the given task repeatedly
     * @param runnable the runnable
     * @param initialDelay the initial delay in ticks
     * @param thenDelay then loop every x ticks
     * @return the task
     */
    public static BukkitTask runTaskRepeated(Runnable runnable, int initialDelay, int thenDelay) {
        return Server.createRunnable(runnable).runTaskTimer(CustomEnchantments.PLUGIN, initialDelay, thenDelay);
    }

    public static BukkitRunnable createRunnable(Runnable runnable) {
        return new BukkitRunnable() {
            @Override
            public void run() {
                runnable.run();
            }
        };
    }

    public static List<Player> collectPlayers(Predicate<Player> p) {
        return CustomEnchantments.PLUGIN.getServer()
                .getOnlinePlayers()
                .stream().filter(p)
                .collect(Collectors.toList());
    }

    public static int getTimer() {
        return epoch.get();
    }

    private static boolean timerEnabled = false;
    public static void timerLoop() {
        if (timerEnabled) throw new IllegalStateException("internal method timerLoop called twice");
        timerEnabled = true;

        Server.runTaskRepeated(() -> {
            int current = epoch.get();
            epoch.set((current + 1) % 20);
        }, 1, 1);
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
