package io.github.rainvaporeon.customenchantments;

import io.github.rainvaporeon.customenchantments.commands.CurrentInfusionCommand;
import io.github.rainvaporeon.customenchantments.commands.GiveInfusionCommand;
import io.github.rainvaporeon.customenchantments.commands.ListInfusionCommand;
import io.github.rainvaporeon.customenchantments.enchant.DamageReductionInfusion;
import io.github.rainvaporeon.customenchantments.util.infusions.InfusionManager;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public final class CustomEnchantments extends JavaPlugin {
    public static final String FALLBACK_PREFIX = "customenchantments";

    public static Plugin PLUGIN;

    @Override
    public void onEnable() {
        // Plugin startup logic
        if (PLUGIN != null) return; // Prevent double initialization
        PLUGIN = this;
        this.getServer().getCommandMap().register(FALLBACK_PREFIX, GiveInfusionCommand.getInstance());
        this.getServer().getCommandMap().register(FALLBACK_PREFIX, ListInfusionCommand.getInstance());
        this.getServer().getCommandMap().register(FALLBACK_PREFIX, new CurrentInfusionCommand());
        InfusionManager.registerInfusion(new DamageReductionInfusion());
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

}
