package com.jarhax.prestige;

import java.util.HashMap;
import java.util.Map;

import com.jarhax.prestige.api.Reward;
import com.jarhax.prestige.capability.IPrestigeData;
import com.jarhax.prestige.capability.PrestigeDataDefault;
import com.jarhax.prestige.capability.StoragePrestige;
import com.jarhax.prestige.client.gui.GuiPrestige;
import com.jarhax.prestige.command.CommandAdd;
import com.jarhax.prestige.data.GlobalPrestigeData;

import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;

@Mod(modid = "prestige", name = "Prestige", version = "@VERSION@", dependencies = "required-after:bookshelf@[2.3.523,)", certificateFingerprint = "@FINGERPRINT@")
public class Prestige {

    public static final Map<String, Reward> REGISTRY = new HashMap<>();

    public static final GlobalPrestigeData prestige = GlobalPrestigeData.readData().save();

    @EventHandler
    public void onPreInit (FMLPreInitializationEvent event) {

        CapabilityManager.INSTANCE.register(IPrestigeData.class, new StoragePrestige(), PrestigeDataDefault::new);
    }

    @EventHandler
    public void onFMLServerStarting (FMLServerStartingEvent event) {

        event.registerServerCommand(new CommandAdd());

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
