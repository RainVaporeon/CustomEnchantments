package io.github.rainvaporeon.customenchantments.enchant.debuff.misc;

import io.github.rainvaporeon.customenchantments.enchant.DebuffInfusion;
import io.github.rainvaporeon.customenchantments.util.infusions.InfusionUtils;
import org.bukkit.damage.DamageSource;
import org.bukkit.damage.DamageType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
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

    @Nullable
    @Override
    public Listener getListener() {
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
            Player player = (Player) event.getDamager();
            int level = InfusionUtils.accumulateInfusionLevelOf(player, ShatteringCurseInfusion.this);
            if (level == 0) return;
            double finalDamage = event.getFinalDamage();
            player.damage(finalDamage, player);
        }
    }
}
