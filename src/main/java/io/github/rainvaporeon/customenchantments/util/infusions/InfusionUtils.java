package io.github.rainvaporeon.customenchantments.util.infusions;

import de.tr7zw.nbtapi.NBT;
import de.tr7zw.nbtapi.iface.ReadWriteNBT;
import de.tr7zw.nbtapi.iface.ReadableNBTList;
import io.github.rainvaporeon.customenchantments.enchant.Infusion;
import io.github.rainvaporeon.customenchantments.util.PlayerInventoryUtils;
import io.github.rainvaporeon.customenchantments.util.SetCollection;
import io.github.rainvaporeon.customenchantments.util.SharedConstants;
import io.github.rainvaporeon.customenchantments.util.enums.InfusionTarget;
import io.github.rainvaporeon.customenchantments.util.enums.Result;
import io.github.rainvaporeon.customenchantments.util.internal.accessors.CESecrets;
import io.github.rainvaporeon.customenchantments.util.io.LocalConfig;
import org.bukkit.entity.Player;
import org.bukkit.inventory.*;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public final class InfusionUtils {
    /**
     * Applies the infusion, changing the lore to adapt
     * @param stack the item stack to apply to
     * @param identifier the infusion identifier
     * @param level the level
     */
    public static boolean applyInfusion(ItemStack stack, String identifier, int level) {
        if (stack == null || stack.isEmpty()) return false;
        Infusion infusion = InfusionManager.getInfusionById(identifier);
        if (infusion == null) return false;
        if (InfusionUtils.getInfusion(stack, infusion) > 0) removeInfusionData(stack, identifier);
        NBT.modify(stack, nbt -> {
            nbt.modifyMeta((readonly, meta) -> {
                InfusionLoreUtils.removeLoreNBT(meta, infusion);
                InfusionLoreUtils.applySortedLoreNBT(stack, meta);
                stack.setItemMeta(meta);
            });
            nbt.getCompoundList(SharedConstants.INFUSION_IDENTIFIER_KEY).addCompound().mergeCompound(infusion.getNBT(level));
        });
        return true;
    }

    /**
     * Removes the infusion, changing the lore to adapt
     * @param stack the item stack to remove
     * @param identifier the infusion identifier
     */
    public static boolean removeInfusion(ItemStack stack, String identifier) {
        if (stack == null || stack.isEmpty()) return false;
        Infusion infusion = InfusionManager.getInfusionById(identifier);
        if (infusion == null) return false;
        // InfusionLoreUtils.removeLoreNBT(item, infusion);
        removeInfusionData(stack, identifier);
        // item.applyNBT(stack);
        InfusionLoreUtils.applySortedLoreNBT(stack);
        return true;
    }

    /**
     * Removes all infusions from this item
     * @param stack the stack
     * @return {@code true} if stack is not null nor empty
     */
    public static boolean removeAllInfusions(ItemStack stack) {
        if (stack == null || stack.isEmpty()) return false;
        InfusionLoreUtils.applySortedLoreNBT(stack);
        NBT.modify(stack, nbt -> {
            nbt.removeKey(SharedConstants.INFUSION_IDENTIFIER_KEY);
        });
        return true;
    }

    /**
     * Removes the infusion data from this item
     * @param item the item
     * @param identifier the infusion identifier
     */
    private static void removeInfusionData(ItemStack item, String identifier) {
        NBT.modify(item, nbt -> {
            nbt.getCompoundList(SharedConstants.INFUSION_IDENTIFIER_KEY)
                    .removeIf(rw -> rw.getString(SharedConstants.INFUSION_ID).equals(identifier));
        });
    }

    /**
     * Applies these stored infusions to the item
     * @param stack the stack
     * @param identifier the infusion identifier
     * @param level the level
     */
    public static boolean applyStoredInfusion(ItemStack stack, String identifier, int level) {
        if (stack == null || stack.isEmpty()) return false;
        Infusion infusion = InfusionManager.getInfusionById(identifier);
        if (infusion == null) return false;
        NBT.modify(stack, nbt -> {
            removeStoredInfusionData(nbt, identifier);
            nbt.modifyMeta((readonly, meta) -> {
                InfusionLoreUtils.removeLoreNBT(meta, infusion);
                InfusionLoreUtils.applySortedLoreNBT(stack, meta);
                stack.setItemMeta(meta);
            });
            nbt.getCompoundList(SharedConstants.STORED_INFUSION_IDENTIFIER_KEY).addCompound().mergeCompound(infusion.getNBT(level));
        });
        return true;
    }

    /**
     * Removes the stored infusion from this item stack
     * @param stack the item stack to remove stored infusion from
     * @param identifier the infusion identifier
     * @return whether this operation succeeds
     */
    public static boolean removeStoredInfusion(ItemStack stack, String identifier) {
        if (stack == null || stack.isEmpty()) return false;
        Infusion infusion = InfusionManager.getInfusionById(identifier);
        if (infusion == null) return false;
        NBT.modify(stack, nbt -> {
            removeStoredInfusionData(nbt, identifier);
        });
        InfusionLoreUtils.applySortedLoreNBT(stack);
        return true;
    }

    /**
     * Removes all stored infusions on this stack
     * @param stack the item stack to remove from
     * @return {@code true} if stack is not null nor empty
     */
    public static boolean removeAllStoredInfusions(ItemStack stack) {
        if (stack == null || stack.isEmpty()) return false;
        NBT.modify(stack, nbt -> {
            nbt.removeKey(SharedConstants.STORED_INFUSION_IDENTIFIER_KEY);
        });
        return true;
    }

    /**
     * Removes the stored infusion data from this NBT
     * @param nbt the nbt
     * @param identifier the identifier
     * @apiNote this should only be used on NBT supplied from {@link NBT#modify(ItemStack, Consumer)}
     */
    private static void removeStoredInfusionData(ReadWriteNBT nbt, String identifier) {
        nbt.getCompoundList(SharedConstants.STORED_INFUSION_IDENTIFIER_KEY)
                .removeIf(rw -> rw.getString(SharedConstants.INFUSION_ID).equals(identifier));
    }

    /**
     * Accumulates the total active infusion level for this infusion on the player
     * @param entity the player
     * @param infusion the infusion
     * @return the total level active on this infusion on the player
     */
    public static int accumulateInfusionLevelOf(Player entity, Infusion infusion) {
        return accumulateInfusionLevelOf(entity, infusion, LocalConfig.instance().readBoolean("strict", false));
    }

    /**
     * @see InfusionUtils#accumulateInfusionLevelOf(Player, Infusion)
     */
    public static int accumulateInfusionLevelOf(Player entity, Infusion infusion, boolean strict) {
        if (infusion == null) return 0;
        PlayerInventory inventory = entity.getInventory();
        if (strict) {
            Set<InfusionTarget> targetSet = infusion.infusionTarget();
            Set<EquipmentSlot> allowedSlots = infusion.applicableSlots();
            List<ItemStack> applicableList = PlayerInventoryUtils.collectFromSlot(inventory, allowedSlots);
            applicableList.removeIf(item -> targetSet.stream().noneMatch(e -> e.includes(item)));
            applicableList.removeIf(item -> item == null || item.isEmpty());
            return applicableList.stream().mapToInt(is -> InfusionUtils.getInfusion(is, infusion)).sum();
        } else {
            return SetCollection.findOrSelf(getAllActiveInfusions(entity), InfusionInfo.Cache.get(infusion)).getLevel();
        }
    }

    /**
     * Counts the player's equipped pieces containing this infusion
     * @param player the player
     * @param infusion the infusion
     * @return the total amounts of pieces having this infusion
     */
    public static int countInfusionPieces(Player player, Infusion infusion) {
        return countInfusionPieces(player, infusion, LocalConfig.instance().readBoolean("strict", false));
    }

    /**
     * @see InfusionUtils#countInfusionPieces(Player, Infusion)
     */
    public static int countInfusionPieces(Player player, Infusion infusion, boolean strict) {
        if (infusion == null) return 0;
        PlayerInventory inventory = player.getInventory();
        if (strict) {
            Set<InfusionTarget> targetSet = infusion.infusionTarget();
            Set<EquipmentSlot> allowedSlots = infusion.applicableSlots();
            List<ItemStack> applicableList = PlayerInventoryUtils.collectFromSlot(inventory, allowedSlots);
            applicableList.removeIf(item -> targetSet.stream().noneMatch(e -> e.includes(item)));
            applicableList.removeIf(item -> item == null || item.isEmpty());
            return (int) applicableList.stream().filter(is -> InfusionUtils.getInfusion(is, infusion) != 0).count();
        } else {
            return (int) PlayerInventoryUtils.collectFromSlot(inventory, SharedConstants.equipmentSlots())
                    .stream()
                    .filter(is -> InfusionUtils.getInfusion(is, infusion) != 0)
                    .count();
        }
    }

    public static Set<InfusionInfo> getAllInfusions(ItemStack stack) {
        return getAllInfusions(stack, LocalConfig.instance().readBoolean("strict", false));
    }

    public static Set<InfusionInfo> getAllInfusions(ItemStack stack, boolean strict) {
        if (stack == null || stack.isEmpty()) return Collections.emptySet();
        return NBT.get(stack, nbt -> {
            ReadableNBTList<ReadWriteNBT> list = nbt.getCompoundList(SharedConstants.INFUSION_IDENTIFIER_KEY);
            Set<InfusionInfo> infusionMap = new HashSet<>();
            list.forEach(rw -> {
                String identifier = rw.getString(SharedConstants.INFUSION_ID);
                Integer level = rw.getInteger(SharedConstants.INFUSION_LEVEL);
                Infusion infusion = InfusionManager.getInfusionById(identifier);
                if (identifier == null || level == null || infusion == null) return;
                if (strict && infusion.infusionTarget().stream().noneMatch(target -> target.includes(stack))) return;
                infusionMap.add(new InfusionInfo(infusion, level));
            });
            return infusionMap;
        });
    }

    /**
     * Fetches all active infusions on a player, the results are folded into
     * one set containing all infusions that are active on the player.
     * @param player the player
     * @return a mutable container containing all active infusions on the player
     * @apiNote the returned mutable container is not backed by the player.
     */
    // "One-liner" as they say...
    public static Set<InfusionInfo> getAllActiveInfusions(Player player) {
        return Stream.concat(
                        PlayerInventoryUtils
                                .collectFromSlot(player.getInventory(), SharedConstants.equipmentSlots())
                                .stream()
                                .map(InfusionUtils::getAllInfusions)
                                .flatMap(Collection::stream),
                        SetInfusionUtils
                                .getActiveSetBonuses(player)
                                .stream())
                .collect(
                        HashSet::new,
                        (set, info) -> {
                            if (!set.contains(info)) {
                                set.add(info);
                            } else {
                                SetCollection.addForced(set, info.combine(SetCollection.find(set, info, CESecrets.getInfusionCacheAccessor().get(info.getInfusion()))));
                            }
                        },
                        (left, right) -> {
                            right.forEach(info -> {
                                if (!left.contains(info)) {
                                    left.add(info);
                                } else {
                                    SetCollection.addForced(left, info.combine(SetCollection.find(left, info, CESecrets.getInfusionCacheAccessor().get(info.getInfusion()))));
                                }
                            });
                        }
                );
    }

    public static int getInfusion(ItemStack stack, Infusion type) {
        if (stack == null || stack.isEmpty()) return 0;
        return NBT.get(stack, nbt -> {
            ReadableNBTList<ReadWriteNBT> list = nbt.getCompoundList(SharedConstants.INFUSION_IDENTIFIER_KEY);
            return StreamSupport.stream(list.spliterator(), false)
                    .filter(rw -> rw.getString(SharedConstants.INFUSION_ID).equals(type.getIdentifier()))
                    .findAny().map(data -> data.getInteger(SharedConstants.INFUSION_LEVEL)).orElse(0);
        });
    }

    public static int getStoredInfusion(ItemStack stack, Infusion type) {
        if (stack == null || stack.isEmpty()) return 0;
        return NBT.get(stack, nbt -> {
            ReadableNBTList<ReadWriteNBT> list = nbt.getCompoundList(SharedConstants.STORED_INFUSION_IDENTIFIER_KEY);
            ReadWriteNBT data = StreamSupport.stream(list.spliterator(), false)
                    .filter(rw -> rw.getString(SharedConstants.INFUSION_ID).equals(type.getIdentifier()))
                    .findFirst().orElse(null);
            return data == null ? 0 : data.getInteger(SharedConstants.INFUSION_LEVEL);
        });
    }

    public static Set<InfusionInfo> getAllStoredInfusions(ItemStack stack) {
        if (stack == null || stack.isEmpty()) return Collections.emptySet();
        return NBT.get(stack, item -> {
            ReadableNBTList<ReadWriteNBT> list = item.getCompoundList(SharedConstants.STORED_INFUSION_IDENTIFIER_KEY);
            Set<InfusionInfo> infusionMap = new HashSet<>();
            list.forEach(rw -> {
                String identifier = rw.getString(SharedConstants.INFUSION_ID);
                Integer level = rw.getInteger(SharedConstants.INFUSION_LEVEL);
                if (identifier == null || level == null) return;
                infusionMap.add(new InfusionInfo(InfusionManager.getInfusionById(identifier), level));
            });
            return infusionMap;
        });
    }

    public static Result testInfusion(ItemStack stack, Infusion infusion) {
        if (stack == null || infusion == null) return Result.FAIL;
        if (infusion.infusionTarget().stream().anyMatch(target -> target.includes(stack))) return Result.SUCCESSFUL;
        return Result.INCOMPATIBLE_TARGET;
    }

    public static boolean hasInfusion(Player player, Infusion infusion) {
        return PlayerInventoryUtils.collectFromSlot(player.getInventory(), SharedConstants.equipmentSlots())
                .stream().anyMatch(is -> getInfusion(is, infusion) != 0);
    }
}
