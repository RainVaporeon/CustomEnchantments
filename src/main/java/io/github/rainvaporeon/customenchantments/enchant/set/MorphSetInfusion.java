package io.github.rainvaporeon.customenchantments.enchant.set;

import io.github.rainvaporeon.customenchantments.enchant.SetInfusion;
import io.github.rainvaporeon.customenchantments.enchant.buff.combat.KillStreakInfusion;
import io.github.rainvaporeon.customenchantments.enchant.buff.misc.AcrobaticsInfusion;
import io.github.rainvaporeon.customenchantments.enchant.buff.misc.GracefulLandingInfusion;
import io.github.rainvaporeon.customenchantments.enchant.buff.protection.DamageReductionInfusion;
import io.github.rainvaporeon.customenchantments.util.SetCollection;
import io.github.rainvaporeon.customenchantments.util.SharedConstants;
import io.github.rainvaporeon.customenchantments.util.infusions.InfusionInfo;
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
        return SharedConstants.emptyListener();
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
                return "(1/4) No effect.";
            case 2:
                return "(2/4) Gain Resistance I and Acrobatics I.";
            case 3:
                return "(3/4) Gain Resistance II and Graceful Landing I.";
            case 4:
                return "(4/4) Gain Resistance IV, Graceful Landing II and Kill Streak I.";
            default:
                return getDescription();
        }
    }

    private static final Set<InfusionInfo> LEVEL_2 = SetCollection.of(
            InfusionInfo.of(DamageReductionInfusion.class, 1),
            InfusionInfo.of(AcrobaticsInfusion.class, 1)
    );

    private static final Set<InfusionInfo> LEVEL_3 = SetCollection.of(
            InfusionInfo.of(DamageReductionInfusion.class, 2),
            InfusionInfo.of(GracefulLandingInfusion.class, 1)
    );

    private static final Set<InfusionInfo> LEVEL_4 = SetCollection.of(
            InfusionInfo.of(DamageReductionInfusion.class, 4),
            InfusionInfo.of(GracefulLandingInfusion.class, 2),
            InfusionInfo.of(KillStreakInfusion.class, 1)
    );

    @Override
    public Set<InfusionInfo> getInfusionBonuses(int level) {
        switch (level) {
            case 2:
                return LEVEL_2;
            case 3:
                return LEVEL_3;
            case 4:
                return LEVEL_4;
            default:
                return Collections.emptySet();
        }
    }
}
