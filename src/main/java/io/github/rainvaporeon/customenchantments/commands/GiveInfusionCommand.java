package io.github.rainvaporeon.customenchantments.commands;

import io.github.rainvaporeon.customenchantments.enchant.Infusion;
import io.github.rainvaporeon.customenchantments.util.infusions.InfusionManager;
import io.github.rainvaporeon.customenchantments.util.Permission;
import io.github.rainvaporeon.customenchantments.util.TabCompletionUtils;
import io.github.rainvaporeon.customenchantments.util.infusions.InfusionUtils;
import io.github.rainvaporeon.fishutils.action.ActionResult;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class GiveInfusionCommand extends BaseCommand {
    protected GiveInfusionCommand(String name) {
        super(name);
    }

    @Override
    public BaseCommand getInstance() {
        return new GiveInfusionCommand("giveinfusion");
    }

    @Override
    public boolean execute(CommandSender commandSender, String s, String[] strings) {
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
        ActionResult<Integer> level = ActionResult.run(() -> Integer.parseInt(strings[1]));
        if (level.isSuccessful()) {
            boolean exceedsCap = level.getReturnValue() > infusion.getMaxLevel();
            if (exceedsCap && !overrideInfusionCap) {
                commandSender.sendMessage("This infusion can only have a maximum level of " + infusion.getMaxLevel() + "" +
                        ", and " + level.getReturnValue() + " is outside of its range!");
                return false;
            } else {
                Player player = commandSender.getServer().getPlayerExact(commandSender.getName());
                if (player == null) {
                    commandSender.sendMessage("This command must be executed by a player!");
                    return false;
                }
                ItemStack handItem = player.getInventory().getItemInMainHand();
                return InfusionUtils.applyInfusion(handItem, infusion.getIdentifier(), level.getReturnValue());
            }
        } else {
            commandSender.sendMessage("Invalid level " + strings[1] + " provided!");
            return false;
        }
    }

    @Override
    public @NotNull List<String> tabComplete(@NotNull CommandSender sender, @NotNull String alias, @NotNull String[] args) throws IllegalArgumentException {
        return switch (args.length) {
            case 1 -> TabCompletionUtils.startsWith(args[0], InfusionManager.getInfusionIdentifiers());
            case 3 -> TabCompletionUtils.startsWith(args[2], "true", "false");
            default -> List.of();
        };
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
