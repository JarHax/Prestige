package com.jarhax.prestige.client.gui;

import java.util.ArrayList;
import java.util.List;


import com.jarhax.prestige.config.Config;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.config.GuiConfig;
import net.minecraftforge.fml.client.config.IConfigElement;

public class GuiConfigPrestige extends GuiConfig {
    
    private static Configuration cfg = Config.config;
    
    public GuiConfigPrestige(GuiScreen parent) {
        
        super(parent, generateConfigList(), "prestige", false, false, GuiConfig.getAbridgedConfigPath(cfg.toString()));
    }
    
    public static List<IConfigElement> generateConfigList () {
        
        final ArrayList<IConfigElement> elements = new ArrayList<>();
        
        for (final String name : cfg.getCategoryNames()) {
            
            elements.add(new ConfigElement(cfg.getCategory(name)));
        }
        
        return elements;
    }
}