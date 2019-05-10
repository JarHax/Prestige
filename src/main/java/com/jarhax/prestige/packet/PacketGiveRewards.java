package com.jarhax.prestige.packet;

import com.jarhax.prestige.Prestige;
import com.jarhax.prestige.api.Reward;
import com.jarhax.prestige.compat.crt.IReward;
import crafttweaker.api.minecraft.CraftTweakerMC;
import net.darkhax.bookshelf.network.SerializableMessage;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.*;
import net.minecraftforge.fml.server.FMLServerHandler;

import java.util.*;

public class PacketGiveRewards extends SerializableMessage {
    
    public String[] rewards;
    
    public PacketGiveRewards() {
        
        // Empty constructor for forge's system
    }
    
    public PacketGiveRewards(String[] rewards) {
        
        this.rewards = rewards;
    }
    
    @Override
    public IMessage handleMessage(MessageContext context) {
        FMLCommonHandler.instance().getMinecraftServerInstance().addScheduledTask(() -> {
            NBTTagCompound tag = context.getServerHandler().player.getEntityData();
            NBTTagCompound data;
            if(!tag.hasKey(EntityPlayer.PERSISTED_NBT_TAG)) {
                data = new NBTTagCompound();
            } else {
                data = tag.getCompoundTag(EntityPlayer.PERSISTED_NBT_TAG);
            }
            for(String s : rewards) {
                final Reward reward = Prestige.REGISTRY.get(s);
                if(reward == null) {
                    Prestige.LOG.error(s + " is not a registered reward!");
                    continue;
                }
                data.setBoolean(reward.getIdentifier(), true);
                List<IReward> list = Prestige.REWARDS.getOrDefault(reward.getIdentifier(), new ArrayList<>());
                for(IReward iReward : list) {
                    iReward.process(CraftTweakerMC.getIWorld(context.getServerHandler().player.world), CraftTweakerMC.getIPlayer(context.getServerHandler().player));
                }
            }
            tag.setTag(EntityPlayer.PERSISTED_NBT_TAG, data);
        });
        
        return null;
    }
}