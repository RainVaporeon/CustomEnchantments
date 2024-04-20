package io.github.rainvaporeon.customenchantments.commands;

import com.google.common.collect.Lists;
import io.github.rainvaporeon.customenchantments.CustomEnchantments;
import io.github.rainvaporeon.customenchantments.enchant.Infusion;
import io.github.rainvaporeon.customenchantments.util.infusions.InfusionManager;
import io.github.rainvaporeon.customenchantments.util.Permission;
import io.github.rainvaporeon.customenchantments.util.TabCompletionUtils;
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

public class GiveInfusionCommand extends BaseCommand {
    protected GiveInfusionCommand(String name) {
        super(name);
    }

    public static BaseCommand getInstance() {
        return new GiveInfusionCommand("giveinfusion");
    }

    @Override
    public @NotNull String getName() {
        return "giveinfusion";
    }

    @Override
    public boolean execute(CommandSender commandSender, String s, String[] strings) {
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
        Infusion infusion = InfusionManager.getInfusionById(strings[0]);
        if (strings.length == 1 || infusion == null) {
            this.sendQueryHelp(commandSender, infusion);
            return true;
        }
        boolean overrideInfusionCap = strings.length >= 3 && Boolean.parseBoolean(strings[2]);
        int level;
        try {
            level = Integer.parseInt(strings[1]);
        } catch (NumberFormatException ex) {
            /* swallow */
            level = -1;
        }
        if (level >= 0) {
            boolean exceedsCap = level > infusion.getMaxLevel();
            if (exceedsCap && !overrideInfusionCap) {
                commandSender.sendMessage("This infusion can only have a maximum level of " + infusion.getMaxLevel() + "" +
                        ", and " + level + " is outside of its range!");
                return false;
            } else {
                Player player = commandSender.getServer().getPlayerExact(commandSender.getName());
                if (player == null) {
                    commandSender.sendMessage("This command must be executed by a player!");
                    return false;
                }
                ItemStack handItem = player.getInventory().getItemInMainHand();
                if (InfusionUtils.applyInfusion(handItem, infusion.getIdentifier(), level)) {
                    commandSender.sendMessage("Successfully applied this infusion!");
                    player.getInventory().setItemInMainHand(handItem);
                    return true;
                } else {
                    commandSender.sendMessage("Failed to apply this infusion. Does the identifier " + infusion.getIdentifier() + " exist?");
                    return false;
                }
            }
        } else {
            commandSender.sendMessage("Invalid level " + strings[1] + " provided!");
            return false;
        }
    }

    @Override
    public @NotNull List<String> tabComplete(@NotNull CommandSender sender, @NotNull String alias, @NotNull String[] args) throws IllegalArgumentException {
        switch (args.length) {
            case 1:
                return TabCompletionUtils.startsWith(args[0], InfusionManager.getInfusionIdentifiers());
            case 3:
                return TabCompletionUtils.startsWith(args[2], "true", "false");
            default:
                return Collections.emptyList();
        }
    }

    @Override
    public @Nullable String getPermission() {
        return Permission.of("giveinfusion");
    }

    private void sendHelp(CommandSender sender) {
        sender.sendMessage("/giveinfusion <type> <level> [override]");
    }

    private void sendQueryHelp(CommandSender sender, Infusion infusion) {
        if (infusion == null) {
            sender.sendMessage("This infusion does not exist!");
        } else {
            sender.sendMessage("/giveinfusion <type> <level> [override]");
            sender.sendMessage("The maximum level for this infusion is " + infusion.getMaxLevel());
        }
    }
}
