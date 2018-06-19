package com.jarhax.prestige.compat.crt;

import com.jarhax.prestige.Prestige;
import crafttweaker.*;
import crafttweaker.annotations.ZenRegister;
import stanhebben.zenscript.annotations.*;

import java.util.*;


@ZenRegister
@ZenClass("mods.prestige.Rewards")
public class CRTRewards {
    
    @ZenMethod
    public static void registerReward(String name, IReward reward) {
        CraftTweakerAPI.apply(new IAction() {
            @Override
            public void apply() {
                List<IReward> list = Prestige.REWARDS.getOrDefault(name, new LinkedList<>());
                list.add(reward);
                Prestige.REWARDS.put(name, list);
            }
            
            @Override
            public String describe() {
                return "Adding reward: " + name + ".";
            }
        });
        
    }
}
