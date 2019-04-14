package com.jarhax.prestige.client.gui;

import com.jarhax.prestige.client.gui.objects.GuiObjectReward;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;

import java.util.*;

public class GuiPrestigeBase extends GuiScreen {
    
    protected Map<String, GuiObjectReward> guiObjects;
    
    protected int guiWidth;
    protected int guiHeight;
    protected int left;
    protected int top;
    
    protected int prevMX;
    protected int prevMY;
    
    public EntityPlayer player;
    
    protected LinkedList<GuiObjectReward> rewardsToGive;
    protected LinkedList<GuiObjectReward> rewardsToSell;
    
    
    public Map<String, GuiObjectReward> getGuiObjects() {
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
    
    public LinkedList<GuiObjectReward> getRewardsToGive() {
        return rewardsToGive;
    }
    
    public void setRewardsToGive(LinkedList<GuiObjectReward> rewardsToGive) {
        this.rewardsToGive = rewardsToGive;
    }
    
    public LinkedList<GuiObjectReward> getRewardsToSell() {
        return rewardsToSell;
    }
    
    public void setRewardsToSell(LinkedList<GuiObjectReward> rewardsToSell) {
        this.rewardsToSell = rewardsToSell;
    }
    
    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }
    
}
