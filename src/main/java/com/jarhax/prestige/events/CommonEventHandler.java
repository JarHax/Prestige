package com.jarhax.prestige.events;

import com.blamejared.ctgui.reference.Reference;
import com.jarhax.prestige.Prestige;
import com.jarhax.prestige.api.Reward;
import com.jarhax.prestige.compat.crt.IReward;
import com.jarhax.prestige.data.*;
import crafttweaker.api.minecraft.CraftTweakerMC;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;

import java.util.*;

public class CommonEventHandler {
    
    public static final String NBT_NAME = Reference.MOD_ID + "_given_rewards";
    
    @SubscribeEvent
    public void playerLogin(PlayerEvent.PlayerLoggedInEvent event) {
        NBTTagCompound tag = event.player.getEntityData();
        NBTTagCompound data;
        if(!tag.hasKey(EntityPlayer.PERSISTED_NBT_TAG)) {
            data = new NBTTagCompound();
        } else {
            data = tag.getCompoundTag(EntityPlayer.PERSISTED_NBT_TAG);
        }
        
        
        PlayerData playerData = GlobalPrestigeData.getPlayerData(event.player);
        for(Reward reward : playerData.getUnlockedRewards()) {
            if(data.hasKey(reward.getIdentifier())) {
                continue;
            }
            List<IReward> list = Prestige.REWARDS.getOrDefault(reward.getIdentifier(), new ArrayList<>());
            for(IReward iReward : list) {
                iReward.process(CraftTweakerMC.getIWorld(event.player.world), CraftTweakerMC.getIPlayer(event.player));
            }
            data.setBoolean(reward.getIdentifier(), true);
        }
        tag.setTag(EntityPlayer.PERSISTED_NBT_TAG, data);
        
        
    }
}
