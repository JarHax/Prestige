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
    public static void registerSellCommand(String name, String command) {
        CraftTweakerAPI.apply(new IAction() {
            @Override
            public void apply() {
                List<ISellAction> list = Prestige.SELL_ACTIONS.getOrDefault(name, new LinkedList<>());
                list.add((world, player) -> {
                    EntityPlayer pl = (EntityPlayer) player.getInternal();
                    pl.getServer().getCommandManager().executeCommand(pl.getServer(), command.replace("@p", pl.getDisplayNameString()));
                });
                Prestige.SELL_ACTIONS.put(name, list);
            }
            
            @Override
            public String describe() {
                return "Adding command sell: " + name + ".";
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
                return "Adding command reward: " + name + ".";
            }
        });
        
    }
    
    
    @ZenMethod
    public static void registerDisabledAction(IDisabledAction reward) {
        CraftTweakerAPI.apply(new IAction() {
            @Override
            public void apply() {
                Prestige.DISABLED_ACTIONS.add(reward);
            }
            
            @Override
            public String describe() {
                return "Adding disabled action.";
            }
        });
        
    }
    
    @ZenMethod
    public static void registerDisabledCommand(String command) {
        CraftTweakerAPI.apply(new IAction() {
            @Override
            public void apply() {
                Prestige.DISABLED_ACTIONS.add((world, player) -> {
                    EntityPlayer pl = (EntityPlayer) player.getInternal();
                    pl.getServer().getCommandManager().executeCommand(pl.getServer(), command.replace("@p", pl.getDisplayNameString()));
                });
            }
            
            @Override
            public String describe() {
                return "Adding disabled command.";
            }
        });
        
    }
    
    
    @ZenMethod
    public static void registerEnabledAction(IEnabledAction reward) {
        CraftTweakerAPI.apply(new IAction() {
            @Override
            public void apply() {
                Prestige.ENABLED_ACTIONS.add(reward);
            }
            
            @Override
            public String describe() {
                return "Adding enabled action.";
            }
        });
        
    }
    
    @ZenMethod
    public static void registerEnabledCommand(String command) {
        CraftTweakerAPI.apply(new IAction() {
            @Override
            public void apply() {
                Prestige.ENABLED_ACTIONS.add((world, player) -> {
                    EntityPlayer pl = (EntityPlayer) player.getInternal();
                    pl.getServer().getCommandManager().executeCommand(pl.getServer(), command.replace("@p", pl.getDisplayNameString()));
                });
            }
            
            @Override
            public String describe() {
                return "Adding enabled command.";
            }
        });
        
    }
    
    
    
    @ZenMethod
    public static void registerRewardCondition(String name, IRewardCondition condition){
        CraftTweakerAPI.apply(new IAction() {
            @Override
            public void apply() {
                List<IRewardCondition> list = Prestige.REWARD_CONDITIONS.getOrDefault(name, new LinkedList<>());
                list.add(condition);
                Prestige.REWARD_CONDITIONS.put(name, list);
            }
        
            @Override
            public String describe() {
                return "Adding reward condition for: " + name + ".";
            }
        });
    
    }
}
