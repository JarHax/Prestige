package com.jarhax.prestige;

import com.google.gson.*;
import com.jarhax.prestige.api.Reward;
import com.jarhax.prestige.command.CommandPrestige;
import com.jarhax.prestige.data.GlobalPrestigeData;
import com.jarhax.prestige.data.PlayerData;
import com.jarhax.prestige.packet.*;

import java.util.*;
import net.darkhax.bookshelf.BookshelfRegistry;
import net.darkhax.bookshelf.network.NetworkHandler;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.item.*;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.event.FMLServerStoppingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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
        NETWORK.register(PacketEditPrestigeGUI.class, Side.CLIENT);
        NETWORK.register(PacketAttemptPurchase.class, Side.SERVER);
        BookshelfRegistry.addCommand(new CommandPrestige());
        List<Reward> parents = new ArrayList<>();
        for(int i = 0;i<5;i++){
            parents.add(new Reward("parent" + i, "parent" +i,0,0,5,new ItemStack(Items.GOLDEN_APPLE), "parents"));
        }
        for (Reward parent : parents) {
            REGISTRY.put(parent.getIdentifier(), parent);
        }
        Random random = new Random();
        for (int i = 0; i < 95; i++) {
           
            Reward reward = new Reward("test" + i, "test" + i, 0, 0, 5, new ItemStack(ForgeRegistries.ITEMS.getValues().get(random.nextInt(ForgeRegistries.ITEMS.getValues().size()-1)+1)), "tests");
            reward.addParent(parents.get(random.nextInt(parents.size())));
            REGISTRY.put("test" + i, reward);
        }
        Gson gson = new GsonBuilder().setPrettyPrinting().excludeFieldsWithoutExposeAnnotation().create();
    
        System.out.println(gson.toJson(REGISTRY.values()));
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
