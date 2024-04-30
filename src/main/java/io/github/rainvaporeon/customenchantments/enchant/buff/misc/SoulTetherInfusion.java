package io.github.rainvaporeon.customenchantments.enchant.buff.misc;

import io.github.rainvaporeon.customenchantments.enchant.SpecialInfusion;
import io.github.rainvaporeon.customenchantments.util.infusions.InfusionUtils;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

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
            List<ItemStack> inventory = Arrays.stream(event.getPlayer().getInventory().getContents()).collect(Collectors.toList());
            inventory.removeIf(Objects::isNull);
            inventory.removeIf(is -> InfusionUtils.getInfusion(is, SoulTetherInfusion.this) == 0);
            event.getDrops().removeAll(inventory);
            event.getItemsToKeep().addAll(inventory);
        }
    }
}
