package com.jarhax.prestige.data;

import java.util.HashSet;
import java.util.Set;

import com.google.gson.annotations.Expose;

public class PlayerData {

    @Expose
    private long confirmed;

    @Expose
    private long unconfirmed;

    @Expose
    private final Set<String> unlocked;

    public PlayerData () {

        this.confirmed = 0;
        this.unconfirmed = 0;
        this.unlocked = new HashSet<>();
    }

    public long getConfirmed () {

        return this.confirmed;
    }

    public long getUnconfirmed () {

        return this.unconfirmed;
    }

    public void add (long points, boolean confirmed) {

        if (confirmed) {

            this.confirmed += points;
        }

        else {

            this.unconfirmed += points;
        }
    }

    public void remove (long points, boolean confirmed) {

        if (confirmed) {

            this.confirmed -= points;
        }

        else {

            this.unconfirmed -= points;
        }
    }

    public Set<String> getUnlocked () {

        return this.unlocked;
    }

    public void unlock (String reward) {

        this.unlocked.add(reward);
    }
}
