package io.github.rainvaporeon.customenchantments.commands;

import io.github.rainvaporeon.customenchantments.enchant.Infusion;
import io.github.rainvaporeon.customenchantments.util.infusions.InfusionManager;
import io.github.rainvaporeon.customenchantments.util.infusions.InfusionUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.PlayerInventory;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
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
            sender.sendMessage("Slot " + slot + ": ");
            for (Map.Entry<Infusion, Integer> infusionInfo : InfusionUtils.getAllInfusions(inventory.getItem(slot)).entrySet()) {
                sender.sendMessage("Infusion " + infusionInfo.getKey().getName() + " (" + infusionInfo.getKey().getIdentifier() + ") Level " + infusionInfo.getValue());
            }
        }
        sender.sendMessage("Here's the total accumulated infusion that you have:");
        for (Infusion infusion : InfusionManager.getInfusions()) {
            int level = InfusionUtils.accumulateInfusionLevelOf(player, infusion);
            if (level == 0) continue;
            sender.sendMessage(infusion.getName() + " (" + infusion.getIdentifier() + "): Level " + level);
        }
        return true;
    }

    @Override
    public @NotNull String getName() {
        return "currentinfusion";
    }
}
