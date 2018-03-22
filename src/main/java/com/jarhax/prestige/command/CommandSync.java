package com.jarhax.prestige.command;

import com.jarhax.prestige.Prestige;
import com.jarhax.prestige.data.GlobalPrestigeData;
import com.jarhax.prestige.packet.PacketSyncPrestige;

import net.darkhax.bookshelf.command.Command;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentTranslation;

public class CommandSync extends Command {

    @Override
    public String getName () {

        return "sync";
    }

    @Override
    public String getUsage (ICommandSender sender) {

        return "/prestige sync";
    }

    @Override
    public int getRequiredPermissionLevel () {

        return 0;
    }

    @Override
    public void execute (MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {

        if (sender instanceof EntityPlayerMP) {

            Prestige.LOG.info("Syncing manually requested by {}.", sender.getName());
            Prestige.NETWORK.sendTo(new PacketSyncPrestige(GlobalPrestigeData.getPlayerData((EntityPlayer) sender)), (EntityPlayerMP) sender);
            sender.sendMessage(new TextComponentTranslation("chat.prestige.synced"));
        }
    }
}