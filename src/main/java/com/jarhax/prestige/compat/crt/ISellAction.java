package com.jarhax.prestige.compat.crt;

import crafttweaker.api.player.IPlayer;
import crafttweaker.api.world.IWorld;

public interface ISellAction {
    
    void process(IWorld world, IPlayer player);
    
}
