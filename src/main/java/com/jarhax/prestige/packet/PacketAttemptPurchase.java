package com.jarhax.prestige.packet;

import com.jarhax.prestige.Prestige;
import com.jarhax.prestige.api.Reward;
import com.jarhax.prestige.data.GlobalPrestigeData;
import com.jarhax.prestige.data.PlayerData;

import net.darkhax.bookshelf.network.SerializableMessage;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.event.FMLServerAboutToStartEvent;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.server.FMLServerHandler;

public class PacketAttemptPurchase extends SerializableMessage {
    
    public String rewardKey;
    
    public PacketAttemptPurchase() {
        
        // Empty constructor for forge's system
    }
    
    public PacketAttemptPurchase(Reward reward) {
        
        this.rewardKey = reward.getIdentifier();
    }
    
    @Override
    public IMessage handleMessage(MessageContext context) {
    
        FMLCommonHandler.instance().getMinecraftServerInstance().addScheduledTask(() -> {
            final EntityPlayer player = context.getServerHandler().player;
            final PlayerData data = GlobalPrestigeData.getPlayerData(player);
            final Reward reward = Prestige.REGISTRY.get(this.rewardKey);
            
            if(data.canPurchase(reward)) {
                
                data.removePrestige(reward.getCost());
                data.unlockReward(reward);
                GlobalPrestigeData.save(player);
            }
        });
        
        
        return null;
    }
}