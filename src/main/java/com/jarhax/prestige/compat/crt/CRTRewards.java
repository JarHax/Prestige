package com.jarhax.prestige.compat.crt;

import com.jarhax.prestige.Prestige;
import crafttweaker.*;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.potions.IPotionEffect;
import crafttweaker.mc1120.potions.MCPotionEfect;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.*;
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
    public static void registerTickingReward(String name, ITickingReward reward) {
        CraftTweakerAPI.apply(new IAction() {
            @Override
            public void apply() {
                List<IReward> list = Prestige.REWARDS.getOrDefault(name, new LinkedList<>());
                list.add(reward);
                Prestige.REWARDS.put(name, list);
            }
            
            @Override
            public String describe() {
                return "Adding ticking reward: " + name + ".";
            }
        });
    }
    
    @ZenMethod
    public static void registerPotionReward(String name, IPotionEffect potion) {
        CraftTweakerAPI.apply(new IAction() {
            @Override
            public void apply() {
                List<IReward> list = Prestige.REWARDS.getOrDefault(name, new LinkedList<>());
                list.add((ITickingReward) (world, player) -> {
                    if(!player.isPotionActive(potion.getPotion()) || player.getActivePotionEffect(potion.getPotion()).getDuration() < Math.ceil(potion.getDuration() / 2))
                        player.addPotionEffect(new MCPotionEfect(new PotionEffect((Potion) potion.getPotion().getInternal(), potion.getDuration(), potion.getAmplifier(), potion.isAmbient(), potion.doesShowParticles())));
                });
                Prestige.REWARDS.put(name, list);
            }
            
            @Override
            public String describe() {
                return "Adding potion reward: " + name + ".";
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
