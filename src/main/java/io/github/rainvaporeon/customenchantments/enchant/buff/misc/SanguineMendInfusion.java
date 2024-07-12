package io.github.rainvaporeon.customenchantments.enchant.buff.misc;

import io.github.rainvaporeon.customenchantments.enchant.Infusion;
import io.github.rainvaporeon.customenchantments.util.infusions.InfusionUtils;
import io.github.rainvaporeon.customenchantments.util.server.Server;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;
import java.util.Set;

public class SanguineMendInfusion extends Infusion {
    @Override
    public String getIdentifier() {
        return "sanguine_mend";
    }

    @Override
    public String getName() {
        return "Sanguine Mend";
    }

    @Override
    public String getDescription() {
        return "Recovers (level)% of this item's maximum durability when used to kill a mob.";
    }

    @Nullable
    @Override
    public String getExtendedDescription(int level) {
        return String.format("Recovers %d%% of this item's maximum durability when used to kill a mob.", level);
    }

    @NotNull
    @Override
    public Listener getListener() {
        return new DeathListener();
    }

    @Override
    public int getMaxLevel() {
        return 5;
    }

    @Override
    public Set<EquipmentSlot> applicableSlots() {
        return EnumSet.of(EquipmentSlot.HAND);
    }

    class DeathListener implements Listener {
        @EventHandler
        public void onEntityDeath(EntityDeathEvent event) {
            Player player = event.getEntity().getKiller();
            if (player == null) return;
            ItemStack stack = player.getInventory().getItemInMainHand();
            int level = InfusionUtils.getInfusion(stack, SanguineMendInfusion.this);
            if (level == 0) return;
            ItemMeta meta = stack.getItemMeta();
            if (!(meta instanceof Damageable)) return;
            Damageable dmeta = (Damageable) meta;
            dmeta.setDamage((int) (dmeta.getDamage() - stack.getType().getMaxDurability() * level / 100.0));
            // Running twice so the item does not break and also appears fully durable after the use.
            // Probably a bad idea -- changes are welcome
            stack.setItemMeta(meta);
            Server.runTaskLater(() -> stack.setItemMeta(meta), 1);
        }
    }
}
