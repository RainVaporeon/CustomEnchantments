package io.github.rainvaporeon.customenchantments.commands;

import io.github.rainvaporeon.customenchantments.util.Permission;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Predicate;

public abstract class PermissibleCommand extends BaseCommand {

    protected PermissibleCommand(String name) {
        super(name);
    }

    protected PermissibleCommand(String name, String description, String usageMessage, List<String> aliases) {
        super(name, description, usageMessage, aliases);
    }

    @Override
    public @Nullable abstract String getPermission();

    public @NotNull Predicate<CommandSender> getPermissionPredicate() {
        return cs -> cs.hasPermission(Permission.bukkit(this.getPermission()));
    }

    public void sendErrorMessage(CommandSender sender) {
        sender.sendMessage("You do not have permission to run this command!");
    }

    @Override
    public final boolean execute(@NotNull CommandSender sender, @NotNull String commandLabel, @NotNull String[] args) {
        if (!this.getPermissionPredicate().test(sender)) {
            this.sendErrorMessage(sender);
            return false;
        }
        return this.executeCommand(sender, commandLabel, args);
    }

    public abstract boolean executeCommand(@NotNull CommandSender sender, @NotNull String label, @NotNull String[] args);
}
