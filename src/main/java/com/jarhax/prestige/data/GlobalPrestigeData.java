package com.jarhax.prestige.data;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.jarhax.prestige.Prestige;
import com.jarhax.prestige.packet.PacketSyncPrestige;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.CompressedStreamTools;

public class GlobalPrestigeData {

    private static final File SAVE_DIR = new File("prestige");
    private static final Map<UUID, PlayerData> CACHE = new HashMap<>();

    static {

        if (!SAVE_DIR.exists()) {

            SAVE_DIR.mkdirs();
        }
    }

    public static PlayerData getPlayerData (UUID id) {
        
        return CACHE.computeIfAbsent(id, PlayerData::new);
    }
    
    public static PlayerData getPlayerData (EntityPlayer player) {

        return getPlayerData(player.getUniqueID());
    }

    public static void saveAll () {

        for (final PlayerData data : CACHE.values()) {

            save(data);
        }
    }

    public static void save (EntityPlayer player) {

        final PlayerData data = getPlayerData(player);
        save(data);
        
        if (player instanceof EntityPlayerMP) {
            
            Prestige.NETWORK.sendTo(new PacketSyncPrestige(data), (EntityPlayerMP) player);
        }
    }

    public static void save (PlayerData data) {

        try {

            CompressedStreamTools.write(data.save(), new File(SAVE_DIR, data.getFileName()));
            Prestige.LOG.info("Saving data for {}.", data.getPlayerId().toString());
            CACHE.put(data.getPlayerId(), data);
        }

        catch (final IOException e) {

            Prestige.LOG.trace("Could not save data for " + data.getPlayerId().toString(), e);
        }
    }

    public static void loadAll () {

        for (final File file : SAVE_DIR.listFiles()) {

            final PlayerData data = load(file);

            if (data != null) {

                CACHE.put(data.getPlayerId(), data);
                Prestige.LOG.info("Successfully loaded data for {}. Confirmed: {} Unlocked: {}", data.getPlayerId().toString(), data.getPrestige(), data.getUnlockedRewards().size());
            }
        }
    }

    public static PlayerData load (File file) {

        if (file.exists()) {

            try {

                return new PlayerData(CompressedStreamTools.read(file));
            }

            catch (final Exception e) {

                Prestige.LOG.trace("Could not load player data from " + file.getName(), e);
            }
        }

        return null;
    }
}
