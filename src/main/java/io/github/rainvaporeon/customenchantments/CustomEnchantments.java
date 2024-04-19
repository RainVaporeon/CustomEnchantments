package io.github.rainvaporeon.customenchantments;

import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public final class CustomEnchantments extends JavaPlugin {

    public static Plugin PLUGIN;

    @Override
    public void onEnable() {
        // Plugin startup logic
        PLUGIN = this;
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public static void registerCommands() {

    }
}
