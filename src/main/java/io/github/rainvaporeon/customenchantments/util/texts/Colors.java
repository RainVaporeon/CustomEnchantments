package io.github.rainvaporeon.customenchantments.util.texts;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Colors {
    public static List<Color> getGradients(int steps, int... values) {
        if (values.length <= 1) return values.length == 0 ? Collections.emptyList() : Collections.singletonList(Color.fromIntValue(values[0]));
        int perSteps = steps / values.length;
        List<Color> clr = new ArrayList<>();
        for (int i = 0; i < values.length - 1; i++) {
            clr.addAll(Colors.getGradient(values[i], values[i + 1], perSteps));
        }
        return clr;
    }

    public static List<Color> getGradient(int from, int to, int steps) {
        Color left = Color.fromIntValue(from);
        Color right = Color.fromIntValue(to);
        List<Color> colors = new ArrayList<>();
        for (int i = left.getRed(); i <= right.getRed(); i += (right.getRed() - left.getRed()) / steps) {
            colors.add(new Color(i, 0, 0));
        }
        int idx = 0;
        for (int i = left.getBlue(); i <= right.getBlue(); i += (right.getBlue() - left.getBlue()) / steps) {
            colors.get(idx++).setBlue(i);
        }
        idx = 0;
        for (int i = left.getGreen(); i <= right.getGreen(); i += (right.getGreen() - left.getGreen()) / steps) {
            colors.get(idx++).setGreen(i);
        }
        return colors;
    }
}
