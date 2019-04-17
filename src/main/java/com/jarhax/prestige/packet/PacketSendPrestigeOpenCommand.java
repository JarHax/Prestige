package com.jarhax.prestige.packet;

import com.jarhax.prestige.Prestige;
import net.darkhax.bookshelf.network.SerializableMessage;
import net.minecraft.entity.player.*;
import net.minecraft.util.text.*;
import net.minecraftforge.fml.common.network.simpleimpl.*;

public class PacketSendPrestigeOpenCommand extends SerializableMessage {
    
    public PacketSendPrestigeOpenCommand() {
        
        // Empty constructor for forge's system
    }
    
    
    @Override
    public IMessage handleMessage(MessageContext context) {
        
        if(context == null || context.getServerHandler() == null || context.getServerHandler().player == null){
            Prestige.LOG.error("Something has seriously messed up when opening the prestige window!");
            return null;
        }
        final EntityPlayerMP player = context.getServerHandler().player;
        
        if(player.getEntityData().getCompoundTag(EntityPlayer.PERSISTED_NBT_TAG).getBoolean("prestigeEnabled")) {
            Prestige.NETWORK.sendTo(new PacketOpenPrestigeGUI(), player);
            
            player.sendMessage(new TextComponentTranslation("chat.prestige.open.reciever", player.getName()));
        } else {
            player.sendMessage(new TextComponentTranslation("chat.prestige.disabled").setStyle(new Style().setColor(TextFormatting.RED)));
        }
        return null;
    }
}
