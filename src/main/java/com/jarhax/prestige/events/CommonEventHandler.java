package com.jarhax.prestige.events;

import com.jarhax.prestige.Prestige;
import com.jarhax.prestige.api.Reward;
import com.jarhax.prestige.compat.crt.IReward;
import com.jarhax.prestige.config.Config;
import com.jarhax.prestige.data.*;
import com.jarhax.prestige.packet.PacketOpenPrestigeGUI;
import crafttweaker.api.minecraft.CraftTweakerMC;
import net.minecraft.entity.player.*;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;

import java.util.*;

public class CommonEventHandler {
    
    
    @SubscribeEvent
    public void playerLogin(PlayerEvent.PlayerLoggedInEvent event) {
        
        NBTTagCompound tag = event.player.getEntityData();
        NBTTagCompound data;
        if(!tag.hasKey(EntityPlayer.PERSISTED_NBT_TAG)) {
            data = new NBTTagCompound();
        } else {
            data = tag.getCompoundTag(EntityPlayer.PERSISTED_NBT_TAG);
        }
        if(!data.hasKey("prestigeEnabled"))
            data.setBoolean("prestigeEnabled", Prestige.prestigeEnabled);
        
        if(data.getBoolean("prestigeEnabled")) {
            if(Config.newWorldMode)
                if(!data.getBoolean("shownMenu")) {
                    Prestige.NETWORK.sendTo(new PacketOpenPrestigeGUI(), (EntityPlayerMP) event.player);
                    data.setBoolean("shownMenu", true);
                }
            Prestige.ENABLED_ACTIONS.forEach(enabledAction -> enabledAction.process(CraftTweakerMC.getIWorld(event.player.world), CraftTweakerMC.getIPlayer(event.player)));
            
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
            
        } else {
            Prestige.DISABLED_ACTIONS.forEach(iDisabledAction -> iDisabledAction.process(CraftTweakerMC.getIWorld(event.player.world), CraftTweakerMC.getIPlayer(event.player)));
        }
        tag.setTag(EntityPlayer.PERSISTED_NBT_TAG, data);
    }
    
    
}
