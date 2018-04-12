package com.jarhax.prestige.client.gui.objects;

import com.jarhax.prestige.api.Reward;
import com.jarhax.prestige.client.gui.GuiPrestigeBase;
import net.minecraft.client.Minecraft;

public abstract class GuiObject {
    
    protected boolean visible;
    protected boolean enabled;
    /**
     * Location relative to the parent gui's left
     */
    protected float x;
    /**
     * Location relative to the parent gui's top
     */
    protected float y;
    protected int width;
    protected int height;
    
    protected boolean alwaysVisible;
    
    protected final GuiPrestigeBase parent;
    
    protected Minecraft mc;
    
    public GuiObject(GuiPrestigeBase parent, int x, int y, int width, int height) {
        
        this.parent = parent;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.visible = true;
        this.enabled = true;
        this.mc = Minecraft.getMinecraft();
    }
    
    public boolean collides(GuiObject other) {
        if(other == null) {
            return false;
        }
        
        return this.collides(other.getX(), other.getY(), other.getX() + other.getWidth(), other.getY() + other.getHeight());
    }
    
    public boolean collides(float x1, float y1, float x2, float y2) {
        
        if(x2 > getX() && x1 < getX() + getWidth()) {
            if(y2 > getY() && y1 < getY() + getHeight()) {
                return true;
            }
        }
        return false;
    }
    
    public void update() {
    
    }
    
    public abstract void draw(int left, int top, int mouseX, int mouseY, float partialTicks);
    
    public void onClick(int mx, int my, int button) {
    
    }
    
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
    
    }
    
    public void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick) {
    
    }
    
    public void mouseReleased(int mouseX, int mouseY, int state) {
    
    }
    
    public boolean isVisible() {
        
        return this.visible;
    }
    
    public void setVisible(boolean visible) {
        
        this.visible = visible;
    }
    
    public boolean isEnabled() {
        
        return this.enabled;
    }
    
    public void setEnabled(boolean enabled) {
        
        this.enabled = enabled;
    }
    
    public float getX() {
        
        return this.x;
    }
    
    public void setX(float x) {
        
        this.x = x;
    }
    
    public float getY() {
        
        return this.y;
    }
    
    public void setY(float y) {
        
        this.y = y;
    }
    
    public int getWidth() {
        
        return this.width;
    }
    
    public void setWidth(int width) {
        
        this.width = width;
    }
    
    public int getHeight() {
        
        return this.height;
    }
    
    public void setHeight(int height) {
        
        this.height = height;
    }
    
    public GuiPrestigeBase getParent() {
        
        return this.parent;
    }
    
    public boolean isAlwaysVisible() {
        
        return this.alwaysVisible;
    }
    
    public void setAlwaysVisible(boolean alwaysVisible) {
        
        this.alwaysVisible = alwaysVisible;
    }
    
    public void addX(float x){
        setX(getX() + x);
    }
    public void addY(float y){
        setY(getY() + y);
    }
    
    
}
