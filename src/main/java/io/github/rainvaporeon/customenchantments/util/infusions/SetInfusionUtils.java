package io.github.rainvaporeon.customenchantments.util.infusions;

import io.github.rainvaporeon.customenchantments.enchant.Infusion;
import io.github.rainvaporeon.customenchantments.enchant.SetInfusion;
import io.github.rainvaporeon.customenchantments.util.SetCollection;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

// TODO Implement some other methods seen in InfusionUtils
//  as set infusions are slightly more tricky to work with
//  under the current code design.
public final class SetInfusionUtils {
    public static Set<InfusionInfo> getActiveSetBonuses(Player player) {
        List<Set<InfusionInfo>> accumulator = new ArrayList<>();
        for (Infusion infusion : InfusionManager.getInfusions()) {
            if (!infusion.isSet()) continue;
            SetInfusion set = (SetInfusion) infusion;
            int count = SetInfusionUtils.countActiveSetPieces(player, set);
            if (count == 0) continue;
            accumulator.add(set.getInfusionBonuses(count));
        }
        return SetCollection.mergeInfo(accumulator);
    }

    public static int countActiveSetPieces(Player player, SetInfusion infusion) {
        return InfusionUtils.countInfusionPieces(player, infusion);
    }
}
