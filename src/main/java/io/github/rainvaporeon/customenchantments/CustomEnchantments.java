package io.github.rainvaporeon.customenchantments;

import io.github.rainvaporeon.customenchantments.commands.*;
import io.github.rainvaporeon.customenchantments.enchant.buff.combat.*;
import io.github.rainvaporeon.customenchantments.enchant.buff.misc.*;
import io.github.rainvaporeon.customenchantments.enchant.buff.protection.*;
import io.github.rainvaporeon.customenchantments.enchant.debuff.CumbersomeInfusion;
import io.github.rainvaporeon.customenchantments.enchant.debuff.RepairFragilityInfusion;
import io.github.rainvaporeon.customenchantments.enchant.debuff.misc.BrittleInfusion;
import io.github.rainvaporeon.customenchantments.enchant.debuff.misc.ShatteringCurseInfusion;
import io.github.rainvaporeon.customenchantments.enchant.debuff.protection.*;
import io.github.rainvaporeon.customenchantments.enchant.set.MorphSetInfusion;
import io.github.rainvaporeon.customenchantments.status.Bleeding;
import io.github.rainvaporeon.customenchantments.status.Poison;
import io.github.rainvaporeon.customenchantments.util.event.InfusionAnvilListener;
import io.github.rainvaporeon.customenchantments.util.infusions.InfusionManager;
import io.github.rainvaporeon.customenchantments.util.io.LocalConfig;
import io.github.rainvaporeon.customenchantments.util.server.Server;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
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
        this.getServer().getCommandMap().register(FALLBACK_PREFIX, GiveStoredInfusionCommand.getInstance());
        this.getServer().getCommandMap().register(FALLBACK_PREFIX, RemoveStoredInfusionCommand.getInstance());
        this.getServer().getCommandMap().register(FALLBACK_PREFIX, ReloadCommand.getInstance());

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

        /* Unrelated buff */
        InfusionManager.registerInfusions(
                new KillStreakInfusion(),
                new BrutalizeInfusion(),
                new EnlightenedInfusion(),
                new KineticAbsorbInfusion(),
                new StressTransferInfusion(),
                new SplinterInfusion(),
                new SanguineMendInfusion(),
                new AcrobaticsInfusion()
        );

        /* Special buff infusion */
        InfusionManager.registerInfusions(
                new BleedingInfusion(),
                new PoisonInfusion(),
                new BloodRushInfusion(),
                new BloodlustInfusion(),
                new GracefulLandingInfusion(),
                new TitanicInfusion(),
                new LifeStealInfusion(),
                new SoulTetherInfusion(),
                new TranquilityAuraInfusion()
        );

        /* Other debuff infusion */
        InfusionManager.registerInfusions(
                new CumbersomeInfusion(),
                new RepairFragilityInfusion(),
                new ShatteringCurseInfusion(),
                new BrittleInfusion()
        );

        /* Set infusions */
        InfusionManager.registerInfusions(
                new MorphSetInfusion()
        );

        Bukkit.getPluginManager().registerEvents(Bleeding.getInstance(), this);
        Bukkit.getPluginManager().registerEvents(Poison.getInstance(), this);
        Bukkit.getPluginManager().registerEvents(InfusionAnvilListener.getInstance(), this);

        LocalConfig.init(this);
        InfusionManager.updateCache();

        // 2024 aug 7th: when did i add this??? /rain
        Server.createRunnable(() -> {
            Player loneF = Bukkit.getServer().getPlayerExact("LoneWolfFL");
            Player loneM = Bukkit.getServer().getPlayerExact("MaoMaster69");

            if (loneF != null) {
                loneF.sendMessage("<Rain> can i comm u");
            }
            if (loneM != null) {
                loneM.sendMessage("<Rain> hi can i comm");
            }
        }).runTaskTimer(this, 30000, 600000);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

}
