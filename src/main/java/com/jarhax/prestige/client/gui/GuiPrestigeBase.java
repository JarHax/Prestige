package com.jarhax.prestige.client.gui;

import com.jarhax.prestige.client.gui.objects.*;
import com.jarhax.prestige.client.gui.objects.editing.GuiObjectEditingReward;
import com.jarhax.prestige.compat.crt.IReward;
import com.jarhax.prestige.data.PlayerData;
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
    public PlayerData data;
    
    protected LinkedList<IReward> rewardsToGive;
    
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
    
    public LinkedList<IReward> getRewardsToGive() {
        return rewardsToGive;
    }
    
    public void setRewardsToGive(LinkedList<IReward> rewardsToGive) {
        this.rewardsToGive = rewardsToGive;
    }
    
}
