package io.github.rainvaporeon.customenchantments.util;

import de.tr7zw.nbtapi.NBTCompoundList;
import de.tr7zw.nbtapi.NBTItem;
import de.tr7zw.nbtapi.iface.ReadWriteNBT;
import io.github.rainvaporeon.customenchantments.enchant.Infusion;
import io.github.rainvaporeon.customenchantments.util.infusions.InfusionLoreUtils;
import io.github.rainvaporeon.customenchantments.util.infusions.InfusionManager;
import io.github.rainvaporeon.customenchantments.util.infusions.InfusionUtils;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InfusionMigrationUtils {
    public static ItemStack migrate(ItemStack stack) {
        if (stack == null || stack.isEmpty()) return stack;
        NBTItem item = new NBTItem(stack);
        Map<Infusion, Integer> replaceMap = new HashMap<>();
        List<ReadWriteNBT> removalList = new ArrayList<>();
        NBTCompoundList list = item.getCompoundList(InfusionUtils.INFUSION_IDENTIFIER_KEY);
        for (ReadWriteNBT nbt : list) {
            String id = nbt.getString(Infusion.INFUSION_ID);
            int level = nbt.getInteger(Infusion.INFUSION_LEVEL);

            String mapping = InfusionManager.getMigrationMapping(id);
            if (mapping == null) continue;
            replaceMap.put(InfusionManager.getInfusionById(mapping), level);
            removalList.add(nbt);
        }
        list.removeIf(removalList::contains);
        replaceMap.forEach((k, v) -> list.addCompound(k.getNBT(v)));
        InfusionLoreUtils.applySortedLoreNBT(stack);
        return stack;
    }
}
