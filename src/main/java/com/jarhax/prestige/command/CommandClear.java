package com.jarhax.prestige.command;

import com.jarhax.prestige.api.Reward;
import com.jarhax.prestige.data.*;
import net.darkhax.bookshelf.command.Command;
import net.minecraft.command.*;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentTranslation;

public class CommandClear extends Command {
    
    @Override
    public String getName() {
        
        return "clear";
    }
    
    @Override
    public String getUsage(ICommandSender sender) {
        
        return "/remove player clear";
    }
    
    @Override
    public int getRequiredPermissionLevel() {
        
        return 2;
    }
    
    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        
        
        final EntityPlayerMP player = args.length == 1 ? getPlayer(server, sender, args[0]) : sender instanceof EntityPlayerMP ? (EntityPlayerMP) sender : null;
        
        if(player != null) {
            
            final PlayerData data = GlobalPrestigeData.getPlayerData(player);
            data.getUnlockedRewards().clear();
            GlobalPrestigeData.save(player);
            sender.sendMessage(new TextComponentTranslation("chat.prestige.clear.sender", player.getName()));
        }
    }
}
