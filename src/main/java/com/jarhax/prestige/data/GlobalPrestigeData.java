package com.jarhax.prestige.data;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

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

        return CACHE.computeIfAbsent(player.getPersistentID(), key -> new PlayerData(key));
    }

    public static void loadAllSavedPlayers () {

        for (final File file : SAVE_DIR.listFiles()) {

            final PlayerData data = load(file);

            if (data != null) {

                CACHE.put(data.getPlayerId(), data);
            }
        }
    }

    public static PlayerData load (File file) {

        if (file.exists()) {

            try {

                return new PlayerData(CompressedStreamTools.read(file));
            }

            catch (final IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        // new PlayerData(UUID.fromString(file.getName().replace(".dat", "")));
        return null;
    }

    public static void save (EntityPlayer player) {

        try {

            CompressedStreamTools.write(getPlayerData(player).save(), getPlayerFile(player));
        }

        catch (final IOException e) {

            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static File getPlayerFile (EntityPlayer player) {

        return new File(SAVE_DIR, player.getUniqueID().toString() + ".dat");
    }
}
