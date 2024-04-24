package io.github.rainvaporeon.customenchantments.util.texts;

public class Color {
    private int red;
    private int green;
    private int blue;

    public Color(int red, int green, int blue) {
        this.red = red;
        this.green = green;
        this.blue = blue;
    }

    public static Color fromIntValue(int value) {
        java.awt.Color color = new java.awt.Color(value);
        return new Color(color.getRed(), color.getGreen(), color.getBlue());
    }

    public int getRed() {
        return red;
    }

    public int getGreen() {
        return green;
    }

    public int getBlue() {
        return blue;
    }

    public void setRed(int red) {
        this.red = red;
    }

    public void setGreen(int green) {
        this.green = green;
    }

    public void setBlue(int blue) {
        this.blue = blue;
    }

    public int getValue() {
        return (red << 16) | (green << 8) | blue;
    }
}
