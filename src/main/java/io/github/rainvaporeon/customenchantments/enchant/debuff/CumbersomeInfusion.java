package io.github.rainvaporeon.customenchantments.enchant.debuff;

import io.github.rainvaporeon.customenchantments.enchant.DebuffInfusion;
import io.github.rainvaporeon.customenchantments.util.infusions.InfusionUtils;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CumbersomeInfusion extends DebuffInfusion {
    @Override
    public String getIdentifier() {
        return "cumbersome";
    }

    @Override
    public String getName() {
        return "Cumbersome";
    }

    @Override
    public int getMaxLevel() {
        return 1;
    }

    @Override
    public String getDisplayName(int level) {
        return level == 1 ? this.getName() : super.getDisplayName(level);
    }

    @Override
    public String getDescription() {
        return "Final critical hit damages are reduced by 35%.";
    }

    @Nullable
    @Override
    public String getExtendedDescription(int level) {
        return this.getDescription();
    }

    @Override
    public int getMaxEffectiveLevel() {
        return 1;
    }

    @Override
    public @NotNull Listener getListener() {
        return new DamageListener();
    }

    class DamageListener implements Listener {
        @EventHandler(priority = EventPriority.HIGH)
        public void onDamage(EntityDamageByEntityEvent event) {
            if (!(event.getDamager() instanceof Player)) return;
            if (!(event.getEntity() instanceof LivingEntity)) return;
            if (!event.isCritical()) return;
            int level = InfusionUtils.accumulateInfusionLevelOf((Player) event.getDamager(), CumbersomeInfusion.this);
            if (level == 0) return;
            event.setDamage(event.getDamage() * 0.65);
        }
    }
}
