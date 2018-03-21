package com.jarhax.prestige.api;

import net.minecraft.item.ItemStack;

public class Reward {

    private final String identifier;
    private final String title;
    private final String description;
    private final ItemStack icon;
    private final int x;
    private final int y;

    public Reward (String identifier, String title, int x, int y, ItemStack icon, String description) {

        if (identifier == null || identifier.isEmpty()) {

            throw new IllegalArgumentException("Prestige reward identifier can not be null or empty!");
        }

        else if (identifier.matches("\\S+")) {

            throw new IllegalArgumentException("Prestige reward identifiers can not contain white space!");
        }

        else if (icon == null || icon.isEmpty()) {

            throw new IllegalArgumentException("Prestige reward icon can not be a null or empty/aire item!");
        }

        this.identifier = identifier.toLowerCase();
        this.title = title;
        this.description = description;
        this.icon = icon;
        this.x = x;
        this.y = y;
    }

    public String getIdentifier () {

        return this.identifier;
    }

    public String getTitle () {

        return this.title;
    }

    public String getDescription () {

        return this.description;
    }

    public ItemStack getIcon () {

        return this.icon;
    }

    public int getX () {

        return this.x;
    }

    public int getY () {

        return this.y;
    }
}