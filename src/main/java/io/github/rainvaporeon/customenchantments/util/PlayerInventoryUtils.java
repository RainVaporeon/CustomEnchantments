package io.github.rainvaporeon.customenchantments.util;

import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PlayerInventoryUtils {
    public static List<ItemStack> collectFromSlot(PlayerInventory inventory, EquipmentSlot... slots) {
        return collectFromSlot(inventory, Arrays.asList(slots));
    }

    public static List<ItemStack> collectFromSlot(PlayerInventory inventory, Iterable<EquipmentSlot> slots) {
        List<ItemStack> is = new ArrayList<>();
        slots.forEach(slot -> {
            if (slot == EquipmentSlot.BODY) return;
            is.add(inventory.getItem(slot));
        });
        return is;
    }
}
