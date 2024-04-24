package io.github.rainvaporeon.customenchantments.util.texts;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.TextColor;

import java.util.List;

public class Texts {

    public static Component homosexualification(String text) {
        return rainbow(text);
    }

    private static final int[] RAINBOW = {0xFF0000, 0xFFA500, 0xFFFF00, 0x008000, 0x0000FF, 0x4B0082, 0xEE82EE};
    public static Component rainbow(String text) {
        char[] sequence = text.toCharArray();

        TextComponent.Builder builder = Component.text();
        List<Color> colors = Colors.getGradients(sequence.length, RAINBOW);
        for (int i = 0; i < sequence.length; i++) {
            builder.append(
                    Component.text(sequence[i]).color(TextColor.color(colors.get(i).getValue()))
            );
        }
        return builder.build();
    }

}
