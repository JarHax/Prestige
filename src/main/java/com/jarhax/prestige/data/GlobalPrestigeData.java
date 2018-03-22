package com.jarhax.prestige.data;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.jarhax.prestige.Prestige;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.CompressedStreamTools;

public class GlobalPrestigeData {

    private static final File SAVE_DIR = new File("prestige");
    private static final Map<UUID, PlayerData> CACHE = new HashMap<>();

    static {

        if (!SAVE_DIR.exists()) {

            SAVE_DIR.mkdirs();
        }
    }

    public static PlayerData getPlayerData (EntityPlayer player) {

        return CACHE.computeIfAbsent(player.getPersistentID(), PlayerData::new);
    }

    public static void loadAllSavedPlayers () {

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

            catch (final IOException e) {

                Prestige.LOG.trace("Could not load player data from " + file.getName(), e);
            }
        }

        return null;
    }

    public static void save (EntityPlayer player) {

        try {

            CompressedStreamTools.write(getPlayerData(player).save(), getPlayerFile(player));
            Prestige.LOG.info("Saving data for {}.", player.getName());
        }

        catch (final IOException e) {

            Prestige.LOG.trace("Could not save data for " + player.getName(), e);
        }
    }

    public static File getPlayerFile (EntityPlayer player) {

        return new File(SAVE_DIR, player.getUniqueID().toString() + ".dat");
    }
}
