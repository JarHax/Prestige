package com.jarhax.prestige.packet;

import com.jarhax.prestige.client.gui.*;
import net.darkhax.bookshelf.network.SerializableMessage;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.network.simpleimpl.*;
import net.minecraftforge.fml.relauncher.*;

public class PacketEditPrestigeGUI extends SerializableMessage {

    public PacketEditPrestigeGUI() {

        // Empty constructor for forge's system
    }


    @Override
    @SideOnly(Side.CLIENT)
    public IMessage handleMessage (MessageContext context) {

        // Move logic off the packet thread
        Minecraft.getMinecraft().addScheduledTask( () -> {
            Minecraft.getMinecraft().displayGuiScreen(new GuiPrestigeEditing());
        });

        return null;
    }
}
