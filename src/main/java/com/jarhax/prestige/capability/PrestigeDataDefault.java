package com.jarhax.prestige.capability;

import java.util.HashSet;
import java.util.Set;

import com.jarhax.prestige.api.Reward;

import net.minecraft.entity.player.EntityPlayer;

public class PrestigeDataDefault implements IPrestigeData {

    private final Set<Reward> rewards = new HashSet<>();
    private EntityPlayer player;

    @Override
    public void unlock (Reward reward) {

        this.rewards.add(reward);
    }

    @Override
    public void lock (Reward reward) {

        this.rewards.remove(reward);
    }

    @Override
    public Set<Reward> getAllUnlocked () {

        return this.rewards;
    }

    @Override
    public boolean canUnlock (Reward reward) {

        for (final Reward parent : reward.getParents()) {

            if (!this.hasUnlocked(parent)) {

                return false;
            }
        }

        return true;
    }

    @Override
    public boolean hasUnlocked (Reward reward) {

        return this.rewards.contains(reward);
    }

    @Override
    public void setPlayer (EntityPlayer player) {

        this.player = player;
    }

    @Override
    public EntityPlayer getPlayer () {

        return this.player;
    }
}