package io.github.rainvaporeon.customenchantments;

import io.github.rainvaporeon.customenchantments.commands.*;
import io.github.rainvaporeon.customenchantments.enchant.buff.combat.BleedingInfusion;
import io.github.rainvaporeon.customenchantments.enchant.buff.combat.BloodRushInfusion;
import io.github.rainvaporeon.customenchantments.enchant.buff.combat.BrutalizeInfusion;
import io.github.rainvaporeon.customenchantments.enchant.buff.protection.*;
import io.github.rainvaporeon.customenchantments.enchant.debuff.CumbersomeInfusion;
import io.github.rainvaporeon.customenchantments.enchant.debuff.RepairFragilityInfusion;
import io.github.rainvaporeon.customenchantments.enchant.debuff.protection.*;
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
        this.getServer().getCommandMap().register(FALLBACK_PREFIX, RemoveInfusionCommand.getInstance());
        this.getServer().getCommandMap().register(FALLBACK_PREFIX, UpdateItemCommand.getInstance());

        /* Buff-related defensive infusion */
        InfusionManager.registerInfusions(
                new DamageReductionInfusion(),
                new MeleeResistanceInfusion(),
                new ProjectileResistanceInfusion(),
                new BlastResistanceInfusion(),
                new FireResistanceInfusion(),
                new MagicResistanceInfusion()
        );

        /* Debuff-related defensive infusion */
        InfusionManager.registerInfusions(
                new MeleeFragilityInfusion(),
                new ProjectileFragilityInfusion(),
                new BlastFragilityInfusion(),
                new FireFragilityInfusion(),
                new MagicFragilityInfusion()
        );

        /* Buff-related combat infusion */
        InfusionManager.registerInfusions(
                new BrutalizeInfusion(),
                new BleedingInfusion(),
                new BloodRushInfusion()
        );

        /* Debuff-related combat infusion */
        InfusionManager.registerInfusions(
                new CumbersomeInfusion()
        );

        /* Unrelated debuff */
        InfusionManager.registerInfusions(
                new RepairFragilityInfusion()
        );
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

}
