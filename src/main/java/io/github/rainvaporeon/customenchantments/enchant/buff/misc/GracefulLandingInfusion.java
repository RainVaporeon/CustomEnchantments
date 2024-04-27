package io.github.rainvaporeon.customenchantments.enchant.buff.misc;

import io.github.rainvaporeon.customenchantments.enchant.SpecialInfusion;
import io.github.rainvaporeon.customenchantments.util.enums.InfusionTarget;
import io.github.rainvaporeon.customenchantments.util.infusions.InfusionUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;
import java.util.Set;

public class GracefulLandingInfusion extends SpecialInfusion {
    @Override
    public String getIdentifier() {
        return "graceful_landing";
    }

    @Override
    public String getName() {
        return "Graceful Landing";
    }

    @Override
    public @NotNull Listener getListener() {
        return new FallDamageListener();
    }

    @Override
    public int getMaxLevel() {
        return 7;
    }

    @Override
    public String getDescription() {
        return "(15 * level)% chance to not take fall damage, and gain Speed II for (level + 3) seconds if so.";
    }

    @Nullable
    @Override
    public String getExtendedDescription(int level) {
        return String.format("%d%% chance not to take fall damage.", 15 * level);
    }

    @Override
    public Set<InfusionTarget> infusionTarget() {
        return EnumSet.of(InfusionTarget.ARMOR_FEET);
    }

    @Override
    public Set<EquipmentSlot> applicableSlots() {
        return EnumSet.of(EquipmentSlot.FEET);
    }

    class FallDamageListener implements Listener {
        @EventHandler
        public void onDamage(EntityDamageEvent event) {
            if (!(event.getEntity() instanceof Player)) return;
            if (event.getCause() != EntityDamageEvent.DamageCause.FALL) return;
            int level = InfusionUtils.accumulateInfusionLevelOf((Player) event.getEntity(), GracefulLandingInfusion.this);
            if (level == 0) return;
            double probability = 0.15 * level;
            if (probability > Math.random()) {
                ((Player) event.getEntity()).addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 20 * (3 + level), 1));
                event.setCancelled(true);
            }
        }
    }
}
