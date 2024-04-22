package io.github.rainvaporeon.customenchantments.enchant.buff.protection;

import io.github.rainvaporeon.customenchantments.enchant.Infusion;
import io.github.rainvaporeon.customenchantments.util.infusions.InfusionUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.jetbrains.annotations.Nullable;

public class BlastResistanceInfusion extends Infusion {
    @Override
    public String getIdentifier() {
        return "blast_resistance";
    }

    @Override
    public String getName() {
        return "Blast Resistance";
    }

    @Override
    public int getMaxLevel() {
        return 5;
    }

    @Override
    public String getDescription() {
        return "Reduces incoming blast damage by (0.94^level)";
    }

    @Nullable
    @Override
    public String getExtendedDescription(int level) {
        return String.format("Blast damage reduced by %.1f%%.", 100 - Math.pow(0.94, level) * 100);
    }

    @Nullable
    @Override
    public Listener getListener() {
        return new DamageListener();
    }

    class DamageListener implements Listener {
        @EventHandler
        public void onDamage(EntityDamageEvent event) {
            if (!(event.getEntity() instanceof Player)) return;
            Player player = (Player) event.getEntity();
            EntityDamageEvent.DamageCause cause = event.getCause();
            if (cause != EntityDamageEvent.DamageCause.ENTITY_EXPLOSION
            && cause != EntityDamageEvent.DamageCause.BLOCK_EXPLOSION) return;
            int level = InfusionUtils.accumulateInfusionLevelOf(player, BlastResistanceInfusion.this);
            event.setDamage(Math.max(0, event.getDamage() * Math.pow(0.94, level)));
        }
    }
}
