package io.github.rainvaporeon.customenchantments.util.enums;

import io.github.rainvaporeon.customenchantments.CustomEnchantments;
import org.bukkit.Material;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.function.Predicate;
import java.util.logging.Level;

/**
 * Simple wrapper to {@link EnchantmentTarget} to provide more
 * control over what can and cannot be infused.
 */
public enum InfusionTarget {

    ALL {
        @Override
        public boolean includes(@NotNull Material item) {
            return true;
        }
    },

    /**
     * Allows the Enchantment to be placed on armor
     */
    ARMOR {
        @Override
        public boolean includes(@NotNull Material item) {
            return ARMOR_FEET.includes(item)
                    || ARMOR_LEGS.includes(item)
                    || ARMOR_HEAD.includes(item)
                    || ARMOR_TORSO.includes(item);
        }
    },

    /**
     * Allows the Enchantment to be placed on feet slot armor
     */
    ARMOR_FEET {
        @Override
        public boolean includes(@NotNull Material item) {
            return item.equals(Material.LEATHER_BOOTS)
                    || item.equals(Material.CHAINMAIL_BOOTS)
                    || item.equals(Material.IRON_BOOTS)
                    || item.equals(Material.DIAMOND_BOOTS)
                    || item.equals(Material.GOLDEN_BOOTS)
                    || item.equals(Material.NETHERITE_BOOTS);
        }
    },

    /**
     * Allows the Enchantment to be placed on leg slot armor
     */
    ARMOR_LEGS {
        @Override
        public boolean includes(@NotNull Material item) {
            return item.equals(Material.LEATHER_LEGGINGS)
                    || item.equals(Material.CHAINMAIL_LEGGINGS)
                    || item.equals(Material.IRON_LEGGINGS)
                    || item.equals(Material.DIAMOND_LEGGINGS)
                    || item.equals(Material.GOLDEN_LEGGINGS)
                    || item.equals(Material.NETHERITE_LEGGINGS);
        }
    },

    /**
     * Allows the Enchantment to be placed on torso slot armor
     */
    ARMOR_TORSO {
        @Override
        public boolean includes(@NotNull Material item) {
            return item.equals(Material.LEATHER_CHESTPLATE)
                    || item.equals(Material.CHAINMAIL_CHESTPLATE)
                    || item.equals(Material.IRON_CHESTPLATE)
                    || item.equals(Material.DIAMOND_CHESTPLATE)
                    || item.equals(Material.GOLDEN_CHESTPLATE)
                    || item.equals(Material.NETHERITE_CHESTPLATE);
        }
    },

    /**
     * Allows the Enchantment to be placed on head slot armor
     */
    ARMOR_HEAD {
        @Override
        public boolean includes(@NotNull Material item) {
            return item.equals(Material.LEATHER_HELMET)
                    || item.equals(Material.CHAINMAIL_HELMET)
                    || item.equals(Material.DIAMOND_HELMET)
                    || item.equals(Material.IRON_HELMET)
                    || item.equals(Material.GOLDEN_HELMET)
                    || item.equals(Material.TURTLE_HELMET)
                    || item.equals(Material.NETHERITE_HELMET);
        }
    },

    /**
     * Allows the Enchantment to be placed on weapons (swords)
     */
    WEAPON {
        @Override
        public boolean includes(@NotNull Material item) {
            return item.equals(Material.WOODEN_SWORD)
                    || item.equals(Material.STONE_SWORD)
                    || item.equals(Material.IRON_SWORD)
                    || item.equals(Material.DIAMOND_SWORD)
                    || item.equals(Material.GOLDEN_SWORD)
                    || item.equals(Material.NETHERITE_SWORD);
        }
    },

    /**
     * Allows the Enchantment to be placed on tools (spades, pickaxe, axes)
     */
    TOOL {
        @Override
        public boolean includes(@NotNull Material item) {
            return item.equals(Material.WOODEN_SHOVEL)
                    || item.equals(Material.STONE_SHOVEL)
                    || item.equals(Material.IRON_SHOVEL)
                    || item.equals(Material.DIAMOND_SHOVEL)
                    || item.equals(Material.GOLDEN_SHOVEL)
                    || item.equals(Material.NETHERITE_SHOVEL)
                    || item.equals(Material.WOODEN_PICKAXE)
                    || item.equals(Material.STONE_PICKAXE)
                    || item.equals(Material.IRON_PICKAXE)
                    || item.equals(Material.DIAMOND_PICKAXE)
                    || item.equals(Material.GOLDEN_PICKAXE)
                    || item.equals(Material.NETHERITE_PICKAXE)
                    || item.equals(Material.WOODEN_AXE)
                    || item.equals(Material.STONE_AXE)
                    || item.equals(Material.IRON_AXE)
                    || item.equals(Material.DIAMOND_AXE)
                    || item.equals(Material.GOLDEN_AXE)
                    || item.equals(Material.NETHERITE_AXE)
                    || item.equals(Material.WOODEN_HOE)
                    || item.equals(Material.STONE_HOE)
                    || item.equals(Material.IRON_HOE)
                    || item.equals(Material.DIAMOND_HOE)
                    || item.equals(Material.GOLDEN_HOE)
                    || item.equals(Material.NETHERITE_HOE);
        }
    },

