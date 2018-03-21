package com.jarhax.prestige.command;

import com.jarhax.prestige.data.GlobalPrestigeData;
import com.jarhax.prestige.data.PlayerData;

import net.darkhax.bookshelf.command.Command;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentTranslation;

public class CommandInfo extends Command {

    @Override
    public String getName () {

        return "info";
    }

    @Override
    public int getRequiredPermissionLevel () {

        return 0;
    }

    @Override
    public String getUsage (ICommandSender sender) {

        return "/prestige info";
    }

    @Override
    public void execute (MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {

        if (sender instanceof EntityPlayer) {

            final PlayerData data = GlobalPrestigeData.getPlayerData((EntityPlayer) sender);
            sender.sendMessage(new TextComponentTranslation("chat.prestige.confirmed", data.getConfirmed()));
            sender.sendMessage(new TextComponentTranslation("chat.prestige.unconfirmed", data.getUnconfirmed()));
            sender.sendMessage(new TextComponentTranslation("chat.prestige.unlocked", data.getUnlockedRewards().size()));
        }
    }
}
