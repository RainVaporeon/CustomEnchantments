package io.github.rainvaporeon.customenchantments.enchant.buff.protection;

import io.github.rainvaporeon.customenchantments.enchant.Infusion;
import io.github.rainvaporeon.customenchantments.util.infusions.InfusionUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.jetbrains.annotations.Nullable;

public class KineticAbsorbInfusion extends Infusion {
    @Override
    public String getIdentifier() {
        return "kinetic_absorb";
    }

    @Override
    public String getName() {
        return "Kinetic Absorb";
    }

    @Nullable
    @Override
    public Listener getListener() {
        return new DamageListener();
    }

    @Override
    public int getMaxLevel() {
        return 5;
    }

    @Override
    public String getDescription() {
        return "Reduces incoming kinetic damage by (0.9^level).";
    }

    @Nullable
    @Override
    public String getExtendedDescription(int level) {
        return String.format("Kinetic damage reduced by %.1f%%.", 100 - Math.pow(0.9, level) * 100);
    }

    class DamageListener implements Listener {
        @EventHandler
        public void onDamage(EntityDamageEvent event) {
            if (!(event.getEntity() instanceof Player)) return;
            if (event.getCause() != EntityDamageEvent.DamageCause.FLY_INTO_WALL) return;
            int level = InfusionUtils.accumulateInfusionLevelOf((Player) event.getEntity(), KineticAbsorbInfusion.this);
            if (level == 0) return;
            event.setDamage(event.getDamage() * Math.pow(0.9, level));
        }
    }
}