    AXE {
        @Override
        public boolean includes(Material item) {
            return item.equals(Material.WOODEN_AXE)
                    || item.equals(Material.STONE_AXE)
                    || item.equals(Material.IRON_AXE)
                    || item.equals(Material.DIAMOND_AXE)
                    || item.equals(Material.GOLDEN_AXE)
                    || item.equals(Material.NETHERITE_AXE);
        }
    },

    /**
     * Allows the Enchantment to be placed on bows.
     */
    BOW {
        @Override
        public boolean includes(@NotNull Material item) {
            return item.equals(Material.BOW);
        }
    },

    /**
     * Allows the Enchantment to be placed on fishing rods.
     */
    FISHING_ROD {
        @Override
        public boolean includes(@NotNull Material item) {
            return item.equals(Material.FISHING_ROD);
        }
    },

    /**
     * Allows the enchantment to be placed on items with durability.
     */
    BREAKABLE {
        @Override
        public boolean includes(@NotNull Material item) {
            return item.getMaxDurability() > 0 && item.getMaxStackSize() == 1;
        }
    },

    /**
     * Allows the enchantment to be placed on wearable items.
     */
    WEARABLE {
        @Override
        public boolean includes(@NotNull Material item) {
            return ARMOR.includes(item)
                    || item.equals(Material.ELYTRA)
                    || item.equals(Material.CARVED_PUMPKIN)
                    || item.equals(Material.SKELETON_SKULL)
                    || item.equals(Material.WITHER_SKELETON_SKULL)
                    || item.equals(Material.ZOMBIE_HEAD)
                    || item.equals(Material.PIGLIN_HEAD)
                    || item.equals(Material.PLAYER_HEAD)
                    || item.equals(Material.CREEPER_HEAD)
                    || item.equals(Material.DRAGON_HEAD)
                    || item.equals(Material.SHIELD);
        }
    },

    /**
     * Allow the Enchantment to be placed on tridents.
     */
    TRIDENT {
        @Override
        public boolean includes(@NotNull Material item) {
            return item.equals(Material.TRIDENT);
        }
    },

    /**
     * Allow the Enchantment to be placed on crossbows.
     */
    CROSSBOW {
        @Override
        public boolean includes(@NotNull Material item) {
            return item.equals(Material.CROSSBOW);
        }
    },

    THROWABLE {
        @Override
        public boolean includes(Material item) {
            return item.equals(Material.SNOWBALL) ||
                    item.equals(Material.ENDER_PEARL);
        }
    },

    /**
     * Allow the Enchantment to be placed on vanishing items.
     */
    VANISHABLE {
        @Override
        public boolean includes(@NotNull Material item) {
            return BREAKABLE.includes(item) || (WEARABLE.includes(item) && !item.equals(Material.ELYTRA)) || item.equals(Material.COMPASS);
        }
    };

    private final @Nullable EnchantmentTarget target;
    private final @Nullable Predicate<Material> materialPredicate;

    InfusionTarget(@Nullable EnchantmentTarget target, @Nullable Predicate<Material> materialPredicate) {
        this.target = target;
        this.materialPredicate = materialPredicate;
    }

    InfusionTarget(EnchantmentTarget target) {
        this(target, null);
    }

    InfusionTarget(Predicate<Material> predicate) {
        this(null, predicate);
    }

    InfusionTarget() {
        this(null, null);
    }

    public boolean includes(ItemStack stack) {
        return includes(stack.getType());
    }

    public boolean includes(Material material) {
        if (target != null) return target.includes(material);
        if (materialPredicate != null) return materialPredicate.test(material);
        CustomEnchantments.PLUGIN.getLogger().log(Level.WARNING, "Potential bug with InfusionTarget " + this + ": No predicate found");
        return true;
    }
}
