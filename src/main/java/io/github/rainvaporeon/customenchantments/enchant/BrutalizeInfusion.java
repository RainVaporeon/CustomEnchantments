package io.github.rainvaporeon.customenchantments.enchant;

import io.github.rainvaporeon.customenchantments.util.infusions.InfusionUtils;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;
import java.util.Set;

public class BrutalizeInfusion extends Infusion {
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
        return "Increases critical hit damage by 10% per level.";
    }

    @Nullable
    @Override
    public String getExtendedDescription(int level) {
        return "+" + 10 * level + "% Increased Critical Damage";
    }

    @Nullable
    @Override
    public Listener getListener() {
        return new CriticalHitListener();
    }

    @Override
    public Set<EquipmentSlot> applicableSlots() {
        return EnumSet.of(EquipmentSlot.HAND, EquipmentSlot.OFF_HAND);
    }

    @Override
    public Set<EnchantmentTarget> infusionTarget() {
        return EnumSet.of(EnchantmentTarget.WEAPON, EnchantmentTarget.TOOL, EnchantmentTarget.TRIDENT);
    }

    class CriticalHitListener implements Listener {

        @EventHandler
        public void onEntityDamaged(EntityDamageByEntityEvent event) {
            if (!(event.getDamager() instanceof Player)) return;
            Player damager = (Player) event.getDamager();
            int totalLevel = InfusionUtils.accumulateInfusionLevelOf(damager, BrutalizeInfusion.this);
            if (event.isCritical()) {
                double multiplier = 1 + 0.1 * totalLevel;
                event.setDamage(event.getDamage() * multiplier);
            }
        }
    }
}
