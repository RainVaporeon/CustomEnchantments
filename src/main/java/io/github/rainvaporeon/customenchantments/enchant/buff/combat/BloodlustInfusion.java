package io.github.rainvaporeon.customenchantments.enchant.buff.combat;

import io.github.rainvaporeon.customenchantments.enchant.SpecialInfusion;
import io.github.rainvaporeon.customenchantments.util.infusions.InfusionUtils;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.jetbrains.annotations.NotNull;
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
    public String getDisplayName(int level) {
        return level == 1 ? this.getName() : super.getDisplayName(level);
    }

    @Override
    public @NotNull Listener getListener() {
        return new DamageListener();
    }

    @Override
    public int getMaxLevel() {
        return 1;
    }

    @Override
    public String getDescription() {
        return "After killing a mob, your next instance of melee damage in the next 8" +
                " seconds is increased by (25 * level)%.";
    }

    @Nullable
    @Override
    public String getExtendedDescription(int level) {
        return String.format("After killing a mob, your next instance of melee damage in the next 8" +
                " seconds is increased by %d%%.", 25 * level);
    }

    class DamageListener implements Listener {
        private final Map<UUID, DamageInfo> damageBonusEffects = new HashMap<>(16, 0.25F);
        final DamageInfo empty = new DamageInfo(0, 0);

        @EventHandler
        public void onDeath(EntityDeathEvent event) {
            Player player = event.getEntity().getKiller();
            if (player == null) return;
            int level = InfusionUtils.accumulateInfusionLevelOf(player, BloodlustInfusion.this);
            if (level == 0) return;
            damageBonusEffects.put(player.getUniqueId(), new DamageInfo(System.currentTimeMillis() + 8000, 0.25 * level));
        }

        @EventHandler
        public void onDamage(EntityDamageByEntityEvent event) {
            Entity src = event.getDamager();
            if (!(src instanceof Player)) return;
            if (event.getCause() != EntityDamageEvent.DamageCause.ENTITY_ATTACK) return;
            Player player = (Player) src;
            int level = InfusionUtils.accumulateInfusionLevelOf(player, BloodlustInfusion.this);
            if (level == 0) return;
            checkAndClearMap();
            if (damageBonusEffects.containsKey(player.getUniqueId())) {
                event.setDamage(event.getDamage() * damageBonusEffects.getOrDefault(player.getUniqueId(), empty).amplifier);
                damageBonusEffects.remove(player.getUniqueId());
            }
        }

        private void checkAndClearMap() {
            damageBonusEffects.entrySet().removeIf(e -> e.getValue().expiry < System.currentTimeMillis());
        }

        private class DamageInfo {
            final long expiry;
            final double amplifier;

            public DamageInfo(long expiry, double amplifier) {
                this.expiry = expiry;
                this.amplifier = 1 + amplifier;
            }
        }
    }
}
