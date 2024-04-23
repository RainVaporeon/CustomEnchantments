package io.github.rainvaporeon.customenchantments.commands;

import io.github.rainvaporeon.customenchantments.util.infusions.InfusionLoreUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class UpdateItemCommand extends BaseCommand {
    UpdateItemCommand() { super("updateitem"); }

    @Override
    public boolean execute(@NotNull CommandSender commandSender, @NotNull String s, @NotNull String[] strings) {
        if (!(commandSender instanceof Player)) {
            commandSender.sendMessage("This command must be executed by a player!");
            return false;
        }
        ItemStack stack = ((Player) commandSender).getInventory().getItemInMainHand();
        if (stack.isEmpty()) {
            commandSender.sendMessage("Must be holding an item!");
            return false;
        }
        InfusionLoreUtils.applySortedLoreNBT(stack);
        commandSender.sendMessage("Updated held item lore!");
        return true;
    }

    public static UpdateItemCommand getInstance() {
        return new UpdateItemCommand();
    }

    @Override
    public @NotNull String getName() {
        return "updateitem";
    }
}
