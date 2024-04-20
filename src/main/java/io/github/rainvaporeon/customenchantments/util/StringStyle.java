package io.github.rainvaporeon.customenchantments.util;

import io.github.rainvaporeon.customenchantments.util.enums.RomanNumeral;

import java.util.Arrays;
import java.util.Iterator;

public class StringStyle {

    public static String toRomanNumerals(int number) {
        // highest representable is 3999 as the largest unit is M (1000)
        if (number >= 4000 || number <= 0) return String.valueOf(number);
        StringBuilder builder = new StringBuilder();
        Iterator<RomanNumeral> it = Arrays.stream(RomanNumeral.values()).iterator();
        while (it.hasNext()) {
            RomanNumeral numeral = it.next();
            while (number >= numeral.value()) {
                builder.append(numeral);
                number -= numeral.value();
            }
        }
        return builder.toString();
    }
}
