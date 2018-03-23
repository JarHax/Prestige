package com.jarhax.prestige.api;

import java.util.HashSet;
import java.util.Set;

import net.minecraft.item.ItemStack;

public class Reward {

    private final String identifier;
    private final String title;
    private final String description;
    private final ItemStack icon;
    private final int x;
    private final int y;
    private final int cost;

    private final Set<Reward> parents = new HashSet<>();
    private final Set<Reward> children = new HashSet<>();

    public Reward (String identifier, String title, int x, int y, int cost, ItemStack icon, String description) {

        if (identifier == null || identifier.isEmpty()) {

            throw new IllegalArgumentException("Prestige reward identifier can not be null or empty!");
        }

        else if (identifier.matches("\\s+")) {

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
        this.cost = cost;
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

    public int getCost () {

        return this.cost;
    }

    public Set<Reward> getChildren () {

        return this.children;
    }

    public void addChild (Reward child) {

        if (this.getChildren().contains(child)) {

            throw new IllegalArgumentException(String.format("The reward %s can not be a child of %s because it is a parent of %s.", child.getIdentifier(), this.identifier, this.identifier));
        }

        this.children.add(child);
    }

    public Set<Reward> getParents () {

        return this.parents;
    }

    public void addParent (Reward parent) {

        if (this.children.contains(parent)) {

            throw new IllegalArgumentException(String.format("The reward %s can not be a parent of %s because it is a child of %s.", parent.getIdentifier(), this.identifier, this.identifier));
        }

        this.parents.add(parent);
        parent.addChild(this);
    }
}