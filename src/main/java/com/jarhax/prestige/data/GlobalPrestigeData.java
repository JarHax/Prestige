package com.jarhax.prestige.data;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;

import net.minecraft.entity.player.EntityPlayer;

public class GlobalPrestigeData {

    @Expose
    private final Map<UUID, PlayerData> playerMap;

    public static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    public static final File FILE = new File("prestige-data.json");

    public GlobalPrestigeData () {

        this.playerMap = new HashMap<>();
    }

    public static GlobalPrestigeData readData () {

        if (!FILE.exists()) {

            new GlobalPrestigeData().save();
        }

        try (final FileReader reader = new FileReader(FILE)) {

            return GSON.fromJson(reader, GlobalPrestigeData.class);
        }

        catch (final IOException e) {

            e.printStackTrace();
        }

        return null;
    }

    public GlobalPrestigeData save () {

        try (final FileWriter writer = new FileWriter(FILE)) {

            GSON.toJson(this, writer);
        }

        catch (final IOException e) {

            e.printStackTrace();
        }

        return this;
    }

    public PlayerData getPlayerData (EntityPlayer player) {

        return this.playerMap.computeIfAbsent(player.getPersistentID(), key -> new PlayerData());
    }
}
