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

public class RemoveStoredInfusionCommand extends BaseCommand {
    protected RemoveStoredInfusionCommand() {
        super("removeinfusion");
    }

    public static BaseCommand getInstance() {
        return new RemoveStoredInfusionCommand();
    }

    @Override
    public @NotNull String getName() {
        return "removestoredinfusion";
    }

    @Override
    public boolean execute(@NotNull CommandSender commandSender, @NotNull String s, String[] strings) {
        if (!commandSender.hasPermission(Permission.bukkit(this.getName()))) {
            commandSender.sendMessage("You do not have permission to do this!");
            return false;
        }

        try {
            return execute0(commandSender, strings);
        } catch (Exception ex) {
            commandSender.sendMessage("An internal error had occurred whilst executing this command.");
            CustomEnchantments.PLUGIN.getLogger().log(Level.SEVERE, "Exception whilst using " + s + " with args " + Arrays.toString(strings) + ": ", ex);
            return false;
        }
    }

    private static final int PLAYER_INDEX = 0;
    private static final int IDENTIFIER_INDEX = 1;
    private boolean execute0(CommandSender commandSender, String[] strings) {
        if (strings.length <= 1) {
            sendHelp(commandSender);
            return true;
        }

        Player player = commandSender.getServer().getPlayerExact(strings[PLAYER_INDEX]);
        if (player == null) {
            commandSender.sendMessage("Cannot find this player! Are they online?");
            return false;
        }
        ItemStack handItem = player.getInventory().getItemInMainHand();
        ItemStack resultItem = InfusionUtils.removeStoredInfusion(handItem, strings[IDENTIFIER_INDEX]);
        if (resultItem != null) {
            commandSender.sendMessage("Successfully removed this infusion!");
            player.getInventory().setItemInMainHand(resultItem);
            return true;
        } else {
            if (InfusionManager.getInfusionById(strings[IDENTIFIER_INDEX]) != null) {
                commandSender.sendMessage("This infusion does not exist on this item!");
                return true;
            }
            commandSender.sendMessage("Failed to remove this infusion. Does the identifier " + strings[IDENTIFIER_INDEX] + " exist?");
            return false;
        }
    }

    @Override
    public @NotNull List<String> tabComplete(@NotNull CommandSender sender, @NotNull String alias, @NotNull String[] args) throws IllegalArgumentException {
        if (args.length == 1) {
            return TabCompletionUtils.serverMembers(args[PLAYER_INDEX], sender);
        }
        if (args.length == 2) {
            return TabCompletionUtils.startsWith(args[IDENTIFIER_INDEX], InfusionManager.getInfusionIdentifiers());
        }
        return Collections.emptyList();
    }

    @Override
    public @Nullable String getPermission() {
        return Permission.of("removestoredinfusion");
    }

    @Override
    public @NotNull List<String> getAliases() {
        return Collections.singletonList("rsi");
    }

    private void sendHelp(CommandSender sender) {
        sender.sendMessage("/removestoredinfusion <player> <type>");
    }
}
