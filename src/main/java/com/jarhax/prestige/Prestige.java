package com.jarhax.prestige;

import com.google.gson.*;
import com.jarhax.prestige.api.Reward;
import com.jarhax.prestige.command.CommandPrestige;
import com.jarhax.prestige.data.*;
import com.jarhax.prestige.packet.*;
import net.darkhax.bookshelf.BookshelfRegistry;
import net.darkhax.bookshelf.network.NetworkHandler;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.*;
import net.minecraftforge.fml.common.event.*;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.fml.relauncher.*;
import org.apache.logging.log4j.*;

import java.io.*;
import java.util.*;

@EventBusSubscriber
@Mod(modid = "prestige", name = "Prestige", version = "@VERSION@", dependencies = "required-after:bookshelf@[2.3.523,)", certificateFingerprint = "@FINGERPRINT@")
public class Prestige {
    
    public static final Logger LOG = LogManager.getLogger("Prestige");
    public static final NetworkHandler NETWORK = new NetworkHandler("prestige");
    public static final Map<String, Reward> REGISTRY = new HashMap<>();
    
    @SideOnly(Side.CLIENT)
    public static PlayerData clientPlayerData;
    public static final Gson GSON = new GsonBuilder().setPrettyPrinting().excludeFieldsWithoutExposeAnnotation().create();
    public static File JSON_FILE;
    
    @Instance("prestige")
    public static Prestige INSTANCE;
    
    @EventHandler
    public void onPreInit(FMLPreInitializationEvent event) {
        
        NETWORK.register(PacketSyncPrestige.class, Side.CLIENT);
        NETWORK.register(PacketOpenPrestigeGUI.class, Side.CLIENT);
        NETWORK.register(PacketEditPrestigeGUI.class, Side.CLIENT);
        NETWORK.register(PacketAttemptPurchase.class, Side.SERVER);
        BookshelfRegistry.addCommand(new CommandPrestige());
        JSON_FILE = new File(new File(event.getModConfigurationDirectory(), "prestige"), "rewards.json");
    }
    
    @EventHandler
    public void onFMLInitialization(FMLInitializationEvent event) {
        loadRewards();
    }
    
    public void saveRewards() {
        
            try {
                if(!JSON_FILE.exists()) {
                JSON_FILE.getParentFile().mkdirs();
                JSON_FILE.createNewFile();
                }
                BufferedWriter writer = new BufferedWriter(new FileWriter(JSON_FILE));
                
    
                String json = GSON.toJson(REGISTRY.values().toArray());
                writer.write(json);
                writer.close();
            } catch(Exception e) {
                LOG.error("Unable to save Prestige JSON file!", e);
            }
        
    }
    
    public void loadRewards() {
        if(JSON_FILE.exists()) {
            REGISTRY.clear();
            try(FileReader reader = new FileReader(JSON_FILE)) {
                Reward[] rewards = GSON.fromJson(reader, Reward[].class);
                for(Reward reward : rewards) {
                    REGISTRY.put(reward.getIdentifier(), reward);
                }
            } catch(IOException e) {
                LOG.error("Unable to read Prestige JSON file!", e);
            }
        }
    }
    
    @EventHandler
    public void onServerStart(FMLServerStartingEvent event) {
        
        GlobalPrestigeData.loadAll();
    }
    
    @EventHandler
    public void onServerStop(FMLServerStoppingEvent event) {
        
        GlobalPrestigeData.saveAll();
    }
    
    @SubscribeEvent
    public static void onPlayerLoggedIn(PlayerLoggedInEvent event) {
        
        // When a player connects to the server, sync their client data with the server's data.
        if(event.player instanceof EntityPlayerMP) {
            
            Prestige.LOG.info("Syncing requested for " + event.player.getName() + ".");
            NETWORK.sendTo(new PacketSyncPrestige(GlobalPrestigeData.getPlayerData(event.player)), (EntityPlayerMP) event.player);
        }
    }
}
