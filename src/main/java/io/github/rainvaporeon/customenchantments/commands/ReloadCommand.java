package io.github.rainvaporeon.customenchantments.commands;

import io.github.rainvaporeon.customenchantments.util.Permission;
import io.github.rainvaporeon.customenchantments.util.io.LocalConfig;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ReloadCommand extends PermissibleCommand {
    protected ReloadCommand() {
        super("reload");
    }

    @Override
    public @Nullable String getPermission() {
        return Permission.of(this.getName());
    }

    @Override
    public boolean executeCommand(@NotNull CommandSender sender, @NotNull String label, @NotNull String[] args) {
        boolean hard = args.length != 0 && Boolean.parseBoolean(args[0]);
        LocalConfig.instance().reload(hard);
        sender.sendMessage("The configuration has been reloaded.");
        return true;
    }

    @Override
    public @NotNull String getName() {
        return "reload";
    }

    public static ReloadCommand getInstance() {
        return new ReloadCommand();
    }

    @Override
    public @NotNull String getUsage() {
        return "/" + getName() + " [true|false] - true if reloading from disk";
    }

    @Override
    public @NotNull List<String> tabComplete(@NotNull CommandSender sender, @NotNull String alias, @NotNull String[] args) throws IllegalArgumentException {
        if (args.length <= 1) return Arrays.asList("true", "false");
        return Collections.emptyList();
    }

    @Override
    public @NotNull List<String> getAliases() {
        return Collections.singletonList("cireload");
    }
}
