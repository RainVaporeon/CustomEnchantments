package io.github.rainvaporeon.customenchantments.util.event;

import io.github.rainvaporeon.customenchantments.util.SetCollection;
import io.github.rainvaporeon.customenchantments.util.infusions.InfusionInfo;
import io.github.rainvaporeon.customenchantments.util.infusions.InfusionUtils;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
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
    @EventHandler(priority = EventPriority.HIGH)
    public void onPrepareAnvil(PrepareAnvilEvent event) {
        AnvilInventory inventory = event.getInventory();
        ItemStack left = inventory.getFirstItem();
        ItemStack right = inventory.getSecondItem();
        ItemStack delegateResult = inventory.getResult();
        if (left == null || left.isEmpty()) return;
        if (right == null || right.isEmpty()) return;
        if (delegateResult == null || delegateResult.isEmpty()) {
            // must copy here to prevent LHS from being modified
            delegateResult = left.clone();
        }
        ItemStack result = delegateResult;

        Set<InfusionInfo> presentLeft = InfusionUtils.getAllInfusions(left);
        Set<InfusionInfo> presentRight = InfusionUtils.getAllInfusions(right);

        // Note: If the result stores nothing, we apply the stored infusions
        Set<InfusionInfo> storedLeft = InfusionUtils.getAllStoredInfusions(left);
        Set<InfusionInfo> storedRight = InfusionUtils.getAllStoredInfusions(right);

        // nothing has infusion, then it's not our business
        if (presentRight.isEmpty() && storedRight.isEmpty()) return;

        // We start managing the case where we apply RHS infusions and stored ones to result
        if (left.getType() == Material.ENCHANTED_BOOK && right.getType() == Material.ENCHANTED_BOOK) {
            // target is book, maybe we are doing book operations (merging)
            storedRight.forEach(info -> {
                InfusionInfo presentInfo = SetCollection.find(storedLeft, info);
                SetCollection.addForced(storedLeft, merge(info, presentInfo));
            });
            // As we are storing something, we put LHS over in stored infusions
            storedLeft.forEach(info -> InfusionUtils.applyStoredInfusion(result, info.getInfusion().getIdentifier(), info.getLevel()));
        } else {
            // different type and not appending book, exit
            if (left.getType() != right.getType() && right.getType() != Material.ENCHANTED_BOOK) return;
            // first, ignore incompatible ones
            storedRight.removeIf(info -> info.getInfusion().infusionTarget().stream().noneMatch(target -> target.includes(left)));
            presentRight.removeIf(info -> info.getInfusion().infusionTarget().stream().noneMatch(target -> target.includes(left)));
            // well, everything's excluded, seeya then
            if (storedRight.isEmpty() && presentRight.isEmpty()) return;
            // then, append stored infusions to LHS
            storedRight.forEach(info -> {
                InfusionInfo presentInfo = SetCollection.find(presentLeft, info);
                SetCollection.addForced(presentLeft, merge(info, presentInfo));
            });
        }

        // Mutual part: Merge right with left and map to left
        presentRight.forEach(info -> {
            InfusionInfo presentInfo = SetCollection.find(presentLeft, info);
            SetCollection.addForced(presentLeft, merge(info, presentInfo));
        });
        // and then we apply
        presentLeft.forEach(info -> InfusionUtils.applyInfusion(result, info.getInfusion().getIdentifier(), info.getLevel()));

        if (left.equals(result)) return;

        event.getInventory().setRepairCost(Math.max(event.getInventory().getRepairCost(), 1));
        event.setResult(result);
    }

    private static InfusionInfo merge(InfusionInfo left, InfusionInfo right) {
        if (left == null && right == null) return null;
        if (left == null) return right;
        if (right == null) return left;
        if (!left.getInfusion().getIdentifier().equals(right.getInfusion().getIdentifier())) {
            return null;
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
