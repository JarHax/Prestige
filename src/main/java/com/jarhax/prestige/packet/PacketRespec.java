package com.jarhax.prestige.packet;

import com.jarhax.prestige.api.Reward;
import com.jarhax.prestige.config.Config;
import com.jarhax.prestige.data.*;
import net.darkhax.bookshelf.network.SerializableMessage;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.simpleimpl.*;

public class PacketRespec extends SerializableMessage {
    
    public NBTTagCompound prestigeData;
    
    public long time;
    
    public PacketRespec() {
        
        // Empty constructor for forge's system
    }
    
    
    public PacketRespec(PlayerData data) {
        
        this.prestigeData = data.save();
    }
    
    @Override
    public IMessage handleMessage(MessageContext context) {
        
        // Move logic off the packet thread
        final EntityPlayer player = context.getServerHandler().player;
        final PlayerData data = GlobalPrestigeData.getPlayerData(player);
        
        long sells = data.getPrestige();
        for(Reward reward : data.getUnlockedRewards()) {
            sells += reward.getSellPrice();
        }
        data.addPrestige(sells);
        data.getUnlockedRewards().clear();
        data.setRespTimer(Config.respecCooldown);
        return null;
    }
}
