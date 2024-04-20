package io.github.rainvaporeon.customenchantments.commands;

import io.github.rainvaporeon.customenchantments.enchant.Infusion;
import io.github.rainvaporeon.customenchantments.util.infusions.InfusionManager;
import io.github.rainvaporeon.customenchantments.util.infusions.InfusionUtils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.Style;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.PlayerInventory;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class CurrentInfusionCommand extends BaseCommand {
    public CurrentInfusionCommand() {
        super("currentinfusion");
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String commandLabel, @NotNull String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Must be a player to execute this command!");
            return false;
        }
        Player player = (Player) sender;
        PlayerInventory inventory = player.getInventory();
        sender.sendMessage("Here's the individual infusion stat you have:");
        for (EquipmentSlot slot : EquipmentSlot.values()) {
            sender.sendMessage(Component.text()
                    .color(NamedTextColor.GRAY)
                    .content("Slot ")
                    .append(Component.text(slot.name()).color(NamedTextColor.AQUA))
                    .append(Component.text(":").color(NamedTextColor.GRAY)));
            for (Map.Entry<Infusion, Integer> infusionInfo : InfusionUtils.getAllInfusions(inventory.getItem(slot)).entrySet()) {
                TextComponent.Builder component = Component.text()
                        .color(NamedTextColor.AQUA).content(infusionInfo.getKey().getName())
                        .append(Component.text(" (" + infusionInfo.getKey().getIdentifier() + ")").color(NamedTextColor.GRAY))
                        .append(Component.text(" Level " + infusionInfo.getValue()).color(NamedTextColor.GRAY));
                String desc = infusionInfo.getKey().getDescription();
                if (desc.isEmpty()) continue;
                component.style(Style.style().hoverEvent(HoverEvent.hoverEvent(HoverEvent.Action.SHOW_TEXT, Component.text(desc))).build());
                sender.sendMessage(component);
            }
        }
        sender.sendMessage("Here's the total accumulated infusion that you have:");
        for (Infusion infusion : InfusionManager.getInfusions()) {
            int level = InfusionUtils.accumulateInfusionLevelOf(player, infusion);
            if (level == 0) continue;

            TextComponent.Builder builder = Component.text()
                    .color(NamedTextColor.AQUA).content(infusion.getName())
                    .append(Component.text(" (" + infusion.getIdentifier() + ") Level " + level).color(NamedTextColor.GRAY));

            String extendedDescription = infusion.getExtendedDescription(level);
            if (extendedDescription != null) {
                builder.style(Style
                        .style()
                        .hoverEvent(HoverEvent.hoverEvent(HoverEvent.Action.SHOW_TEXT, Component.text(extendedDescription))).build());
            }
            sender.sendMessage(builder);
        }
        sender.sendMessage(Component.text().color(NamedTextColor.YELLOW).content("You can hover over the infusion name to see their effects."));
        return true;
    }

    @Override
    public @NotNull String getName() {
        return "currentinfusion";
    }
}
