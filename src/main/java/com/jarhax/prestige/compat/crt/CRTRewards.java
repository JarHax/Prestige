package com.jarhax.prestige.compat.crt;

import com.jarhax.prestige.Prestige;
import crafttweaker.*;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.minecraft.CraftTweakerMC;
import crafttweaker.api.player.IPlayer;
import crafttweaker.api.world.IWorld;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.command.ServerCommandManager;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fml.common.FMLCommonHandler;
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
    public static void registerCommandReward(String name, String command) {
        CraftTweakerAPI.apply(new IAction() {
            @Override
            public void apply() {
                List<IReward> list = Prestige.REWARDS.getOrDefault(name, new LinkedList<>());
                list.add(new IReward() {
                    @Override
                    public void process(IWorld world, IPlayer player) {
                        player.executeCommand(command);
                    }
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
