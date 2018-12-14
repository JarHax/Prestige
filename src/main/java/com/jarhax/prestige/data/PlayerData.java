package com.jarhax.prestige.data;

import com.jarhax.prestige.Prestige;
import com.jarhax.prestige.api.Reward;
import net.darkhax.bookshelf.util.NBTUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.*;
import net.minecraftforge.common.util.Constants.NBT;

import java.util.*;

public class PlayerData {
    
    private static final String TAG_OWNER = "PlayerId";
    private static final String TAG_CONFIRMED = "Prestige";
    private static final String TAG_UNLOCKED = "Unlocked";
    private static final String TAG_SOURCES = "Sources";
    private static final String TAG_LAST_RESPEC = "LastRespec";
    
    private UUID playerId;
    private long prestige;
    private final Set<Reward> unlockedRewards;
    private final Set<String> sources;
    private long lastRespec;
    
    public PlayerData(EntityPlayer player) {
        
        this(player.getPersistentID());
    }
    
    public PlayerData(UUID id) {
        
        this.playerId = id;
        this.prestige = 0;
        this.unlockedRewards = new HashSet<>();
        this.sources = new HashSet<>();
        this.lastRespec = 0;
    }
    
    public PlayerData(NBTTagCompound tag) {
        
        this.playerId = tag.getUniqueId(TAG_OWNER);
        this.prestige = tag.getLong(TAG_CONFIRMED);
        this.unlockedRewards = (Set<Reward>) NBTUtils.readCollection(new HashSet<Reward>(), tag.getTagList(TAG_UNLOCKED, NBT.TAG_STRING), Prestige.REGISTRY::get);
        this.sources = (Set<String>) NBTUtils.readCollection(new HashSet<String>(), tag.getTagList(TAG_SOURCES, NBT.TAG_STRING), string -> string);
        this.lastRespec = tag.getLong(TAG_LAST_RESPEC);
    }
    
    public NBTTagCompound save() {
        
        final NBTTagCompound tag = new NBTTagCompound();
        
        tag.setUniqueId(TAG_OWNER, this.playerId);
        tag.setLong(TAG_CONFIRMED, this.prestige);
        
        
        final NBTTagList tagList = new NBTTagList();
        
        for(final Reward reward : unlockedRewards) {
            if(reward != null) {
                tagList.appendTag(new NBTTagString(reward.getIdentifier()));
            }
        }
        tag.setTag(TAG_UNLOCKED, tagList);
        tag.setTag(TAG_SOURCES, NBTUtils.writeCollection(this.sources, string -> string));
        tag.setLong(TAG_LAST_RESPEC, this.lastRespec);
        return tag;
    }
    
    
    public UUID getPlayerId() {
        
        return this.playerId;
    }
    
    public void setPlayerId(UUID playerId) {
        
        this.playerId = playerId;
    }
    
    public long getPrestige() {
        
        return this.prestige;
    }
    
    public void setPrestige(long confirmed) {
        
        this.prestige = confirmed;
    }
    
    public void addPrestige(long amount) {
        
        this.prestige += amount;
    }
    
    public void removePrestige(long amount) {
        
        this.prestige -= amount;
    }
    
    public Set<Reward> getUnlockedRewards() {
        
        return this.unlockedRewards;
    }
    
    public void unlockReward(Reward reward) {
        
        this.getUnlockedRewards().add(reward);
    }
    
    public void removeReward(Reward reward) {
        
        this.getUnlockedRewards().remove(reward);
    }
    
    public Set<String> getSources() {
        
        return this.sources;
    }
    
    public boolean hasSource(String source) {
        
        return this.getSources().contains(source);
    }
    
    public void addSource(String source) {
        
        this.getSources().add(source);
    }
    
    public void removeSource(String source) {
        
        this.getSources().remove(source);
    }
    
    public String getFileName() {
        
        return this.playerId.toString() + ".dat";
    }
    
    public boolean hasReward(Reward reward) {
        
        return this.getUnlockedRewards().contains(reward);
    }
    
    public boolean canPurchase(Reward reward) {
        
        return reward != null && this.prestige >= reward.getCost() && !this.hasReward(reward) && this.hasRequiredRewards(reward);
    }
    
    public boolean hasRequiredRewards(Reward reward) {
        
        for(final Reward parent : reward.getParents()) {
            
            if(!this.hasReward(parent)) {
                
                return false;
            }
        }
        
        return true;
    }
    
    
    public long getLastRespec() {
        return lastRespec;
    }
    
    public void setLastRespec(long lastRespec) {
        this.lastRespec = lastRespec;
    }
}
