package io.github.rainvaporeon.customenchantments.commands;

import io.github.rainvaporeon.customenchantments.CustomEnchantments;
import io.github.rainvaporeon.customenchantments.enchant.Infusion;
import io.github.rainvaporeon.customenchantments.util.Permission;
import io.github.rainvaporeon.customenchantments.util.TabCompletionUtils;
import io.github.rainvaporeon.customenchantments.util.infusions.InfusionLoreUtils;
import io.github.rainvaporeon.customenchantments.util.infusions.InfusionManager;
import io.github.rainvaporeon.customenchantments.util.infusions.InfusionUtils;
import io.github.rainvaporeon.customenchantments.util.server.Server;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;

public class GiveStoredInfusionCommand extends BaseCommand {
    protected GiveStoredInfusionCommand() {
        super("givestoredinfusion");
    }

    public static BaseCommand getInstance() {
        return new GiveStoredInfusionCommand();
    }

    @Override
    public @NotNull String getName() {
        return "givestoredinfusion";
    }


    private static final int PLAYER_INDEX = 0;
    private static final int IDENTIFIER_INDEX = 1;
    private static final int LEVEL_INDEX = 2;
    private static final int FORCE_INDEX = 3;
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
            Server.log(Level.SEVERE, "Exception whilst using " + s + " with args " + Arrays.toString(strings) + ": ", ex);
            return false;
        }
    }

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

        Infusion infusion = InfusionManager.getInfusionById(strings[IDENTIFIER_INDEX]);
        if (infusion == null) {
            sendHelp(commandSender);
            return true;
        }
        int level;
        if (strings.length == LEVEL_INDEX) {
            level = 1;
        } else {
            try {
                level = Integer.parseInt(strings[LEVEL_INDEX]);
            } catch (NumberFormatException ex) {
                /* swallow */
                level = -1;
            }
        }
        boolean force = strings.length > FORCE_INDEX && Boolean.parseBoolean(strings[FORCE_INDEX]);
        if (level >= 0) {
            boolean exceedsCap = level > infusion.getMaxLevel();
            if (exceedsCap && !force) {
                commandSender.sendMessage("This infusion can only have a maximum level of " + infusion.getMaxLevel() +
                        ", and " + level + " is outside of its range!");
                return false;
            } else {
                ItemStack handItem = player.getInventory().getItemInMainHand();
                if (handItem.getType() != Material.ENCHANTED_BOOK && handItem.getType() != Material.BOOK) {
                    commandSender.sendMessage("The hand item is not an enchanted book!");
                    return false;
                }
                if (InfusionUtils.applyStoredInfusion(handItem, infusion.getIdentifier(), level)) {
                    commandSender.sendMessage("Successfully applied this infusion!");
                    player.getInventory().setItemInMainHand(InfusionLoreUtils.applySortedLoreNBT(handItem));
                    return true;
                } else {
                    commandSender.sendMessage("Failed to apply this infusion. Does the identifier " + infusion.getIdentifier() + " exist?");
                    return false;
                }
            }
        } else {
            commandSender.sendMessage("Invalid level " + strings[LEVEL_INDEX] + " provided!");
            return false;
        }
    }

    @Override
    public @NotNull List<String> tabComplete(@NotNull CommandSender sender, @NotNull String alias, @NotNull String[] args) throws IllegalArgumentException {
        switch (args.length) {
            case 1:
                return TabCompletionUtils.serverMembers(args[PLAYER_INDEX], sender);
            case 2:
                return TabCompletionUtils.startsWith(args[IDENTIFIER_INDEX], InfusionManager.getInfusionIdentifiers());
            case 4:
                return TabCompletionUtils.startsWith(args[FORCE_INDEX], "true", "false");
            default:
                return Collections.emptyList();
        }
    }

    @Override
    public @Nullable String getPermission() {
        return Permission.of("givestoredinfusion");
    }

    private void sendHelp(CommandSender sender) {
        sender.sendMessage("/givestoredinfusion <player> <type> <level> [override]");
    }

    @Override
    public @NotNull List<String> getAliases() {
        return Collections.singletonList("gsi");
    }

    @Override
    public boolean testPermission(@NotNull CommandSender target) {
        return target.hasPermission(Permission.of(this.getName()));
    }
}
