package io.github.rainvaporeon.customenchantments.enchant.debuff;

import io.github.rainvaporeon.customenchantments.enchant.DebuffInfusion;
import io.github.rainvaporeon.customenchantments.util.infusions.InfusionUtils;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class RepairFragilityInfusion extends DebuffInfusion {
    @Override
    public String getIdentifier() {
        return "repair_fragility";
    }

    @Override
    public String getName() {
        return "Repair Fragility";
    }

    @Override
    public String getDisplayName() {
        return this.getName();
    }

    @Override
    public @NotNull Listener getListener() {
        return new AnvilListener();
    }

    @Override
    public boolean showInSummary() {
        return false;
    }

    @Override
    public String getDescription() {
        return "This item cannot be used in anvils.";
    }

    @Nullable
    @Override
    public String getExtendedDescription(int level) {
        return this.getDescription();
    }

    @Override
    public int getMaxLevel() {
        return 1;
    }

    @Override
    public int getMaxEffectiveLevel() {
        return 1;
    }

    class AnvilListener implements Listener {
        @EventHandler(priority = EventPriority.HIGH)
        public void onAnvilPrepare(PrepareAnvilEvent event) {
            ItemStack left = event.getInventory().getFirstItem();
            ItemStack right = event.getInventory().getSecondItem();
            if (left == null) return;
            if (right == null) return;
            if (InfusionUtils.getInfusion(left, RepairFragilityInfusion.this) != 0) {
                event.setResult(null);
            }
            if (InfusionUtils.getInfusion(right, RepairFragilityInfusion.this) != 0) {
                event.setResult(null);
            }
        }
    }
}
