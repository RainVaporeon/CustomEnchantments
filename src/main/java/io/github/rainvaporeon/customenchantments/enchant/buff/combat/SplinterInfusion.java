package io.github.rainvaporeon.customenchantments.enchant.buff.combat;

import de.tr7zw.nbtapi.NBT;
import de.tr7zw.nbtapi.NBTCompound;
import de.tr7zw.nbtapi.NBTCompoundList;
import de.tr7zw.nbtapi.NBTEntity;
import de.tr7zw.nbtapi.iface.ReadWriteNBT;
import io.github.rainvaporeon.customenchantments.enchant.Infusion;
import io.github.rainvaporeon.customenchantments.util.SharedConstants;
import io.github.rainvaporeon.customenchantments.util.collections.TimedHashSet;
import io.github.rainvaporeon.customenchantments.util.enums.InfusionTarget;
import io.github.rainvaporeon.customenchantments.util.infusions.InfusionUtils;
import io.github.rainvaporeon.customenchantments.util.nbt.ItemData;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
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
    public static final String NAME = "splinter";
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

    private ReadWriteNBT getEffectTag(int level) {
        ReadWriteNBT compound = NBT.createNBTObject();
        compound.setString(SharedConstants.INFUSION_ID, SplinterInfusion.NAME);
        compound.setInteger(SharedConstants.INFUSION_LEVEL, level);
        return compound;
    }

    private int readEffectTag(NBTEntity entity) {
        NBTCompoundList list = ItemData.getEntityExtras(entity);
        return list.stream().filter(rw -> rw.getString(SharedConstants.INFUSION_ID).equals(NAME))
                .findFirst().map(rw -> rw.getInteger(SharedConstants.INFUSION_LEVEL)).orElse(0);
    }

    class ShootEventListener implements Listener {
        private final TimedHashSet<Entity> projectiles = new TimedHashSet<>(15000);

        @EventHandler
        public void onArrowShot(EntityShootBowEvent event) {
            if (!(event.getEntity() instanceof Player)) return;
            int level = InfusionUtils.getInfusion(event.getBow(), SplinterInfusion.this);
            if (level == 0) return;
            NBTEntity entity = new NBTEntity(event.getProjectile());
            ItemData.getEntityExtras(entity).addCompound(getEffectTag(level));
            projectiles.add(event.getProjectile());
        }

        @EventHandler
        public void onArrowHit(EntityDamageByEntityEvent event) {
            if (!(event.getDamager() instanceof Player)) return;
            if (!(event.getCause() == EntityDamageEvent.DamageCause.PROJECTILE)) return;
            if (!projectiles.contains(event.getDamager())) return;
            projectiles.remove(event.getDamager());
            int level = readEffectTag(new NBTEntity(event.getDamager()));
            if (level == 0) return;
            Location location = event.getDamager().getLocation();
            event.getDamager().getWorld().getNearbyLivingEntities(location, EFFECTIVE_RADIUS).forEach(e -> {
                e.damage(event.getDamage() * 0.1 * level);
            });
        }
    }
}
