package io.github.rainvaporeon.customenchantments.commands;

import io.github.rainvaporeon.customenchantments.enchant.Infusion;
import io.github.rainvaporeon.customenchantments.util.TabCompletionUtils;
import io.github.rainvaporeon.customenchantments.util.infusions.InfusionManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.Style;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
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
        if (args.length > 0) {
            registry.removeIf(i -> Arrays.stream(args).noneMatch(i.getIdentifier()::equals));
        }
        sender.sendMessage("There are currently " + registry.size() + " registered infusions: ");
        if (args.length > 0) {
            sender.sendMessage(Component.text("Some infusions may have been filtered from requests.")
                    .color(NamedTextColor.GRAY));
        }
        for (Infusion infusion : registry) {
           Component component = Component.text(infusion.getName())
                    .append(Component.text(" (" + infusion.getIdentifier() + ")").color(NamedTextColor.GRAY))
                    .append(Component.text(" Max level: " + infusion.getMaxLevel() + ", Effective: " + infusion.getMaxEffectiveLevel()).color(NamedTextColor.GRAY))
                    .style(Style.style().color(NamedTextColor.AQUA).hoverEvent(HoverEvent.showText(Component.text(infusion.getDescription()))));
            sender.sendMessage(component);
        }
        sender.sendMessage(Component.text("You may hover over the infusions to see their effects.")
                .color(NamedTextColor.YELLOW));
        return true;
    }

    @Override
    public @NotNull List<String> getAliases() {
        return Collections.singletonList("li");
    }

    @Override
    public @NotNull List<String> tabComplete(@NotNull CommandSender sender, @NotNull String alias, @NotNull String[] args) throws IllegalArgumentException {
        if (args.length == 0) return InfusionManager.getInfusionIdentifiers();
        return TabCompletionUtils.startsWith(args[args.length - 1], InfusionManager.getInfusionIdentifiers());
    }
}
