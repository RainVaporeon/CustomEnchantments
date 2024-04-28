package io.github.rainvaporeon.customenchantments.enchant.buff.misc;

import io.github.rainvaporeon.customenchantments.enchant.SpecialInfusion;
import io.github.rainvaporeon.customenchantments.util.infusions.InfusionUtils;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemDamageEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class TitanicInfusion extends SpecialInfusion {
    @Override
    public String getIdentifier() {
        return "titanic";
    }

    @Override
    public String getName() {
        return "Titanic";
    }

    @Override
    public String getDisplayName(int level) {
        return level == 1 ? this.getName() : super.getDisplayName(level);
    }

    @Override
    public String getDescription() {
        return "Halves durability lost on the item.";
    }

    @Nullable
    @Override
    public String getExtendedDescription(int level) {
        return this.getDescription();
    }

    @NotNull
    @Override
    public Listener getListener() {
        return new DurabilityLostListener();
    }

    @Override
    public int getMaxLevel() {
        return 1;
    }

    @Override
    public boolean showInSummary() {
        return false;
    }

    class DurabilityLostListener implements Listener {
        @EventHandler
        public void onDurabilityLost(PlayerItemDamageEvent event) {
            ItemStack item = event.getItem();
            if (item.isEmpty()) return;
            int level = InfusionUtils.getInfusion(item, TitanicInfusion.this);
            if (level == 0) return;
            if (Math.random() >= 0.5) event.setDamage(event.getDamage() / 2);
        }
    }
}
