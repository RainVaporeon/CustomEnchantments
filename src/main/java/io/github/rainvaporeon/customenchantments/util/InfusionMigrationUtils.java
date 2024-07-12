package io.github.rainvaporeon.customenchantments.util;

import de.tr7zw.nbtapi.NBT;
import de.tr7zw.nbtapi.NBTCompoundList;
import de.tr7zw.nbtapi.NBTItem;
import de.tr7zw.nbtapi.iface.ReadWriteNBT;
import de.tr7zw.nbtapi.iface.ReadWriteNBTCompoundList;
import io.github.rainvaporeon.customenchantments.enchant.Infusion;
import io.github.rainvaporeon.customenchantments.util.infusions.InfusionLoreUtils;
import io.github.rainvaporeon.customenchantments.util.infusions.InfusionManager;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InfusionMigrationUtils {
    public static ItemStack migrate(ItemStack stack) {
        if (stack == null || stack.isEmpty()) return stack;
        ReadWriteNBT item = NBT.itemStackToNBT(stack);
        Map<Infusion, Integer> replaceMap = new HashMap<>();
        List<ReadWriteNBT> removalList = new ArrayList<>();
        ReadWriteNBTCompoundList list = item.getCompoundList(SharedConstants.INFUSION_IDENTIFIER_KEY);
        for (ReadWriteNBT nbt : list) {
            String id = nbt.getString(SharedConstants.INFUSION_ID);
            int level = nbt.getInteger(SharedConstants.INFUSION_LEVEL);

            String mapping = InfusionManager.getMigrationMapping(id);
            if (mapping == null) continue;
            replaceMap.put(InfusionManager.getInfusionById(mapping), level);
            removalList.add(nbt);
        }
        ReadWriteNBTCompoundList store = item.getCompoundList(SharedConstants.STORED_INFUSION_IDENTIFIER_KEY);
        List<ReadWriteNBT> storeRemovalList = new ArrayList<>();
        Map<Infusion, Integer> storeReplaceMap = new HashMap<>();
        for (ReadWriteNBT nbt : store) {
            String id = nbt.getString(SharedConstants.INFUSION_ID);
            int level = nbt.getInteger(SharedConstants.INFUSION_LEVEL);

            String mapping = InfusionManager.getMigrationMapping(id);
            if (mapping == null) continue;
            storeReplaceMap.put(InfusionManager.getInfusionById(mapping), level);
            storeRemovalList.add(nbt);
        }
        list.removeIf(removalList::contains);
        replaceMap.forEach((k, v) -> list.addCompound().mergeCompound(k.getNBT(v)));
        store.removeIf(storeRemovalList::contains);
        storeReplaceMap.forEach((k, v) -> store.addCompound().mergeCompound(k.getNBT(v)));
        InfusionLoreUtils.applySortedLoreNBT(stack);
        return stack;
    }
}
