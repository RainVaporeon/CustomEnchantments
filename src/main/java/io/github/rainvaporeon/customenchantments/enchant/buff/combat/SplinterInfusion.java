package io.github.rainvaporeon.customenchantments.enchant.buff.combat;

import io.github.rainvaporeon.customenchantments.enchant.Infusion;
import io.github.rainvaporeon.customenchantments.util.enums.InfusionTarget;
import io.github.rainvaporeon.customenchantments.util.infusions.InfusionUtils;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;
import java.util.Set;

public class SplinterInfusion extends Infusion {
    public static final double EFFECTIVE_RADIUS = 4.0;

    @Override
    public String getIdentifier() {
        return "splinter";
    }

    @Override
    public String getName() {
        return "Splinter";
    }

    @Override
    public String getDescription() {
        return "(10*level)% of the arrow damage gets transferred to nearby mobs.";
    }

    @Nullable
    @Override
    public String getExtendedDescription(int level) {
        return String.format("%d%% of the arrow damage done to the entity also gets spread to entities in a 4 block radius.", level * 10);
    }

    @NotNull
    @Override
    public Listener getListener() {
        return new ShootEventListener();
    }

    @Override
    public Set<EquipmentSlot> applicableSlots() {
        return EnumSet.of(EquipmentSlot.HAND);
    }

    @Override
    public Set<InfusionTarget> infusionTarget() {
        return EnumSet.of(InfusionTarget.BOW, InfusionTarget.CROSSBOW);
    }

    @Override
    public int getMaxLevel() {
        return 5;
    }

    private void appendEffectTag(Entity entity, int level) {
        entity.addScoreboardTag("ce.splinter." + level);
    }

    private static final int TAG_LENGTH = "ce.splinter.".length();
    private int readEffectTag(Entity entity) {
        return entity.getScoreboardTags()
                .stream()
                .filter(tag -> tag.startsWith("ce.splinter."))
                .findFirst()
                .map(s -> Integer.parseInt(s.substring(TAG_LENGTH)))
                .orElse(0);
    }

    class ShootEventListener implements Listener {

        @EventHandler
        public void onArrowShot(EntityShootBowEvent event) {
            if (!(event.getEntity() instanceof Player)) return;
            int level = InfusionUtils.getInfusion(event.getBow(), SplinterInfusion.this);
            if (level == 0) return;
            appendEffectTag(event.getProjectile(), level);
        }

        @EventHandler
        public void onArrowHit(EntityDamageByEntityEvent event) {
            if (!(event.getDamager() instanceof Player)) return;
            if (!(event.getCause() == EntityDamageEvent.DamageCause.PROJECTILE)) return;
            int level = readEffectTag(event.getDamager());
            if (level == 0) return;
            Location location = event.getDamager().getLocation();
            event.getDamager().getWorld().getNearbyLivingEntities(location, EFFECTIVE_RADIUS).forEach(e -> {
                e.damage(event.getDamage() * 0.1 * level);
            });
        }
    }
}
