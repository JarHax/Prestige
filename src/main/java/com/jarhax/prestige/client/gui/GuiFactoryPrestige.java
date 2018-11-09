package com.jarhax.prestige.client.gui;

import java.util.Collections;
import java.util.Set;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.fml.client.IModGuiFactory;

public class GuiFactoryPrestige implements IModGuiFactory {
    
    @Override
    public void initialize (Minecraft mc) {
        
        // not used yet.
    }
    
    @Override
    public boolean hasConfigGui () {
        
        return true;
    }
    
    @Override
    public GuiScreen createConfigGui (GuiScreen parent) {
        
        return new GuiConfigPrestige(parent);
    }
    
    @Override
    public Set<RuntimeOptionCategoryElement> runtimeGuiCategories () {
        
        return Collections.<RuntimeOptionCategoryElement> emptySet();
    }
}