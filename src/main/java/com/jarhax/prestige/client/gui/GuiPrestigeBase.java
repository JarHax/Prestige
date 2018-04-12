package com.jarhax.prestige.client.gui;

import com.jarhax.prestige.client.gui.objects.*;
import net.minecraft.client.gui.GuiScreen;

import java.util.Map;

public class GuiPrestigeBase extends GuiScreen {
    
    protected Map<String, GuiObject> guiObjects;
    
    protected int guiWidth;
    protected int guiHeight;
    protected int left;
    protected int top;
    
    protected int prevMX;
    protected int prevMY;
    
    public Map<String, GuiObject> getGuiObjects() {
        return guiObjects;
    }
    
    
    public int getLeft() {
        
        return this.left;
    }
    
    public int getTop() {
        
        return this.top;
    }
    
    public int getPrevMX() {
        
        return this.prevMX;
    }
    
    public int getPrevMY() {
        
        return this.prevMY;
    }
    
    public int getGuiWidth() {
        
        return this.guiWidth;
    }
    
    public int getGuiHeight() {
        
        return this.guiHeight;
    }
}
