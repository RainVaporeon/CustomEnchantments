package io.github.rainvaporeon.customenchantments.enchant.buff.combat;

import io.github.rainvaporeon.customenchantments.enchant.SpecialInfusion;
import io.github.rainvaporeon.customenchantments.util.infusions.InfusionUtils;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class LifeStealInfusion extends SpecialInfusion {
    @Override
    public String getIdentifier() {
        return "life_steal";
    }

    @Override
    public String getName() {
        return "Life Steal";
    }

    @Override
    public @NotNull Listener getListener() {
        return new DamageListener();
    }

    @Override
    public int getMaxLevel() {
        return 3;
    }

    @Override
    public String getDescription() {
        return "(5*level)% of the final damage dealt gets converted to healing.";
    }

    @Nullable
    @Override
    public String getExtendedDescription(int level) {
        return String.format("%d%% of the damage dealt gets converted to healing.", 5 * level);
    }

    class DamageListener implements Listener {
        @EventHandler
        public void onDamage(EntityDamageByEntityEvent event) {
            Entity src = event.getDamager();
            if (!(src instanceof Player)) return;
            Player player = (Player) src;
            int level = InfusionUtils.accumulateInfusionLevelOf(player, LifeStealInfusion.this);
            if (level == 0) return;
            double healing = (0.05 * level) * event.getFinalDamage();
            player.setHealth(player.getHealth() + healing);
        }
    }
}
