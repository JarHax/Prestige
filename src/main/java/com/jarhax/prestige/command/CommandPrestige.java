package com.jarhax.prestige.command;

import com.jarhax.prestige.client.gui.GuiPrestige;

import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;

public class CommandPrestige extends CommandBase {

    @Override
    public String getName () {

        return "prestige";
    }

    @Override
    public String getUsage (ICommandSender sender) {

        return "prestige";
    }

    @Override
    public void execute (MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {

        // TODO make this a packet to open the gui?
        // Yes :P
        Minecraft.getMinecraft().displayGuiScreen(new GuiPrestige());
    }
}
