package com.jarhax.prestige;

import com.jarhax.prestige.client.gui.GuiPrestige;

import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;

@Mod(modid = "prestige", name = "Prestige", version = "@VERSION@", dependencies = "required-after:bookshelf@[2.3.523,)", certificateFingerprint = "@FINGERPRINT@")
public class Prestige {

    @Mod.EventHandler
    public void onFMLServerStarting (FMLServerStartingEvent event) {

        // TODO move this to it's own class
        event.registerServerCommand(new CommandBase() {
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
                Minecraft.getMinecraft().displayGuiScreen(new GuiPrestige());
            }
        });
    }
}
