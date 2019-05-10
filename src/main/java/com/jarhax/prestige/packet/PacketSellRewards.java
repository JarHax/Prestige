package com.jarhax.prestige.packet;

import com.jarhax.prestige.Prestige;
import com.jarhax.prestige.compat.crt.ISellAction;
import crafttweaker.api.minecraft.CraftTweakerMC;
import net.darkhax.bookshelf.network.SerializableMessage;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.*;
import net.minecraftforge.fml.server.FMLServerHandler;

import java.util.*;

public class PacketSellRewards extends SerializableMessage {
    
    public String[] rewards;
    
    public PacketSellRewards() {
        
        // Empty constructor for forge's system
    }
    
    public PacketSellRewards(String[] rewards) {
        
        this.rewards = rewards;
    }
    
    @Override
    public IMessage handleMessage(MessageContext context) {
    
        FMLCommonHandler.instance().getMinecraftServerInstance().addScheduledTask(() -> {
            final EntityPlayer player = context.getServerHandler().player;
            for(String s : rewards) {
                List<ISellAction> sellActions = Prestige.SELL_ACTIONS.getOrDefault(s, new ArrayList<>());
                for(ISellAction action : sellActions) {
                    action.process(CraftTweakerMC.getIWorld(player.world), CraftTweakerMC.getIPlayer(player));
                }
            }
        });
        
        return null;
    }
}