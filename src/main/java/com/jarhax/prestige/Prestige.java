package com.jarhax.prestige;

import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.jarhax.prestige.api.Reward;
import com.jarhax.prestige.command.CommandPrestige;
import com.jarhax.prestige.data.GlobalPrestigeData;
import com.jarhax.prestige.data.PlayerData;
import com.jarhax.prestige.packet.PacketAttemptPurchase;
import com.jarhax.prestige.packet.PacketOpenPrestigeGUI;
import com.jarhax.prestige.packet.PacketSyncPrestige;

import net.darkhax.bookshelf.BookshelfRegistry;
import net.darkhax.bookshelf.network.NetworkHandler;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.event.FMLServerStoppingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@EventBusSubscriber
@Mod(modid = "prestige", name = "Prestige", version = "@VERSION@", dependencies = "required-after:bookshelf@[2.3.523,)", certificateFingerprint = "@FINGERPRINT@")
public class Prestige {

    public static final Logger LOG = LogManager.getLogger("Prestige");
    public static final NetworkHandler NETWORK = new NetworkHandler("prestige");
    public static final Map<String, Reward> REGISTRY = new HashMap<>();

    @SideOnly(Side.CLIENT)
    public static PlayerData clientPlayerData;

    @EventHandler
    public void onPreInit (FMLPreInitializationEvent event) {

        NETWORK.register(PacketSyncPrestige.class, Side.CLIENT);
        NETWORK.register(PacketOpenPrestigeGUI.class, Side.CLIENT);
        NETWORK.register(PacketAttemptPurchase.class, Side.SERVER);
        BookshelfRegistry.addCommand(new CommandPrestige());

    }

    @EventHandler
    public void onServerStart (FMLServerStartingEvent event) {

        GlobalPrestigeData.loadAll();
    }

    @EventHandler
    public void onServerStop (FMLServerStoppingEvent event) {

        GlobalPrestigeData.saveAll();
    }

    @SubscribeEvent
    public static void onPlayerLoggedIn (PlayerLoggedInEvent event) {

        // When a player connects to the server, sync their client data with the server's data.
        if (event.player instanceof EntityPlayerMP) {

            Prestige.LOG.info("Syncing requested for " + event.player.getName() + ".");
            NETWORK.sendTo(new PacketSyncPrestige(GlobalPrestigeData.getPlayerData(event.player)), (EntityPlayerMP) event.player);
        }
    }
}
