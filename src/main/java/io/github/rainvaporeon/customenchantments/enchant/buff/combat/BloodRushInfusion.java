package io.github.rainvaporeon.customenchantments.enchant.buff.combat;

import io.github.rainvaporeon.customenchantments.enchant.SpecialInfusion;
import io.github.rainvaporeon.customenchantments.util.infusions.InfusionUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class BloodRushInfusion extends SpecialInfusion {
    @Override
    public String getIdentifier() {
        return "blood_rush";
    }

    @Override
    public String getName() {
        return "Blood Rush";
    }

    @Override
    public String getDisplayName() {
        return this.getName();
    }

    @Override
    public int getMaxLevel() {
        return 1;
    }

    @Override
    public String getDescription() {
        return "Grants Speed I and Haste I on kill for 10 seconds.";
    }

    @Nullable
    @Override
    public String getExtendedDescription(int level) {
        return this.getDescription();
    }

    @Override
    public int getMaxEffectiveLevel() {
        return 1;
    }

    @Override
    public @NotNull Listener getListener() {
        return new KillListener();
    }

    class KillListener implements Listener {
        @EventHandler
        public void onEntityKill(EntityDeathEvent event) {
            Player killer = event.getEntity().getKiller();
            if (killer == null) return;
            int level = InfusionUtils.accumulateInfusionLevelOf(killer, BloodRushInfusion.this);
            if (level == 0) return;
            killer.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 200, 0));
            killer.addPotionEffect(new PotionEffect(PotionEffectType.FAST_DIGGING, 200, 0));
        }
    }
}
