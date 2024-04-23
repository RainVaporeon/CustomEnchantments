package io.github.rainvaporeon.customenchantments.enchant.buff.combat;

import io.github.rainvaporeon.customenchantments.enchant.SpecialInfusion;
import io.github.rainvaporeon.customenchantments.status.Bleeding;
import io.github.rainvaporeon.customenchantments.util.infusions.InfusionUtils;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.jetbrains.annotations.Nullable;

public class BleedingInfusion extends SpecialInfusion {
    @Override
    public String getIdentifier() {
        return "bleeding";
    }

    @Override
    public String getName() {
        return "Bleeding";
    }

    @Override
    public int getMaxLevel() {
        return 5;
    }

    @Override
    public String getDescription() {
        return "Enemies hit by a critical hit bleeds for 3 seconds,\n" +
                "losing sqrt(level) health every second.";
    }

    @Nullable
    @Override
    public String getExtendedDescription(int level) {
        return String.format("Enemies hit with a critical hit loses %.1f HP/s for 3 second", Math.sqrt(level));
    }

    @Nullable
    @Override
    public Listener getListener() {
        return new DamageListener();
    }


    class DamageListener implements Listener {

        @EventHandler
        public void onDamage(EntityDamageByEntityEvent event) {
            if (!(event.getDamager() instanceof Player)) return;
            if (!(event.getEntity() instanceof LivingEntity)) return;
            if (!event.isCritical()) return;
            int level = InfusionUtils.accumulateInfusionLevelOf((Player) event.getDamager(), BleedingInfusion.this);
            if (level == 0) return;
            Bleeding.applyBleeding((LivingEntity) event.getEntity(), 3, level);
        }
    }
}
