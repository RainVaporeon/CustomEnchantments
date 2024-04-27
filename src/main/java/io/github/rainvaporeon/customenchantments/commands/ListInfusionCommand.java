package io.github.rainvaporeon.customenchantments.commands;

import io.github.rainvaporeon.customenchantments.enchant.Infusion;
import io.github.rainvaporeon.customenchantments.util.infusions.InfusionManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.Style;
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
           Component component = Component.text(infusion.getName())
                    .append(Component.text(" (" + infusion.getIdentifier() + ")").color(NamedTextColor.GRAY))
                    .append(Component.text(" Max level: " + infusion.getMaxLevel() + ", Effective: " + infusion.getMaxEffectiveLevel()).color(NamedTextColor.GRAY))
                    .style(Style.style().hoverEvent(HoverEvent.showText(Component.text(infusion.getDescription()))));
            sender.sendMessage(component);
        }
        sender.sendMessage(Component.text("You may hover over the infusions to see their effects.")
                .color(NamedTextColor.YELLOW));
        return true;
    }
}
