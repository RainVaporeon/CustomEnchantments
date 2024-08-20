package io.github.rainvaporeon.customenchantments.enchant.buff.combat;

import io.github.rainvaporeon.customenchantments.enchant.Infusion;
import io.github.rainvaporeon.customenchantments.util.infusions.InfusionManager;
import io.github.rainvaporeon.customenchantments.util.infusions.InfusionUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;
import java.util.Set;

public class BrutalizeInfusion extends Infusion {
    public static final double AMPLIFIER = 1.05;

    @Override
    public String getIdentifier() {
        return "brutalize";
    }

    @Override
    public String getName() {
        return "Brutalize";
    }

    @Override
    public int getMaxLevel() {
        return 3;
    }

    @Override
    public String getDescription() {
        return "Increases critical hit damage by (1.05)^level.";
    }

    @Nullable
    @Override
    public String getExtendedDescription(int level) {
        return String.format("Critical damage increased by %.1f%%", 100 * (Math.pow(AMPLIFIER, level) - 1));
    }

    @Override
    public @NotNull Listener getListener() {
        return new CriticalHitListener();
    }

    @Override
    public Set<EquipmentSlot> applicableSlots() {
        return EnumSet.of(EquipmentSlot.HAND, EquipmentSlot.OFF_HAND);
    }

    class CriticalHitListener implements Listener {

        @EventHandler
        public void onEntityDamaged(EntityDamageByEntityEvent event) {
            if (!(event.getDamager() instanceof Player)) return;
            Player damager = (Player) event.getDamager();
            int totalLevel = InfusionUtils.accumulateInfusionLevelOf(damager, BrutalizeInfusion.this);
            int cumbersome = InfusionUtils.accumulateInfusionLevelOf(damager, InfusionManager.getInfusionById("cumbersome"));
            if (cumbersome > 0) return; // Do not apply on cumbersome
            if (event.isCritical()) {
                double multiplier = Math.pow(AMPLIFIER, totalLevel);
                event.setDamage(event.getDamage() * multiplier);
            }
        }
    }
}
