package io.github.rainvaporeon.customenchantments.enchant.buff.misc;

import io.github.rainvaporeon.customenchantments.enchant.Infusion;
import io.github.rainvaporeon.customenchantments.util.infusions.InfusionUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class AcrobaticsInfusion extends Infusion {
    @Override
    public String getIdentifier() {
        return "acrobatics";
    }

    @Override
    public String getName() {
        return "Acrobatics";
    }

    @NotNull
    @Override
    public Listener getListener() {
        return new FallDamageListener();
    }

    @Override
    public int getMaxLevel() {
        return 3;
    }

    @Override
    public String getDescription() {
        return "(level * 5%) chance to not take fall damage.\n" +
                "Additionally, fall damage is reduced by (level * 10%).";
    }

    @Nullable
    @Override
    public String getExtendedDescription(int level) {
        return String.format("%d%% chance to not take fall damage.\n" +
                "Additionally, fall damage is reduced by %d%%.", 5 * level, 10 * level);
    }

    class FallDamageListener implements Listener {
        @EventHandler
        public void onFallDamage(EntityDamageEvent event) {
            if (!(event.getEntity() instanceof Player)) return;
            int level = InfusionUtils.accumulateInfusionLevelOf((Player) event.getEntity(), AcrobaticsInfusion.this);
            if (level == 0) return;
            if (level >= 10 || Math.random() < 0.05 * level) {
                event.setCancelled(true);
            } else {
                event.setDamage(event.getDamage() * Math.max(0, (1 - 0.1 * level)));
            }
        }
    }
}
