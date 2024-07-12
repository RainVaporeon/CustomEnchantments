package io.github.rainvaporeon.customenchantments.enchant.buff.combat;

import io.github.rainvaporeon.customenchantments.enchant.Infusion;
import io.github.rainvaporeon.customenchantments.util.infusions.InfusionUtils;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class KillStreakInfusion extends Infusion {
    @Override
    public String getIdentifier() {
        return "killstreak";
    }

    @Override
    public String getName() {
        return "Kill Streak";
    }

    @Override
    public @NotNull Listener getListener() {
        return new DamageListener();
    }

    @Override
    public int getMaxLevel() {
        return 2;
    }

    @Override
    public String getDescription() {
        return "After killing a mob, your damage increases by 10% per level for 8 seconds.\n" +
                "Effect is stackable.";
    }

    @Nullable
    @Override
    public String getExtendedDescription(int level) {
        return String.format("After killing a mob, your damage in the next 8" +
                " seconds is increased by %d%%.", 10 * level);
    }

    class DamageListener implements Listener {
        private final Map<UUID, List<DamageInfo>> damageBonusEffects = new HashMap<>(16, 0.25F);
        final DamageListener.DamageInfo empty = new DamageListener.DamageInfo(0, 0);

        @EventHandler
        public void onDeath(EntityDeathEvent event) {
            Player player = event.getEntity().getKiller();
            if (player == null) return;
            int level = InfusionUtils.accumulateInfusionLevelOf(player, KillStreakInfusion.this);
            if (level == 0) return;
            if (damageBonusEffects.containsKey(player.getUniqueId())) {
                damageBonusEffects.get(player.getUniqueId()).add(new DamageListener.DamageInfo(System.currentTimeMillis() + 8000, 0.1 * level));
            } else {
                damageBonusEffects.put(player.getUniqueId(), new ArrayList<>());
                damageBonusEffects.get(player.getUniqueId()).add(new DamageListener.DamageInfo(System.currentTimeMillis() + 8000, 0.1 * level));
            }
        }

        @EventHandler(priority = EventPriority.HIGH)
        public void onDamage(EntityDamageByEntityEvent event) {
            Entity src = event.getDamager();
            if (!(src instanceof Player)) return;
            if (event.getCause() == EntityDamageEvent.DamageCause.ENTITY_SWEEP_ATTACK) return;
            Player player = (Player) src;
            int level = InfusionUtils.accumulateInfusionLevelOf(player, KillStreakInfusion.this);
            if (level == 0) return;
            checkAndClearMap();
            if (damageBonusEffects.containsKey(player.getUniqueId())) {
                double amplifier = damageBonusEffects.getOrDefault(player.getUniqueId(), Collections.singletonList(empty))
                                .stream().mapToDouble(info -> info.amplifier).sum();
                event.setDamage(event.getDamage() * (1 + amplifier));
            }
        }

        private void checkAndClearMap() {
            damageBonusEffects.values().forEach(ls -> ls.removeIf(info -> info.expiry < System.currentTimeMillis()));
            damageBonusEffects.entrySet().removeIf(e -> e.getValue().isEmpty());
        }

        private class DamageInfo {
            final long expiry;
            final double amplifier;

            public DamageInfo(long expiry, double amplifier) {
                this.expiry = expiry;
                this.amplifier = amplifier;
            }
        }
    }
}
