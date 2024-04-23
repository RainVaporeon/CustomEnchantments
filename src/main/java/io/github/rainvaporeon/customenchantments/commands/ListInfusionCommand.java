package io.github.rainvaporeon.customenchantments.commands;

import io.github.rainvaporeon.customenchantments.enchant.Infusion;
import io.github.rainvaporeon.customenchantments.util.infusions.InfusionManager;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public class ListInfusionCommand extends BaseCommand {
    protected ListInfusionCommand() {
        super("listinfusion");
    }

    public static ListInfusionCommand getInstance() {
        return new ListInfusionCommand();
    }

    @Override
    public @NotNull String getName() {
        return "listinfusion";
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String commandLabel, @NotNull String[] args) {
        Set<Infusion> registry = InfusionManager.getInfusions();
        sender.sendMessage("There are currently " + registry.size() + " registered infusions: ");
        for (Infusion infusion : registry) {
            sender.sendMessage(infusion.getName() + " (" + infusion.getIdentifier() + ") Max Level: " + infusion.getMaxLevel());
        }
        return true;
    }
}
