package io.github.rainvaporeon.customenchantments.enchant.debuff.misc;

import io.github.rainvaporeon.customenchantments.enchant.DebuffInfusion;
import io.github.rainvaporeon.customenchantments.util.infusions.InfusionUtils;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemDamageEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class BrittleInfusion extends DebuffInfusion {
    @Override
    public String getIdentifier() {
        return "brittle";
    }

    @Override
    public String getName() {
        return "Brittle";
    }

    @Override
    public String getDescription() {
        return "Loses (10*level)% more durability on the item.";
    }

    @NotNull
    @Override
    public Listener getListener() {
        return new DurabilityLostListener();
    }

    @Override
    public int getMaxLevel() {
        return 10;
    }

    @Override
    public boolean showInSummary() {
        return false;
    }

    class DurabilityLostListener implements Listener {
        @EventHandler
        public void onDurabilityLost(PlayerItemDamageEvent event) {
            ItemStack stack = event.getItem();
            if (stack.isEmpty()) return;
            int level = InfusionUtils.getInfusion(stack, BrittleInfusion.this);
            if (level == 0) return;
            event.setDamage((int) (event.getDamage() * (1 + 0.1 * level)));
        }
    }
}
