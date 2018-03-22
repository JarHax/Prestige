package com.jarhax.prestige.command;

import com.jarhax.prestige.client.gui.GuiPrestige;

import net.darkhax.bookshelf.command.Command;
import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;

public class CommandOpen extends Command {

    @Override
    public String getName () {

        return "open";
    }

    @Override
    public String getUsage (ICommandSender sender) {

        return "/prestige open";
    }

    @Override
    public void execute (MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {

        // TODO make this a packet to open the gui?
        // Yes :P
        Minecraft.getMinecraft().displayGuiScreen(new GuiPrestige());
    }
}