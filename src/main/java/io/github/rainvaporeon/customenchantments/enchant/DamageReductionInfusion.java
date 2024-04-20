package io.github.rainvaporeon.customenchantments.enchant;

import io.github.rainvaporeon.customenchantments.util.infusions.InfusionUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.jetbrains.annotations.Nullable;

public class DamageReductionInfusion extends Infusion {

    @Override
    public String getIdentifier() {
        return "damage_reduction";
    }

    @Override
    public String getName() {
        return "Resistance";
    }

    @Override
    public int getMaxLevel() {
        return 5;
    }

    @Nullable
    @Override
    public Listener getListener() {
        return new DamageReductionListener();
    }

    @Override
    public String getDescription() {
        return "Reduces incoming damage by (0.96^level)";
    }

    @Nullable
    @Override
    public String getExtendedDescription(int level) {
        return String.format("Damage reduced by %.1f%%.", Math.pow(0.96, level) * 100);
    }

    class DamageReductionListener implements Listener {
        @EventHandler
        public void onPlayerHurtEvent(EntityDamageEvent event) {
            if (!(event.getEntity() instanceof Player)) return;
            Player player = (Player) event.getEntity();
            int total = InfusionUtils.accumulateInfusionLevelOf(player, DamageReductionInfusion.this);
            event.setDamage(Math.max(0, event.getDamage() * Math.pow(0.96, total)));
        }
    }
}
