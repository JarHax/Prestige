package com.jarhax.prestige.command;

import com.jarhax.prestige.Prestige;
import com.jarhax.prestige.packet.PacketOpenPrestigeGUI;

import net.darkhax.bookshelf.command.Command;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentTranslation;

public class CommandOpen extends Command {

    @Override
    public String getName () {

        return "open";
    }

    @Override
    public String getUsage (ICommandSender sender) {

        return "/prestige open [player]";
    }

    @Override
    public int getRequiredPermissionLevel () {

        return 2;
    }

    @Override
    public void execute (MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {

        final EntityPlayerMP player = args.length == 1 ? getPlayer(server, sender, args[0]) : sender instanceof EntityPlayerMP ? (EntityPlayerMP) sender : null;

        if (player != null) {

            Prestige.NETWORK.sendTo(new PacketOpenPrestigeGUI(), player);

            player.sendMessage(new TextComponentTranslation("chat.prestige.open.reciever", sender.getName()));

            if (player != sender) {

                sender.sendMessage(new TextComponentTranslation("chat.prestige.open.sender", player.getName()));
            }
        }
    }
}