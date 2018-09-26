package com.jarhax.prestige.compat.crt;

import com.jarhax.prestige.Prestige;
import crafttweaker.*;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.minecraft.CraftTweakerMC;
import crafttweaker.api.player.IPlayer;
import crafttweaker.api.world.IWorld;
import crafttweaker.mc1120.command.MCCommandSender;
import crafttweaker.mc1120.server.ServerPlayer;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.command.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
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
                        EntityPlayer pl = (EntityPlayer) player.getInternal();
                        pl.getServer().getCommandManager().executeCommand(pl.getServer(), command.replace("@p", pl.getDisplayNameString()));
//                        player.getServer().getCommandManager().executeCommand(new MCCommandSender((ICommandSender) CraftTweakerAPI.server.getInternal()), command.replace("@p", player.getDisplayName()));
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
