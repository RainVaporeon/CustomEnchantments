package io.github.rainvaporeon.customenchantments.commands;

import io.github.rainvaporeon.customenchantments.CustomEnchantments;
import io.github.rainvaporeon.customenchantments.util.Permission;
import io.github.rainvaporeon.customenchantments.util.TabCompletionUtils;
import io.github.rainvaporeon.customenchantments.util.infusions.InfusionManager;
import io.github.rainvaporeon.customenchantments.util.infusions.InfusionUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;

public class RemoveInfusionCommand extends BaseCommand {
    protected RemoveInfusionCommand() {
        super("removeinfusion");
    }

    public static BaseCommand getInstance() {
        return new RemoveInfusionCommand();
    }

    @Override
    public @NotNull String getName() {
        return "removeinfusion";
    }

    @Override
    public boolean execute(@NotNull CommandSender commandSender, @NotNull String s, String[] strings) {
        try {
            return execute0(commandSender, strings);
        } catch (Exception ex) {
            commandSender.sendMessage("An internal error had occurred whilst executing this command.");
            CustomEnchantments.PLUGIN.getLogger().log(Level.SEVERE, "Exception whilst using " + s + " with args " + Arrays.toString(strings) + ": ", ex);
            return false;
        }
    }

    private boolean execute0(CommandSender commandSender, String[] strings) {
        if (strings.length == 0) {
            sendHelp(commandSender);
            return true;
        }

        Player player = commandSender.getServer().getPlayerExact(commandSender.getName());
        if (player == null) {
            commandSender.sendMessage("This command must be executed by a player!");
            return false;
        }
        ItemStack handItem = player.getInventory().getItemInMainHand();
        if (InfusionUtils.removeInfusion(handItem, strings[0])) {
            commandSender.sendMessage("Successfully removed this infusion!");
            player.getInventory().setItemInMainHand(handItem);
            return true;
        } else {
            if (InfusionManager.getInfusionById(strings[0]) != null) {
                commandSender.sendMessage("This infusion does not exist on this item!");
                return true;
            }
            commandSender.sendMessage("Failed to remove this infusion. Does the identifier " + strings[0] + " exist?");
            return false;
        }
    }

    @Override
    public @NotNull List<String> tabComplete(@NotNull CommandSender sender, @NotNull String alias, @NotNull String[] args) throws IllegalArgumentException {
        if (args.length == 1) {
            return TabCompletionUtils.startsWith(args[0], InfusionManager.getInfusionIdentifiers());
        }
        return Collections.emptyList();
    }

    @Override
    public @Nullable String getPermission() {
        return Permission.of("removeinfusion");
    }

    @Override
    public @NotNull List<String> getAliases() {
        return Collections.singletonList("ri");
    }

    private void sendHelp(CommandSender sender) {
        sender.sendMessage("/removeinfusion <type>");
    }
}
