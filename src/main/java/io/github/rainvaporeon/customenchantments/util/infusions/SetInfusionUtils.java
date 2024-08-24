package io.github.rainvaporeon.customenchantments.util.infusions;

import io.github.rainvaporeon.customenchantments.enchant.Infusion;
import io.github.rainvaporeon.customenchantments.enchant.SetInfusion;
import io.github.rainvaporeon.customenchantments.util.SetCollection;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Unmodifiable;

import java.util.*;

// TODO Implement some other methods seen in InfusionUtils
//  as set infusions are slightly more tricky to work with
//  under the current code design.
public final class SetInfusionUtils {
    /**
     * Retrieves the total active set bonus for this player
     * @param player the player
     * @return the set bonuses
     */
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

    /**
     * Counts the total active set pieces for this infusion on this player
     * @param player the player
     * @param infusion the set infusion type
     * @return the numbers of pieces worn that has this infusion set
     */
    public static int countActiveSetPieces(Player player, SetInfusion infusion) {
        return InfusionUtils.countInfusionPieces(player, infusion);
    }

    /**
     * Accumulates the set bonus of this infusion on this player
     * @param player the player
     * @param type the infusion type interested
     * @return the level of infusions this player's set bonus provided
     */
    public static int accumulateSetBonusOf(Player player, Infusion type) {
        int level = 0;
        for (Infusion infusion : InfusionManager.getInfusions()) {
            if (!infusion.isSet()) continue;
            SetInfusion set = (SetInfusion) infusion;
            int count = SetInfusionUtils.countActiveSetPieces(player, set);
            if (count == 0) continue;
            level += set.getInfusionBonus(type, count);
        }
        return level;
    }

    public static Map<SetInfusion, @Unmodifiable Set<InfusionInfo>> getIndividualSetBonus(Player player) {
        Map<SetInfusion, Set<InfusionInfo>> ret = new HashMap<>();
        for (SetInfusion set : InfusionManager.getSetInfusions()) {
            int level = countActiveSetPieces(player, set);
            if (level == 0) continue;
            ret.put(set, set.getInfusionBonuses(level));
        }
        return ret;
    }
}
