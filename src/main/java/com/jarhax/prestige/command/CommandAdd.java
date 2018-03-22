package com.jarhax.prestige.command;

import org.apache.commons.lang3.math.NumberUtils;

import com.jarhax.prestige.data.GlobalPrestigeData;
import com.jarhax.prestige.data.PlayerData;

import net.darkhax.bookshelf.command.Command;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;

public class CommandAdd extends Command {

    @Override
    public String getName () {

        return "add";
    }

    @Override
    public String getUsage (ICommandSender sender) {

        return "/add <player> <amount> [source-id]";
    }

    @Override
    public void execute (MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {

        if (args.length > 1 && args.length < 4 && NumberUtils.isParsable(args[1])) {

            final EntityPlayer player = getPlayer(server, sender, args[0]);

            if (player != null) {

                final PlayerData data = GlobalPrestigeData.getPlayerData(player);

                // Handles the source parameter
                if (args.length == 3) {

                    // Player already has source, so they will get a message and then stop
                    // executing.
                    if (data.hasSource(args[2])) {

                        // Send the sender an error message.
                        if (sender != player) {

                            sender.sendMessage(new TextComponentTranslation("chat.prestige.hassource.sender", player.getName(), args[2]).setStyle(new Style().setColor(TextFormatting.RED)));
                        }

                        // Send the player their error message.
                        sender.sendMessage(new TextComponentTranslation("chat.prestige.hassource.reciever", args[2]).setStyle(new Style().setColor(TextFormatting.RED)));
                        return;
                    }

                    // Player doesn't have source, add it.
                    data.addSource(args[2]);
                }

                // Add points to the players data and save.
                data.addPrestige(Long.parseLong(args[1]));
                GlobalPrestigeData.save(player);

                // Send the sender a confirmation message.
                if (sender != player) {

                    sender.sendMessage(new TextComponentTranslation("chat.prestige.added.sender", player.getName(), args[1]).setStyle(new Style().setColor(TextFormatting.GREEN)));
                }

                // Send the player a confirmation message.
                sender.sendMessage(new TextComponentTranslation("chat.prestige.added.reciever", args[1]).setStyle(new Style().setColor(TextFormatting.GREEN)));
            }
        }
    }
}
