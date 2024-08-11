package io.github.rainvaporeon.customenchantments.enchant.set;

import io.github.rainvaporeon.customenchantments.enchant.SetInfusion;
import io.github.rainvaporeon.customenchantments.util.infusions.InfusionInfo;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.EquipmentSlot;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

import java.util.Collections;
import java.util.EnumSet;
import java.util.Set;

// holy scallops is that a wynncraft reference???
public class MorphSetInfusion extends SetInfusion {
    @Override
    public String getIdentifier() {
        return "set_morph";
    }

    @Override
    public String getSetName() {
        return "Morph";
    }

    @NotNull
    @Override
    public Listener getListener() {
        return new MorphSetEffectHandler();
    }

    @Override
    public @Unmodifiable Set<EquipmentSlot> applicableSlots() {
        return EnumSet.of(EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET);
    }

    @Override
    public int getMaxEffectiveLevel() {
        return 4;
    }

    @Override
    public String getDescription() {
        return "Provides various effects, depending on how many pieces are worn.";
    }

    @Nullable
    @Override
    public String getExtendedDescription(int level) {
        switch (level) {
            case 1:
                return "(1/4) ";
            case 2:
            case 3:
            case 4:
            default:
                return getDescription();
        }
    }

    class MorphSetEffectHandler implements Listener {

    }
}
