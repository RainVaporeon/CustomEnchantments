package io.github.rainvaporeon.customenchantments.enchant.buff.misc;

import io.github.rainvaporeon.customenchantments.enchant.SpecialInfusion;
import io.github.rainvaporeon.customenchantments.util.infusions.InfusionUtils;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;

public class SoulTetherInfusion extends SpecialInfusion {
    @Override
    public String getIdentifier() {
        return "soul_tether";
    }

    @Override
    public String getName() {
        return "Soul Tether";
    }

    @NotNull
    @Override
    public Listener getListener() {
        return new DeathListener();
    }

    @Override
    public int getMaxLevel() {
        return 1;
    }

    @Override
    public String getDescription() {
        return "Items with this enchantment does not drop on death.";
    }

    @Override
    public String getDisplayName(int level) {
        return level == 1 ? this.getName() : super.getDisplayName(level);
    }

    @Override
    public boolean showInSummary() {
        return false;
    }

    class DeathListener implements Listener {
        @EventHandler
        public void onPlayerDeath(PlayerDeathEvent event) {
            if (event.getKeepInventory()) return; // keep inv, this infusion is useless
            Iterator<ItemStack> it = event.getDrops().iterator();
            while (it.hasNext()) {
                ItemStack stx = it.next();
                if (InfusionUtils.getInfusion(stx, SoulTetherInfusion.this) == 0) continue;
                it.remove();
                event.getItemsToKeep().add(stx);
            }
        }
    }
}
