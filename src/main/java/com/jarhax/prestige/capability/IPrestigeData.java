package com.jarhax.prestige.capability;

import java.util.Set;

import com.jarhax.prestige.api.Reward;

import net.minecraft.entity.player.EntityPlayer;

public interface IPrestigeData {

    boolean canUnlock (Reward reward);

    boolean hasUnlocked (Reward reward);

    void unlock (Reward reward);

    void lock (Reward reward);

    Set<Reward> getAllUnlocked ();

    void setPlayer (EntityPlayer player);

    EntityPlayer getPlayer ();
}