package io.github.rainvaporeon.customenchantments.enchant.buff.misc;

import io.github.rainvaporeon.customenchantments.enchant.SpecialInfusion;
import io.github.rainvaporeon.customenchantments.util.infusions.InfusionUtils;
import io.papermc.paper.event.entity.WardenAngerChangeEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class TranquilityAuraInfusion extends SpecialInfusion {
    @Override
    public String getIdentifier() {
        return "tranquility_aura";
    }

    @Override
    public String getName() {
        return "Tranquility Aura";
    }

    @NotNull
    @Override
    public Listener getListener() {
        return new WardenListener();
    }

    @Override
    public int getMaxLevel() {
        return 3;
    }

    @Override
    public int getMaxEffectiveLevel() {
        return 5;
    }

    @Override
    public String getDescription() {
        return "(20 * level)% chance for Warden to not accumulate anger level against you.";
    }

    @Nullable
    @Override
    public String getExtendedDescription(int level) {
        return String.format("%d%% chance for Warden to not accumulate anger level against you.", level * 20);
    }

    private class WardenListener implements Listener {

        @EventHandler
        public void onAngerLevelChange(WardenAngerChangeEvent event) {
            if (!(event.getTarget() instanceof Player pl)) return;
            int level = InfusionUtils.accumulateInfusionLevelOf(pl, TranquilityAuraInfusion.this);
            if (level == 0) return;
            if (Math.random() < 0.2 * level) event.setCancelled(true);
        }
    }
}
