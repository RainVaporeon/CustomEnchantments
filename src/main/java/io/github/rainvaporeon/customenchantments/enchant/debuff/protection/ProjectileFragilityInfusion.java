package io.github.rainvaporeon.customenchantments.enchant.debuff.protection;

import io.github.rainvaporeon.customenchantments.enchant.DebuffInfusion;
import io.github.rainvaporeon.customenchantments.util.infusions.InfusionUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.jetbrains.annotations.Nullable;

public class ProjectileFragilityInfusion extends DebuffInfusion {
    @Override
    public String getIdentifier() {
        return "projectile_fragility";
    }

    @Override
    public String getName() {
        return "Projectile Fragility";
    }

    @Override
    public int getMaxLevel() {
        return 5;
    }

    @Override
    public String getDescription() {
        return "Take (1.1^level) more damage from projectiles.";
    }

    @Nullable
    @Override
    public String getExtendedDescription(int level) {
        return String.format("Take %.1f%% more projectile damage.", Math.pow(1.1, level) * 100);
    }

    @Nullable
    @Override
    public Listener getListener() {
        return new DamageListener();
    }

    class DamageListener implements Listener {
        @EventHandler(priority = EventPriority.LOWEST)
        public void onDamage(EntityDamageEvent event) {
            if (!(event.getEntity() instanceof Player)) return;
            Player player = (Player) event.getEntity();
            EntityDamageEvent.DamageCause cause = event.getCause();
            if (cause != EntityDamageEvent.DamageCause.PROJECTILE) return;
            int level = InfusionUtils.accumulateInfusionLevelOf(player, ProjectileFragilityInfusion.this);
            event.setDamage(event.getDamage() * Math.pow(1.1, level));
        }
    }
}