package io.github.rainvaporeon.customenchantments.util.infusions;

import de.tr7zw.nbtapi.NBTCompoundList;
import de.tr7zw.nbtapi.NBTItem;
import de.tr7zw.nbtapi.iface.ReadWriteNBT;
import io.github.rainvaporeon.customenchantments.CustomEnchantments;
import io.github.rainvaporeon.customenchantments.enchant.Infusion;
import io.github.rainvaporeon.customenchantments.util.PlayerInventoryUtils;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.*;

import java.util.*;

public final class InfusionUtils {
    public static final String INFUSION_IDENTIFIER_KEY = "CEInfusions";
    public static final NamespacedKey INFUSION_NAMESPACE = new NamespacedKey(CustomEnchantments.PLUGIN, INFUSION_IDENTIFIER_KEY);

    public static boolean applyInfusion(ItemStack stack, String identifier, int level) {
        NBTItem item = new NBTItem(stack);
        Infusion infusion = InfusionManager.getInfusionById(identifier);
        if (infusion == null) return false;
        item.getCompoundList(INFUSION_IDENTIFIER_KEY).addCompound(infusion.getNBT(level));
        InfusionLoreUtils.applyLore(item.getOrCreateCompound("display").getCompoundList("Lore"), infusion, level);
        return true;
    }

    public static boolean removeInfusion(ItemStack stack, String identifier) {
        NBTItem item = new NBTItem(stack);
        Infusion infusion = InfusionManager.getInfusionById(identifier);
        if (infusion == null) return false;
        InfusionLoreUtils.removeLore(item.getOrCreateCompound("display").getCompoundList("Lore"), infusion);
        return item.getCompoundList(INFUSION_IDENTIFIER_KEY).removeIf(rw -> rw.getString(Infusion.INFUSION_ID).equals(identifier));
    }

    public static int accumulateInfusionLevelOf(Player entity, Infusion infusion) {
        PlayerInventory inventory = entity.getInventory();
        Set<EnchantmentTarget> targetSet = infusion.infusionTarget();
        Set<EquipmentSlot> allowedSlots = infusion.applicableSlots();
        List<ItemStack> applicableList = PlayerInventoryUtils.collectFromSlot(inventory, allowedSlots);
        applicableList.removeIf(item -> targetSet.stream().noneMatch(e -> e.includes(item)));
        return applicableList.stream().mapToInt(is -> InfusionUtils.getInfusion(is, infusion)).sum();
    }

    public static Map<Infusion, Integer> getAllInfusions(ItemStack stack) {
        NBTItem item = new NBTItem(stack);
        NBTCompoundList list = item.getCompoundList(INFUSION_IDENTIFIER_KEY);
        Map<Infusion, Integer> infusionMap = new HashMap<>();
        list.forEach(rw -> {
            String identifier = rw.getString(Infusion.INFUSION_ID);
            Integer level = rw.getInteger(Infusion.INFUSION_LEVEL);
            if (identifier == null || level == null) return;
            infusionMap.put(InfusionManager.getInfusionById(identifier), level);
        });
        return infusionMap;
    }

    public static int getInfusion(ItemStack stack, Infusion type) {
        NBTItem item = new NBTItem(stack);
        NBTCompoundList list = item.getCompoundList(INFUSION_IDENTIFIER_KEY);
        Map<Infusion, Integer> infusionMap = new HashMap<>();
        ReadWriteNBT nbt = list.stream()
                .filter(rw -> rw.getString(Infusion.INFUSION_ID).equals(type.getIdentifier()))
                .findFirst().orElse(null);
        return nbt == null ? 0 : nbt.getInteger(Infusion.INFUSION_LEVEL);
    }
}
