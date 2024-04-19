package io.github.rainvaporeon.customenchantments.enchant;

import io.github.rainvaporeon.customenchantments.util.infusions.InfusionUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.jetbrains.annotations.Nullable;

public class DamageReductionInfusion extends Infusion {

    @Override
    public String getIdentifier() {
        return "damage_reduction";
    }

    @Override
    public String getName() {
        return "Resistance";
    }

    @Override
    public int getMaxLevel() {
        return 5;
    }

    @Nullable
    @Override
    public Listener getListener() {
        return new DamageReductionListener();
    }

    class DamageReductionListener implements Listener {
        @EventHandler
        public void onPlayerHurtEvent(EntityDamageEvent event) {
            if (!(event.getEntity() instanceof Player)) return;
            Player player = (Player) event.getEntity();
            int total = InfusionUtils.accumulateInfusionLevelOf(player, DamageReductionInfusion.this);
            event.setDamage(event.getDamage() * (1 - 0.05 * total));
        }
    }
}
