package io.github.rainvaporeon.customenchantments.util.event;

import io.github.rainvaporeon.customenchantments.util.SetCollection;
import io.github.rainvaporeon.customenchantments.util.infusions.InfusionInfo;
import io.github.rainvaporeon.customenchantments.util.infusions.InfusionUtils;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.inventory.AnvilInventory;
import org.bukkit.inventory.ItemStack;

import java.util.Set;

public class InfusionAnvilListener implements Listener {

    private static final InfusionAnvilListener instance = new InfusionAnvilListener();
    private InfusionAnvilListener() {}

    public static InfusionAnvilListener getInstance() {
        return instance;
    }

    /*
     * Note: As left automatically carries over to result,
     * we do not really need to look at LHS at all.
     */
    @EventHandler
    public void onPrepareAnvil(PrepareAnvilEvent event) {
        AnvilInventory inventory = event.getInventory();
        ItemStack left = inventory.getFirstItem();
        ItemStack right = inventory.getSecondItem();
        ItemStack result = inventory.getResult();
        if (left == null || left.isEmpty()) return;
        if (right == null || right.isEmpty()) return;
        if (result == null || result.isEmpty()) return;

        Set<InfusionInfo> presentRight = InfusionUtils.getAllInfusions(right);
        Set<InfusionInfo> presentResult = InfusionUtils.getAllInfusions(result);

        // Note: If the result stores nothing, we apply the stored infusions
        Set<InfusionInfo> storedRight = InfusionUtils.getAllStoredInfusions(right);
        Set<InfusionInfo> storedResult = InfusionUtils.getAllStoredInfusions(result);

        presentRight.removeIf(info -> {
            return info.getInfusion().infusionTarget().stream().anyMatch(target -> target.includes(result));
        });

        // nothing has infusion, then it's not our business
        if (presentRight.isEmpty() && presentResult.isEmpty()) return;

        // We start managing the case where we apply RHS infusions and stored ones to result
        if (storedResult.isEmpty()) {
            // first ignore incompatible ones
            storedRight.removeIf(info -> {
                return info.getInfusion().infusionTarget().stream().anyMatch(target -> target.includes(result));
            });
            storedRight.forEach(info -> {
                InfusionInfo presentInfo = SetCollection.find(presentResult, info);
                presentResult.add(merge(info, presentInfo));
            });
        } else {
            // Result stores something, may be merging books then
            storedRight.forEach(info -> {
                InfusionInfo presentInfo = SetCollection.find(storedResult, info);
                storedResult.add(merge(info, presentInfo));
            });
        }
        // Mutual part
        presentRight.forEach(info -> {
            InfusionInfo presentInfo = SetCollection.find(presentResult, info);
            presentResult.add(merge(info, presentInfo));
        });

        presentResult.forEach(info -> {
            InfusionUtils.applyInfusion(result, info.getInfusion().getIdentifier(), info.getLevel());
        });
        storedResult.forEach(info -> {
            InfusionUtils.applyStoredInfusion(result, info.getInfusion().getIdentifier(), info.getLevel());
        });
        event.setResult(result);
    }

    private static InfusionInfo merge(InfusionInfo left, InfusionInfo right) {
        if (left == null && right == null) throw new NullPointerException();
        if (left == null) return right;
        if (right == null) return left;
        if (!left.getInfusion().getIdentifier().equals(right.getInfusion().getIdentifier())) {
            throw new RuntimeException("unexpected merge " + left + " and " + right);
        }
        int leftLevel = left.getLevel();
        int rightLevel = right.getLevel();
        // As we've already established left == right, no need to bother
        int levelCap = left.getInfusion().getMaxLevel();

        if (leftLevel == rightLevel) {
            // Increase a level, but does not go over the established cap
            return new InfusionInfo(left.getInfusion(), Math.min(levelCap, leftLevel + 1));
        } else {
            // Retain level, keep overleveled infusion.
            return new InfusionInfo(left.getInfusion(), Math.max(leftLevel, rightLevel));
        }
    }
}
