package com.jarhax.prestige.config;

import net.minecraftforge.common.config.Configuration;

import java.io.File;

public class Config {
    
    public static int unavailableColour = 0xcc0000;
    public static int purchasedColour = 0x00cccc;
    public static int purchaseableColour = 0xFFFFFF;
    
    public static boolean newWorldMode;
    
    public static Configuration config;
    
    public static void init(File file) {
        config = new Configuration(file);
        config.load();
        unavailableColour = config.get("Colours", "Unavailable Colour", unavailableColour).getInt();
        purchasedColour = config.get("Colours", "Purchased Colour", purchasedColour).getInt();
        purchaseableColour = config.get("Colours", "Purchasable Colour", purchaseableColour).getInt();
        newWorldMode = config.getBoolean("NewWorldMode", "Gameplay", false, "Prestige window on a new world");
        config.save();
    }
    
    public static float getRed(int hex) {
        return ((hex >> 16) & 0xFF) / 255f;
    }
    
    public static float getGreen(int hex) {
        return ((hex >> 8) & 0xFF) / 255f;
    }
    
    public static float getBlue(int hex) {
        return ((hex) & 0xFF) / 255f;
    }
    
    public static float getAlpha(int hex) {
        return ((hex >> 24) & 0xff) / 255f;
    }
    
    
    public static float[] getARGB(int hex) {
        return new float[]{getAlpha(hex), getRed(hex), getGreen(hex), getBlue(hex)};
    }
    
}
