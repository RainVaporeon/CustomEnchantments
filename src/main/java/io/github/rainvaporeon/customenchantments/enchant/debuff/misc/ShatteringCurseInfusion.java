package io.github.rainvaporeon.customenchantments.enchant.debuff.misc;

import io.github.rainvaporeon.customenchantments.enchant.DebuffInfusion;
import io.github.rainvaporeon.customenchantments.util.infusions.InfusionUtils;
import io.github.rainvaporeon.customenchantments.util.server.Server;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ShatteringCurseInfusion extends DebuffInfusion {
    @Override
    public String getIdentifier() {
        return "shattering_curse";
    }

    @Override
    public String getName() {
        return "Curse of Shattering";
    }

    @Override
    public @NotNull Listener getListener() {
        return new DamageListener();
    }

    @Override
    public int getMaxLevel() {
        return 10;
    }

    @Override
    public String getDescription() {
        return "(level * 10)% of the final damage dealt is reflected back onto you";
    }

    @Nullable
    @Override
    public String getExtendedDescription(int level) {
        return String.format("Takes %d%% of the final damage dealt.", 10 * level);
    }

    @Override
    public int getMaxEffectiveLevel() {
        return 10;
    }

    class DamageListener implements Listener {
        @EventHandler(priority = EventPriority.MONITOR)
        public void onDamage(EntityDamageByEntityEvent event) {
            if (!(event.getDamager() instanceof Player)) return;
            if (event.getDamager().equals(event.getEntity())) return;
            // sweeping would murder the armor durability and do nothing else, exclude it
            if (event.getCause() == EntityDamageEvent.DamageCause.ENTITY_SWEEP_ATTACK) return;
            Player player = (Player) event.getDamager();
            int level = InfusionUtils.accumulateInfusionLevelOf(player, ShatteringCurseInfusion.this);
            if (level == 0) return;
            double finalDamage = event.getFinalDamage();
            Server.damageInstantly(player, player, finalDamage);
        }
    }
}
