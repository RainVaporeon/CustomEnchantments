package io.github.rainvaporeon.customenchantments.commands;

import org.bukkit.command.Command;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public abstract class BaseCommand extends Command {

    protected BaseCommand(String name) {
        super(name);
    }

    protected BaseCommand(String name, String description, String usageMessage, List<String> aliases) {
        super(name, description, usageMessage, aliases);
    }

    @Override
    public abstract @NotNull String getName();
}
