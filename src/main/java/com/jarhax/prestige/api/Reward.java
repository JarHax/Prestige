package com.jarhax.prestige.api;

import com.google.gson.annotations.Expose;
import com.jarhax.prestige.Prestige;

import net.minecraft.init.Blocks;
import net.minecraft.item.*;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTException;
import net.minecraft.nbt.NBTTagCompound;

import java.util.*;

public class Reward {
    
    @Expose
    private final String identifier;
    @Expose
    private String title;
    @Expose
    private String description;
    @Expose
    private String icon;
    @Expose
    private int x;
    @Expose
    private int y;
    @Expose
    private int cost;
    
    @Expose
    private final Set<String> parents = new HashSet<>();
    @Expose
    private final Set<String> children = new HashSet<>();
    /*
    Render stuff starts here
     */
    @Expose
    private boolean placed;
    
    
    public Reward(String identifier, String title, int x, int y, int cost, ItemStack icon, String description) {
        
        if(identifier == null || identifier.isEmpty()) {
            
            throw new IllegalArgumentException("Prestige reward identifier can not be null or empty!");
        } else if(identifier.matches("\\s+")) {
            
            throw new IllegalArgumentException("Prestige reward identifiers can not contain white space!");
        } else if(icon == null || icon.isEmpty()) {
            
            throw new IllegalArgumentException("Prestige reward icon can not be a null or empty/air item!");
        }
        
        this.identifier = identifier.toLowerCase();
        this.title = title;
        this.description = description;
        //todo fix this
        setIcon(icon);
        this.x = x;
        this.y = y;
        this.cost = cost;
    }
    
    public String getIdentifier() {
        
        return this.identifier;
    }
    
    public String getTitle() {
        
        return this.title;
    }
    
    public String getDescription() {
        
        return this.description;
    }
    
    public ItemStack getIcon() {
        
        // TODO cache result?
        try {
            return new ItemStack(JsonToNBT.getTagFromJson(this.icon));
        }
        catch (NBTException e) {
            
            Prestige.LOG.error("The reward \"{}\" has an invalid icon value of \"{}\".", this.identifier, this.icon);
            return new ItemStack(Blocks.BARRIER);
        }
    }
    
    public int getX() {
        
        return this.x;
    }
    
    public int getY() {
        
        return this.y;
    }
    
    public int getCost() {
        
        return this.cost;
    }
    
    public Set<Reward> getChildren() {
        Set<Reward> rewards = new HashSet<>();
        for(String s : this.children) {
            rewards.add(Prestige.REGISTRY.get(s));
        }
        return rewards;
    }
    
    public void addChild(Reward child) {
        
        if(this.children.contains(child.identifier)) {
            
            throw new IllegalArgumentException(String.format("The reward %s can not be a child of %s because it is a parent of %s.", child.getIdentifier(), this.identifier, this.identifier));
        }
        child.parents.add(getIdentifier());
        this.children.add(child.getIdentifier());
    }
    
    public void removeChild(Reward child) {
        this.children.remove(child.getIdentifier());
        child.parents.remove(this.getIdentifier());
    }
    
    public Set<Reward> getParents() {
        Set<Reward> rewards = new HashSet<>();
        for(String s : this.parents) {
            rewards.add(Prestige.REGISTRY.get(s));
        }
        return rewards;
    }
    
    public void addParent(Reward parent) {
        
        if(this.children.contains(parent.identifier)) {
            
            throw new IllegalArgumentException(String.format("The reward %s can not be a parent of %s because it is a child of %s.", parent.getIdentifier(), this.identifier, this.identifier));
        }
        
        this.parents.add(parent.getIdentifier());
        parent.addChild(this);
    }
    
    public void clearParents() {
        for(Reward reward : getParents()) {
            reward.children.remove(this.getIdentifier());
        }
        parents.clear();
        
    }
    
    public void clearChildren() {
        for(Reward reward : getChildren()) {
            reward.parents.remove(this.getIdentifier());
        }
        children.clear();
    }
    
    public void removeParent(Reward parent) {
        this.parents.remove(parent.getIdentifier());
        parent.children.remove(this.getIdentifier());
    }
    
    public boolean isPlaced() {
        return placed;
    }
    
    public void setPlaced(boolean placed) {
        this.placed = placed;
    }
    
    public void setX(int x) {
        this.x = x;
    }
    
    public void setY(int y) {
        this.y = y;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public void setIcon(ItemStack icon) {

        this.icon = icon.writeToNBT(new NBTTagCompound()).toString();
    }
    
    public void setCost(int cost) {
        this.cost = cost;
    }
}