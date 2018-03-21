package com.jarhax.prestige;

import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.jarhax.prestige.api.Reward;
import com.jarhax.prestige.capability.IPrestigeData;
import com.jarhax.prestige.capability.PrestigeDataDefault;
import com.jarhax.prestige.capability.StoragePrestige;
import com.jarhax.prestige.command.CommandAdd;
import com.jarhax.prestige.command.CommandPrestige;
import com.jarhax.prestige.data.GlobalPrestigeData;

import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;

@Mod(modid = "prestige", name = "Prestige", version = "@VERSION@", dependencies = "required-after:bookshelf@[2.3.523,)", certificateFingerprint = "@FINGERPRINT@")
public class Prestige {

    public static final Logger LOG = LogManager.getLogger("Prestige");
    public static final Map<String, Reward> REGISTRY = new HashMap<>();

    @EventHandler
    public void onPreInit (FMLPreInitializationEvent event) {

        GlobalPrestigeData.loadAllSavedPlayers();

        CapabilityManager.INSTANCE.register(IPrestigeData.class, new StoragePrestige(), PrestigeDataDefault::new);
    }

    @EventHandler
    public void onFMLServerStarting (FMLServerStartingEvent event) {

        event.registerServerCommand(new CommandAdd());
        event.registerServerCommand(new CommandPrestige());
    }
}
