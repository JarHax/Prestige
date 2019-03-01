package com.jarhax.prestige.command;

import com.jarhax.prestige.Prestige;
import com.jarhax.prestige.api.Reward;
import com.jarhax.prestige.compat.crt.IReward;
import com.jarhax.prestige.config.Config;
import com.jarhax.prestige.data.*;
import com.jarhax.prestige.packet.*;
import crafttweaker.api.minecraft.CraftTweakerMC;
import net.darkhax.bookshelf.command.Command;
import net.minecraft.command.*;
import net.minecraft.entity.player.*;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.*;

import java.util.*;

public class CommandForcePrestige extends Command {
    
    @Override
    public String getName() {
        
        return "force";
    }
    
    @Override
    public String getUsage(ICommandSender sender) {
        
        return "/prestige force <enabled/disabled>";
    }
    
    @Override
    public int getRequiredPermissionLevel() {
        
        return 0;
    }
    
    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        if(args.length == 0) {
            sender.sendMessage(new TextComponentString(TextFormatting.RED + getUsage(sender) + TextFormatting.RESET));
            return;
        }
        boolean newValue = args[0].equalsIgnoreCase("enabled");
        if(!newValue && !args[0].equalsIgnoreCase("disabled")) {
            sender.sendMessage(new TextComponentTranslation("chat.prestige.force.unknown", args[0]));
            return;
        }
        
        if(sender instanceof EntityPlayerMP) {
            Prestige.LOG.info("Force Prestige mode requested by {}.", sender.getName());
            Prestige.prestigeEnabled = newValue;
            for(EntityPlayerMP player : server.getPlayerList().getPlayers()) {
                NBTTagCompound tag = player.getEntityData();
                NBTTagCompound data;
                if(!tag.hasKey(EntityPlayer.PERSISTED_NBT_TAG)) {
                    data = new NBTTagCompound();
                } else {
                    data = tag.getCompoundTag(EntityPlayer.PERSISTED_NBT_TAG);
                }
                data.setBoolean("prestigeEnabled", newValue);
                boolean oldValue = data.getBoolean("prestigeEnabled");
                if(oldValue != newValue) {
                    if(data.getBoolean("prestigeEnabled")) {
                        if(Config.newWorldMode)
                            if(!data.getBoolean("shownMenu")) {
                                Prestige.NETWORK.sendTo(new PacketOpenPrestigeGUI(), (EntityPlayerMP) player);
                                data.setBoolean("shownMenu", true);
                            }
                        Prestige.ENABLED_ACTIONS.forEach(enabledAction -> enabledAction.process(CraftTweakerMC.getIWorld(player.world), CraftTweakerMC.getIPlayer(player)));
                        
                        PlayerData playerData = GlobalPrestigeData.getPlayerData(player);
                        for(Reward reward : playerData.getUnlockedRewards()) {
                            if(data.hasKey(reward.getIdentifier())) {
                                continue;
                            }
                            List<IReward> list = Prestige.REWARDS.getOrDefault(reward.getIdentifier(), new ArrayList<>());
                            for(IReward iReward : list) {
                                iReward.process(CraftTweakerMC.getIWorld(player.world), CraftTweakerMC.getIPlayer(player));
                            }
                            data.setBoolean(reward.getIdentifier(), true);
                        }
                        
                    } else {
                        Prestige.DISABLED_ACTIONS.forEach(iDisabledAction -> iDisabledAction.process(CraftTweakerMC.getIWorld(player.world), CraftTweakerMC.getIPlayer(player)));
                    }
                }
                tag.setTag(EntityPlayer.PERSISTED_NBT_TAG, data);
            }
            
            Prestige.NETWORK.sendTo(new PacketSyncPrestige(GlobalPrestigeData.getPlayerData((EntityPlayer) sender)), (EntityPlayerMP) sender);
            sender.sendMessage(new TextComponentTranslation("chat.prestige.force.complete", newValue ? "Enabled" : "Disabled"));
        }
    }
}