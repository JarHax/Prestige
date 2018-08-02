package com.jarhax.prestige.packet;

import com.jarhax.prestige.Prestige;
import com.jarhax.prestige.api.Reward;
import com.jarhax.prestige.compat.crt.IReward;
import com.jarhax.prestige.data.*;
import crafttweaker.api.minecraft.CraftTweakerMC;
import net.darkhax.bookshelf.network.SerializableMessage;
import net.minecraftforge.fml.common.network.simpleimpl.*;

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
    public IMessage handleMessage (MessageContext context) {

        for(String s : rewards) {
            final Reward reward = Prestige.REGISTRY.get(s);
    
            List<IReward> list = Prestige.REWARDS.getOrDefault(reward.getIdentifier(), new ArrayList<>());
            for(IReward iReward : list) {
                iReward.process(CraftTweakerMC.getIWorld(context.getServerHandler().player.world), CraftTweakerMC.getIPlayer(context.getServerHandler().player));
            }
            
        }

        return null;
    }
}