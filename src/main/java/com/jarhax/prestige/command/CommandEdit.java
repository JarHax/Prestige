package com.jarhax.prestige.command;

import com.jarhax.prestige.Prestige;
import com.jarhax.prestige.packet.PacketEditPrestigeGUI;
import net.darkhax.bookshelf.command.Command;
import net.minecraft.command.*;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentTranslation;

public class CommandEdit extends Command {
    
    @Override
    public String getName() {
        
        return "edit";
    }
    
    @Override
    public String getUsage(ICommandSender sender) {
        
        return "/prestige edit";
    }
    
    @Override
    public int getRequiredPermissionLevel() {
        
        return 2;
    }
    
    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        final EntityPlayerMP player = (EntityPlayerMP) sender;
        
        if(server.isDedicatedServer()) {
            player.sendMessage(new TextComponentTranslation("chat.prestige.open.server", sender.getName()));
        }
        
        Prestige.NETWORK.sendTo(new PacketEditPrestigeGUI(), player);
        
        player.sendMessage(new TextComponentTranslation("chat.prestige.open.sender", sender.getName()));
    }
}