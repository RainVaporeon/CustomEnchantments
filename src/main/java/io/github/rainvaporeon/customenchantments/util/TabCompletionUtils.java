package io.github.rainvaporeon.customenchantments.util;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class TabCompletionUtils {

    public static List<String> startsWith(String string, String... candidates) {
        return Arrays.stream(candidates).filter(id -> id.toLowerCase(Locale.ROOT).startsWith(string.toLowerCase(Locale.ROOT))).collect(Collectors.toList());
    }

    public static List<String> startsWith(String string, Collection<String> candidates) {
        return candidates.stream().filter(id -> id.toLowerCase(Locale.ROOT).startsWith(string.toLowerCase(Locale.ROOT))).collect(Collectors.toList());
    }

    public static List<String> serverMembers(String string, CommandSender sender) {
        return TabCompletionUtils.startsWith(string.toLowerCase(Locale.ROOT), sender.getServer()
                .getOnlinePlayers()
                .stream()
                .map(Player::getName)
                .map(String::toLowerCase)
                .collect(Collectors.toList()));
    }
}
