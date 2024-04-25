package io.github.rainvaporeon.customenchantments.enchant.buff.combat;

import io.github.rainvaporeon.customenchantments.enchant.SpecialInfusion;
import io.github.rainvaporeon.customenchantments.util.infusions.InfusionUtils;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class BloodlustInfusion extends SpecialInfusion {
    @Override
    public String getIdentifier() {
        return "bloodlust";
    }

    @Override
    public String getName() {
        return "Bloodlust";
    }

    @Override
    public String getDisplayName() {
        return this.getName();
    }

    @Nullable
    @Override
    public Listener getListener() {
        return new DamageListener();
    }

    @Override
    public int getMaxLevel() {
        return 1;
    }

    @Override
    public String getDescription() {
        return "After killing a mob, your next instance of melee damage in the next 8" +
                " seconds are increased by 25%.";
    }

    @Nullable
    @Override
    public String getExtendedDescription(int level) {
        return this.getDescription();
    }

    class DamageListener implements Listener {
        private final Map<UUID, Long> damageBonusEffects = new HashMap<>(16, 0.25F);

        @EventHandler
        public void onDeath(EntityDeathEvent event) {
            Player player = event.getEntity().getKiller();
            if (player == null) return;
            int level = InfusionUtils.accumulateInfusionLevelOf(player, BloodlustInfusion.this);
            if (level == 0) return;
            damageBonusEffects.put(player.getUniqueId(), System.currentTimeMillis() + 8000);
        }

        @EventHandler
        public void onDamage(EntityDamageByEntityEvent event) {
            Entity src = event.getDamager();
            if (!(src instanceof Player)) return;
            Player player = (Player) src;
            int level = InfusionUtils.accumulateInfusionLevelOf(player, BloodlustInfusion.this);
            if (level == 0) return;
            checkAndClearMap();
            if (damageBonusEffects.containsKey(player.getUniqueId())) {
                event.setDamage(event.getDamage() * 1.25);
                damageBonusEffects.remove(player.getUniqueId());
            }
        }

        private void checkAndClearMap() {
            damageBonusEffects.entrySet().removeIf(e -> e.getValue() < System.currentTimeMillis());
        }
    }
}
