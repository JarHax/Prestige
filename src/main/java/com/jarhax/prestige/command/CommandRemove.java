package com.jarhax.prestige.command;

import com.jarhax.prestige.data.*;
import net.darkhax.bookshelf.command.Command;
import net.minecraft.command.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import org.apache.commons.lang3.math.NumberUtils;

public class CommandRemove extends Command {
    
    @Override
    public String getName() {
        
        return "remove";
    }
    
    @Override
    public String getUsage(ICommandSender sender) {
        
        return "/prestige remove <player> <amount> [source-id]";
    }
    
    @Override
    public int getRequiredPermissionLevel() {
        
        return 2;
    }
    
    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        
        if(args.length == 2) {
            
            if(NumberUtils.isParsable(args[1])) {
                final EntityPlayer player = getPlayer(server, sender, args[0]);
                if(player != null) {
                    final PlayerData data = GlobalPrestigeData.getPlayerData(player);
                    data.removePrestige(Long.parseLong(args[1]));
                    GlobalPrestigeData.save(player);
                }
            } else if(args[1].equalsIgnoreCase("all")) {
                final EntityPlayer player = getPlayer(server, sender, args[0]);
                
                if(player != null) {
                    final PlayerData data = GlobalPrestigeData.getPlayerData(player);
                    data.removePrestige(data.getPrestige());
                    GlobalPrestigeData.save(player);
                }
            }
        }
    }
}
