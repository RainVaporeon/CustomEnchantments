package io.github.rainvaporeon.customenchantments.enchant.buff.misc;

import io.github.rainvaporeon.customenchantments.enchant.Infusion;
import io.github.rainvaporeon.customenchantments.util.infusions.InfusionUtils;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerExpChangeEvent;
import org.jetbrains.annotations.Nullable;

public class KnowledgeBoostInfusion extends Infusion {
    @Override
    public String getIdentifier() {
        return "enlightened";
    }

    @Override
    public String getName() {
        return "Enlightened";
    }

    @Nullable
    @Override
    public Listener getListener() {
        return new ExperienceGainListener();
    }

    @Override
    public int getMaxLevel() {
        return 50;
    }

    @Override
    public String getDescription() {
        return "Gain (2 * level)% more experience from all sources.";
    }

    @Nullable
    @Override
    public String getExtendedDescription(int level) {
        return String.format("Gain %d%% more experience from all sources", 2 * level);
    }

    class ExperienceGainListener implements Listener {
        @EventHandler
        public void onExperienceGain(PlayerExpChangeEvent event) {
            int amount = event.getAmount();
            int level = InfusionUtils.accumulateInfusionLevelOf(event.getPlayer(), KnowledgeBoostInfusion.this);
            double amplifier = 1 + 0.02 * level;
            event.setAmount((int) Math.round(amount * amplifier));
        }
    }
}
