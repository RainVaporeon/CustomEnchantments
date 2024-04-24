package io.github.rainvaporeon.customenchantments.util.server;

import io.github.rainvaporeon.customenchantments.CustomEnchantments;
import org.bukkit.scheduler.BukkitRunnable;

public class Server {

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
}
