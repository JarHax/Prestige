package com.jarhax.prestige.packet;

import com.jarhax.prestige.Prestige;
import com.jarhax.prestige.api.Reward;
import com.jarhax.prestige.data.*;
import net.darkhax.bookshelf.network.SerializableMessage;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.network.simpleimpl.*;

public class PacketAttemptSell extends SerializableMessage {

    public String rewardKey;

    public PacketAttemptSell() {

        // Empty constructor for forge's system
    }

    public PacketAttemptSell(Reward reward) {

        this.rewardKey = reward.getIdentifier();
    }

    @Override
    public IMessage handleMessage (MessageContext context) {

        final EntityPlayer player = context.getServerHandler().player;
        final PlayerData data = GlobalPrestigeData.getPlayerData(player);
        final Reward reward = Prestige.REGISTRY.get(this.rewardKey);

        if (data.hasReward(reward)) {

            data.addPrestige(reward.getSellPrice());
            data.removeReward(reward);
            GlobalPrestigeData.save(player);
        }

        return null;
    }
}