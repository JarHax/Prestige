package com.jarhax.prestige.packet;

import com.jarhax.prestige.Prestige;
import com.jarhax.prestige.data.PlayerData;

import net.darkhax.bookshelf.network.SerializableMessage;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class PacketSyncPrestige extends SerializableMessage {

    public NBTTagCompound prestigeData;

    public PacketSyncPrestige () {

        // Empty constructor for forge's system
    }

    public PacketSyncPrestige (PlayerData data) {

        this.prestigeData = data.save();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IMessage handleMessage (MessageContext context) {

        // Move logic off the packet thread
        Minecraft.getMinecraft().addScheduledTask( () -> {

            Prestige.LOG.info("Recieved client update from server.");
            Prestige.clientPlayerData = new PlayerData(this.prestigeData);
        });

        return null;
    }
}
