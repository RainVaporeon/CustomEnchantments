package io.github.rainvaporeon.customenchantments.util.event;

import io.github.rainvaporeon.customenchantments.CustomEnchantments;
import io.github.rainvaporeon.customenchantments.util.SetCollection;
import io.github.rainvaporeon.customenchantments.util.infusions.InfusionInfo;
import io.github.rainvaporeon.customenchantments.util.infusions.InfusionLoreUtils;
import io.github.rainvaporeon.customenchantments.util.infusions.InfusionUtils;
import io.github.rainvaporeon.customenchantments.util.io.LocalConfig;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.inventory.AnvilInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;

import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;
import java.util.logging.Level;

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
        boolean shouldDebug = LocalConfig.instance().readBoolean(LocalConfig.DEBUG_LOGGING, false);
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
        final AtomicReference<ItemStack> result = new AtomicReference<>(delegateResult);

        Set<InfusionInfo> presentLeft = InfusionUtils.getAllInfusions(left);
        Set<InfusionInfo> presentRight = InfusionUtils.getAllInfusions(right);

        // Note: If the result stores nothing, we apply the stored infusions
        Set<InfusionInfo> storedLeft = InfusionUtils.getAllStoredInfusions(left);
        Set<InfusionInfo> storedRight = InfusionUtils.getAllStoredInfusions(right);

        if (shouldDebug) {
            CustomEnchantments.PLUGIN.getLogger().log(Level.INFO,
                    "P1 >> pL, pR, sL, sR = " + presentLeft + ", " + presentRight + ", " + storedLeft + ", " + storedRight);
        }
        // nothing has infusion, then it's not our business
        if (presentRight.isEmpty() && storedRight.isEmpty()) {
            if (shouldDebug) {
                CustomEnchantments.PLUGIN.getLogger().log(Level.INFO, "QUIT:PR/R E");
            }
            return;
        }
        // We start managing the case where we apply RHS infusions and stored ones to result
        if ((left.getType() == Material.ENCHANTED_BOOK && right.getType() == Material.ENCHANTED_BOOK)
                || (left.getType() == Material.BOOK && right.getType() == Material.BOOK)) {
            // target is book, maybe we are doing book operations (merging)
            if (shouldDebug) {
                CustomEnchantments.PLUGIN.getLogger().log(Level.INFO, "TYPE BOOK");
            }
            storedRight.forEach(info -> {
                InfusionInfo presentInfo = SetCollection.find(storedLeft, info);
                SetCollection.addForced(storedLeft, merge(info, presentInfo));
            });
            // As we are storing something, we put LHS over in stored infusions
            storedLeft.forEach(info -> InfusionUtils.applyStoredInfusion(result.get(), info.getInfusion().getIdentifier(), info.getLevel()));
        } else {
            if (shouldDebug) {
                CustomEnchantments.PLUGIN.getLogger().log(Level.INFO, "TYPE NORMAL");
            }
            // different type and not appending from book, exit
            if (left.getType() != right.getType() && right.getType() != Material.ENCHANTED_BOOK && right.getType() != Material.BOOK) {
                if (shouldDebug) {
                    CustomEnchantments.PLUGIN.getLogger().log(Level.INFO, "DIFF, NO BOOK, EXIT");
                }
                return;
            }
            // if RHS is enchantment storage, do logic here
            if (!InfusionUtils.getAllStoredInfusions(right).isEmpty()) {
                // merging with a book, run storage logic
                storedRight.removeIf(info -> info.getInfusion().infusionTarget().stream().noneMatch(target -> target.includes(left)));
                storedRight.forEach(info -> {
                    InfusionInfo presentInfo = SetCollection.find(presentLeft, info);
                    SetCollection.addForced(presentLeft, merge(info, presentInfo));
                });
            } else if (left.getType() != right.getType()) {
                presentRight.clear(); // not supposed to carry over on normal enchantments
            }
            // first, ignore incompatible ones
            presentRight.removeIf(info -> info.getInfusion().infusionTarget().stream().noneMatch(target -> target.includes(left)));
            // well, everything's excluded, see ya then
            if (storedRight.isEmpty() && presentRight.isEmpty()) return;
        }
        if (shouldDebug) {
            CustomEnchantments.PLUGIN.getLogger().log(Level.INFO,
                    "P2 >> pL, pR, sL, sR = " + presentLeft + ", " + presentRight + ", " + storedLeft + ", " + storedRight);
        }

        // Mutual part: Merge right with left and map to left
        presentRight.forEach(info -> {
            InfusionInfo presentInfo = SetCollection.find(presentLeft, info);
            SetCollection.addForced(presentLeft, merge(info, presentInfo));
        });
        // and then we apply
        presentLeft.forEach(info -> InfusionUtils.applyInfusion(result.get(), info.getInfusion().getIdentifier(), info.getLevel()));

        if (shouldDebug) {
            CustomEnchantments.PLUGIN.getLogger().log(Level.INFO,
                    "P3 >> pL, pR, sL, sR = " + presentLeft + ", " + presentRight + ", " + storedLeft + ", " + storedRight);
        }

        if (left.equals(result.get())) {
            if (shouldDebug) {
                CustomEnchantments.PLUGIN.getLogger().log(Level.INFO, "L=RES, EXIT");
            }
            return;
        }

        if (shouldDebug) {
            CustomEnchantments.PLUGIN.getLogger().log(Level.INFO, "PASS, ITEM=" + result.get());
        }

        event.getInventory().setRepairCost(Math.max(0, event.getInventory().getRepairCost()));
        event.setResult(InfusionLoreUtils.applySortedLoreNBT(result.get()));
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
