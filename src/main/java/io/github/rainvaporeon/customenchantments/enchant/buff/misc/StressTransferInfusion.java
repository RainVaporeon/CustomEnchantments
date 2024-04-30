package io.github.rainvaporeon.customenchantments.enchant.buff.misc;

import io.github.rainvaporeon.customenchantments.enchant.Infusion;
import io.github.rainvaporeon.customenchantments.util.SharedConstants;
import io.github.rainvaporeon.customenchantments.util.infusions.InfusionUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class StressTransferInfusion extends Infusion {
    @Override
    public String getIdentifier() {
        return "stress_transfer";
    }

    @Override
    public String getName() {
        return "Stress Transfer";
    }

    @NotNull
    @Override
    public Listener getListener() {
        return new DamageListener();
    }

    @Override
    public int getMaxLevel() {
        return 3;
    }

    @Override
    public String getDescription() {
        return "(5 * level)% of the damage is reflected onto the wearing piece as durability lost.\n" +
                "Does not apply to natural damage (Fire, fall, void...)";
    }

    @Nullable
    @Override
    public String getExtendedDescription(int level) {
        return String.format("%d%% of the damage is reflected onto the wearing piece as durability lost.\n" +
                "Does not apply to natural damage causes.", 5 * level);
    }

    @Override
    public boolean showInSummary() {
        return false;
    }

    class DamageListener implements Listener {
        // high priority: before reductions are applied
        @EventHandler(priority = EventPriority.HIGH)
        public void onDamage(EntityDamageEvent event) {
            if (!(event.getEntity() instanceof Player)) return;
            if (SharedConstants.naturalCauses().contains(event.getCause())) return;
            Player player = (Player) event.getEntity();
            double damage = event.getDamage();
            int reductionPct = 0;
            for (EquipmentSlot slot : StressTransferInfusion.this.applicableSlots()) {
                ItemStack stack = player.getInventory().getItem(slot);
                int level = InfusionUtils.getInfusion(stack, StressTransferInfusion.this);
                if (level == 0) continue;
                int reduction = 5 * level;
                double durabilityPenalty = damage * reduction / 100.0;
                player.damageItemStack(slot, (int) durabilityPenalty);
                reductionPct += reduction;
            }
            if (reductionPct == 0) return;
            event.setDamage(damage * (1 - Math.max(0, reductionPct / 100.0)));
        }
    }
}
