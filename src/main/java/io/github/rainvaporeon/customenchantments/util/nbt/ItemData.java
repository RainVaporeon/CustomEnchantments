package io.github.rainvaporeon.customenchantments.util.nbt;

import de.tr7zw.nbtapi.iface.ReadWriteNBT;
import de.tr7zw.nbtapi.iface.ReadWriteNBTCompoundList;
import io.github.rainvaporeon.customenchantments.util.SharedConstants;

public class ItemData {
    public static ReadWriteNBTCompoundList getInfusions(ReadWriteNBT item) {
        return item.getCompoundList(SharedConstants.INFUSION_IDENTIFIER_KEY);
    }

    public static ReadWriteNBTCompoundList getStoredInfusions(ReadWriteNBT item) {
        return item.getCompoundList(SharedConstants.STORED_INFUSION_IDENTIFIER_KEY);
    }

    public static ReadWriteNBTCompoundList getEntityExtras(ReadWriteNBT entity) {
        return entity.getCompoundList(SharedConstants.ENTITY_EXTRA_DATA);
    }
}
