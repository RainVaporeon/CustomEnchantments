package io.github.rainvaporeon.customenchantments.util.infusions;

import de.tr7zw.nbtapi.NBTCompoundList;
import de.tr7zw.nbtapi.NBTItem;
import de.tr7zw.nbtapi.iface.ReadWriteNBT;
import io.github.rainvaporeon.customenchantments.CustomEnchantments;
import io.github.rainvaporeon.customenchantments.enchant.Infusion;
import io.github.rainvaporeon.customenchantments.util.PlayerInventoryUtils;
import io.github.rainvaporeon.customenchantments.util.enums.InfusionTarget;
import io.github.rainvaporeon.customenchantments.util.enums.Result;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.*;

import java.util.*;

public final class InfusionUtils {
    public static final String INFUSION_IDENTIFIER_KEY = "CEInfusions";
    public static final NamespacedKey INFUSION_NAMESPACE = new NamespacedKey(CustomEnchantments.PLUGIN, INFUSION_IDENTIFIER_KEY);

    /**
     * Applies the infusion, changing the lore to adapt
     * @param stack the item stack to apply to
     * @param identifier the infusion identifier
     * @param level the level
     * @return whether this succeeds.
     */
    public static boolean applyInfusion(ItemStack stack, String identifier, int level) {
        if (stack == null || stack.isEmpty()) return false;
        Infusion infusion = InfusionManager.getInfusionById(identifier);
        if (infusion == null) return false;
        NBTItem item = new NBTItem(stack);
        if (InfusionUtils.getInfusion(stack, infusion) > 0) removeInfusionData(item, identifier);
        InfusionLoreUtils.removeLoreNBT(item, infusion);
        item.getCompoundList(INFUSION_IDENTIFIER_KEY).addCompound(infusion.getNBT(level));
        // InfusionLoreUtils.applyLoreNBT(item, infusion, level);
        item.applyNBT(stack);
        InfusionLoreUtils.applySortedLoreNBT(stack);
        return true;
    }

    /**
     * Removes the infusion, changing the lore to adapt
     * @param stack the item stack to remove
     * @param identifier the infusion identifier
     * @return whether this succeeds.
     */
    public static boolean removeInfusion(ItemStack stack, String identifier) {
        if (stack == null || stack.isEmpty()) return false;
        Infusion infusion = InfusionManager.getInfusionById(identifier);
        if (infusion == null) return false;
        NBTItem item = new NBTItem(stack);
        // InfusionLoreUtils.removeLoreNBT(item, infusion);
        boolean removed = removeInfusionData(item, identifier);
        item.applyNBT(stack);
        InfusionLoreUtils.applySortedLoreNBT(stack);
        return removed;
    }

    private static boolean removeInfusionData(NBTItem nbt, String identifier) {
        return nbt.getCompoundList(INFUSION_IDENTIFIER_KEY)
                .removeIf(rw -> rw.getString(Infusion.INFUSION_ID).equals(identifier));
    }

    public static int accumulateInfusionLevelOf(Player entity, Infusion infusion) {
        PlayerInventory inventory = entity.getInventory();
        Set<InfusionTarget> targetSet = infusion.infusionTarget();
        Set<EquipmentSlot> allowedSlots = infusion.applicableSlots();
        List<ItemStack> applicableList = PlayerInventoryUtils.collectFromSlot(inventory, allowedSlots);
        applicableList.removeIf(item -> targetSet.stream().noneMatch(e -> e.includes(item)));
        applicableList.removeIf(item -> item == null || item.isEmpty());
        return applicableList.stream().mapToInt(is -> InfusionUtils.getInfusion(is, infusion)).sum();
    }

    public static Map<Infusion, Integer> getAllInfusions(ItemStack stack) {
        if (stack == null || stack.isEmpty()) return Collections.emptyMap();
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
        if (stack == null || stack.isEmpty()) return 0;
        NBTItem item = new NBTItem(stack);
        NBTCompoundList list = item.getCompoundList(INFUSION_IDENTIFIER_KEY);
        ReadWriteNBT nbt = list.stream()
                .filter(rw -> rw.getString(Infusion.INFUSION_ID).equals(type.getIdentifier()))
                .findFirst().orElse(null);
        return nbt == null ? 0 : nbt.getInteger(Infusion.INFUSION_LEVEL);
    }

    public static Result testInfusion(ItemStack stack, Infusion infusion) {
        if (stack == null || infusion == null) return Result.FAIL;
        if (infusion.infusionTarget().stream().anyMatch(target -> target.includes(stack))) return Result.SUCCESSFUL;
        return Result.INCOMPATIBLE_TARGET;
    }
}
