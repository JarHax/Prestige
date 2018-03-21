package com.jarhax.prestige.command;

import org.apache.commons.lang3.math.NumberUtils;

import com.jarhax.prestige.Prestige;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;

public class CommandAdd extends CommandBase {

    @Override
    public String getName () {

        return "add";
    }

    @Override
    public String getUsage (ICommandSender sender) {

        return "/add player amount";
    }

    @Override
    public void execute (MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {

        if (args.length == 2 && NumberUtils.isParsable(args[1])) {

            final EntityPlayer player = getPlayer(server, sender, args[0]);

            if (player != null) {

                Prestige.prestige.getPlayerData(player).add(Long.parseLong(args[1]), true);
                Prestige.prestige.save();
            }
        }
    }
}
