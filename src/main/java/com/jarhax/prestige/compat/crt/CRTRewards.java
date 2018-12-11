package com.jarhax.prestige.compat.crt;

import com.jarhax.prestige.Prestige;
import crafttweaker.*;
import crafttweaker.annotations.ZenRegister;
import net.minecraft.entity.player.EntityPlayer;
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
    
    @ZenMethod
    public static void registerSellAction(String name, ISellAction reward) {
        CraftTweakerAPI.apply(new IAction() {
            @Override
            public void apply() {
                List<ISellAction> list = Prestige.SELL_ACTIONS.getOrDefault(name, new LinkedList<>());
                list.add(reward);
                Prestige.SELL_ACTIONS.put(name, list);
            }
            
            @Override
            public String describe() {
                return "Adding sell action: " + name + ".";
            }
        });
        
    }
    
    
    
    @ZenMethod
    public static void registerCommandReward(String name, String command) {
        CraftTweakerAPI.apply(new IAction() {
            @Override
            public void apply() {
                List<IReward> list = Prestige.REWARDS.getOrDefault(name, new LinkedList<>());
                list.add((world, player) -> {
                    EntityPlayer pl = (EntityPlayer) player.getInternal();
                    pl.getServer().getCommandManager().executeCommand(pl.getServer(), command.replace("@p", pl.getDisplayNameString()));
                });
                Prestige.REWARDS.put(name, list);
            }
            
            @Override
            public String describe() {
                return "Adding reward: " + name + ".";
            }
        });
        
    }
}
