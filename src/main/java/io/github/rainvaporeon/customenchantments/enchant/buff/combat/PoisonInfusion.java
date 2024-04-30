package io.github.rainvaporeon.customenchantments.enchant.buff.combat;

import io.github.rainvaporeon.customenchantments.enchant.SpecialInfusion;
import io.github.rainvaporeon.customenchantments.status.Poison;
import io.github.rainvaporeon.customenchantments.util.infusions.InfusionUtils;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PoisonInfusion extends SpecialInfusion {
    @Override
    public String getIdentifier() {
        return "poison";
    }

    @Override
    public String getName() {
        return "Poison";
    }

    @Override
    public @NotNull Listener getListener() {
        return new DamageListener();
    }

    @Override
    public int getMaxLevel() {
        return 5;
    }

    @Override
    public String getDescription() {
        return "Enemies hit loses (level)% of their max HP over 4 seconds. Does not kill.";
    }

    @Nullable
    @Override
    public String getExtendedDescription(int level) {
        return String.format("Enemies hit loses %d%% of their max HP over 4 seconds.", level);
    }

    class DamageListener implements Listener {
        @EventHandler
        public void onDamage(EntityDamageByEntityEvent event) {
            if (!(event.getDamager() instanceof Player)) return;
            if (!(event.getEntity() instanceof LivingEntity)) return;
            if (event.getDamager().equals(event.getEntity())) return; // do not poison yourself
            int level = InfusionUtils.accumulateInfusionLevelOf((Player) event.getDamager(), PoisonInfusion.this);
            if (level == 0) return;
            Poison.applyPoison((LivingEntity) event.getEntity(), 4, level);
        }
    }
}
