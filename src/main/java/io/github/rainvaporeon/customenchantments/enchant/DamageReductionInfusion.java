package io.github.rainvaporeon.customenchantments.enchant;

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
        return super.getListener();
    }

    static class DamageReductionListener implements Listener {

        @EventHandler
        public void onPlayerHurtEvent(EntityDamageEvent event) {

        }

    }
}
