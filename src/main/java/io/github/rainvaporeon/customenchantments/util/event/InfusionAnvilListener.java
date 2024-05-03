package io.github.rainvaporeon.customenchantments.util.event;

import io.github.rainvaporeon.customenchantments.util.SetCollection;
import io.github.rainvaporeon.customenchantments.util.infusions.InfusionInfo;
import io.github.rainvaporeon.customenchantments.util.infusions.InfusionUtils;
import io.github.rainvaporeon.customenchantments.util.server.Server;
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
        if (presentRight.isEmpty() && presentLeft.isEmpty() && storedRight.isEmpty() && storedLeft.isEmpty()) return;

        // We start managing the case where we apply RHS infusions and stored ones to result
        if (storedLeft.isEmpty()) {
            // first, ignore incompatible ones
            storedRight.removeIf(info -> {
                return info.getInfusion().infusionTarget().stream().noneMatch(target -> target.includes(result));
            });
            presentRight.removeIf(info -> {
                return info.getInfusion().infusionTarget().stream().noneMatch(target -> target.includes(result));
            });
            // then, append stored infusions to LHS
            storedRight.forEach(info -> {
                InfusionInfo presentInfo = SetCollection.find(presentLeft, info);
                SetCollection.addForced(presentLeft, merge(info, presentInfo));
            });
        } else {
            // Result stores something, may be merging books then
            // in that case we filter nothing at all (probably will change for conflicting infusions)
            storedRight.forEach(info -> {
                InfusionInfo presentInfo = SetCollection.find(storedLeft, info);
                SetCollection.addForced(storedLeft, merge(info, presentInfo));
            });
            // As we are storing something, we put LHS over in stored infusions
            storedLeft.forEach(info -> {
                InfusionUtils.applyStoredInfusion(result, info.getInfusion().getIdentifier(), info.getLevel());
            });
        }

        // Mutual part: Merge right with left and map to left
        presentRight.forEach(info -> {
            InfusionInfo presentInfo = SetCollection.find(presentLeft, info);
            SetCollection.addForced(presentLeft, merge(info, presentInfo));
        });
        // and then we apply
        presentLeft.forEach(info -> {
            InfusionUtils.applyInfusion(result, info.getInfusion().getIdentifier(), info.getLevel());
        });
        event.getInventory().setRepairCost(Math.max(event.getInventory().getRepairCost(), 1));
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
        Server.log("merging infusion " + left + " and " + right + ":");
        Server.log("LVLL=" + leftLevel + "; LVLR=" + rightLevel + "; CAP=" + levelCap);

        if (leftLevel == rightLevel) {
            Server.log("Equality: Evaluating " + Math.min(levelCap, leftLevel + 1));
            // Increase a level, but does not go over the established cap
            return new InfusionInfo(left.getInfusion(), Math.min(levelCap, leftLevel + 1));
        } else {
            Server.log("Different: Evaluating " + Math.max(leftLevel, rightLevel));
            // Retain level, keep overleveled infusion.
            return new InfusionInfo(left.getInfusion(), Math.max(leftLevel, rightLevel));
        }
    }
}
