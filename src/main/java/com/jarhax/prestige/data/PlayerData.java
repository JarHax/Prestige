package com.jarhax.prestige.data;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import com.jarhax.prestige.Prestige;
import com.jarhax.prestige.api.Reward;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraftforge.common.util.Constants.NBT;

public class PlayerData {

    private static final String TAG_OWNER = "PlayerId";
    private static final String TAG_CONFIRMED = "Confirmed";
    private static final String TAG_UNCONFIRMED = "Unconfirmed";
    private static final String TAG_UNLOCKED = "Unlocked";

    private UUID playerId;
    private long confirmed;
    private long unconfirmed;
    private Set<Reward> unlockedRewards;

    public PlayerData (EntityPlayer player) {

        this(player.getPersistentID());
    }

    public PlayerData (UUID id) {

        this.playerId = id;
        this.confirmed = 0;
        this.unconfirmed = 0;
        this.unlockedRewards = new HashSet<>();
    }

    public PlayerData (NBTTagCompound tag) {

        this.playerId = tag.getUniqueId(TAG_OWNER);
        this.confirmed = tag.getLong(TAG_CONFIRMED);
        this.unconfirmed = tag.getLong(TAG_UNCONFIRMED);
        this.unlockedRewards = new HashSet<>();

        final NBTTagList tagList = tag.getTagList(TAG_UNLOCKED, NBT.TAG_STRING);

        for (int index = 0; index < tagList.tagCount(); index++) {

            final Reward reward = Prestige.REGISTRY.get(tagList.getStringTagAt(index));

            if (reward != null) {

                this.unlockedRewards.add(reward);
            }
        }
    }

    public NBTTagCompound save () {

        final NBTTagCompound tag = new NBTTagCompound();

        tag.setUniqueId(TAG_OWNER, this.playerId);
        tag.setLong(TAG_CONFIRMED, this.confirmed);
        tag.setLong(TAG_UNCONFIRMED, this.unconfirmed);

        final NBTTagList tagList = new NBTTagList();

        for (final Reward reward : this.unlockedRewards) {

            tagList.appendTag(new NBTTagString(reward.getIdentifier()));
        }

        tag.setTag(TAG_UNLOCKED, tagList);

        return tag;
    }

    public UUID getPlayerId () {

        return this.playerId;
    }

    public void setPlayerId (UUID playerId) {

        this.playerId = playerId;
    }

    public long getConfirmed () {

        return this.confirmed;
    }

    public void setConfirmed (long confirmed) {

        this.confirmed = confirmed;
    }

    public void addConfirmed (long amount) {

        this.confirmed += amount;
    }

    public void removeConfirmed (long amount) {

        this.confirmed -= amount;
    }

    public long getUnconfirmed () {

        return this.unconfirmed;
    }

    public void setUnconfirmed (long unconfirmed) {

        this.unconfirmed = unconfirmed;
    }

    public void addUnconfirmed (long amount) {

        this.unconfirmed += amount;
    }

    public void removeUnconfirmed (long amount) {

        this.unconfirmed -= amount;
    }

    public Set<Reward> getUnlockedRewards () {

        return this.unlockedRewards;
    }

    public void setUnlockedRewards (Set<Reward> unlockedRewards) {

        this.unlockedRewards = unlockedRewards;
    }
}
