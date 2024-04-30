package io.github.rainvaporeon.customenchantments.util.nbt;

import de.tr7zw.nbtapi.NBTCompoundList;
import de.tr7zw.nbtapi.NBTEntity;
import de.tr7zw.nbtapi.NBTItem;
import io.github.rainvaporeon.customenchantments.util.SharedConstants;

public class ItemData {
    public static NBTCompoundList getInfusions(NBTItem item) {
        return item.getCompoundList(SharedConstants.INFUSION_IDENTIFIER_KEY);
    }

    public static NBTCompoundList getStoredInfusions(NBTItem item) {
        return item.getCompoundList(SharedConstants.STORED_INFUSION_IDENTIFIER_KEY);
    }

    public static NBTCompoundList getEntityExtras(NBTEntity entity) {
        return entity.getCompoundList(SharedConstants.ENTITY_EXTRA_DATA);
    }
}
