package com.jarhax.prestige.packet;

import com.jarhax.prestige.client.gui.GuiPrestige;

import net.darkhax.bookshelf.network.SerializableMessage;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class PacketOpenPrestigeGUI extends SerializableMessage {

    public boolean editing;
    public PacketOpenPrestigeGUI () {

        // Empty constructor for forge's system
    }
    
    public PacketOpenPrestigeGUI(boolean editing) {
        this.editing = editing;
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public IMessage handleMessage (MessageContext context) {

        // Move logic off the packet thread
        Minecraft.getMinecraft().addScheduledTask( () -> {

            Minecraft.getMinecraft().displayGuiScreen(new GuiPrestige(editing));
        });

        return null;
    }
}
