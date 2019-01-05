package com.jarhax.prestige.packet;

import com.jarhax.prestige.Prestige;
import com.jarhax.prestige.api.Reward;
import com.jarhax.prestige.data.*;
import net.darkhax.bookshelf.network.SerializableMessage;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.simpleimpl.*;
import net.minecraftforge.fml.relauncher.*;

public class PacketRespec extends SerializableMessage {

    public NBTTagCompound prestigeData;

    public PacketRespec() {

        // Empty constructor for forge's system
    }

    public PacketRespec(PlayerData data) {

        this.prestigeData = data.save();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IMessage handleMessage (MessageContext context) {

        // Move logic off the packet thread
        final EntityPlayer player = context.getServerHandler().player;
        final PlayerData data = GlobalPrestigeData.getPlayerData(player);
    
        long sells = data.getPrestige();
        for(Reward reward : data.getUnlockedRewards()) {
            sells += reward.getSellPrice();
        }
        data.addPrestige(sells);
        data.getUnlockedRewards().clear();
        data.setLastRespec(System.nanoTime());
        return null;
    }
}