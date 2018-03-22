package com.jarhax.prestige.command;

import net.darkhax.bookshelf.command.CommandTree;
import net.minecraft.command.ICommandSender;

public class CommandPrestige extends CommandTree {

    public CommandPrestige () {

        this.addSubcommand(new CommandAdd());
        this.addSubcommand(new CommandRemove());
        this.addSubcommand(new CommandInfo());
        this.addSubcommand(new CommandOpen());
        this.addSubcommand(new CommandSync());
    }

    @Override
    public int getRequiredPermissionLevel () {

        return 0;
    }

    @Override
    public String getName () {

        return "prestige";
    }

    @Override
    public String getUsage (ICommandSender sender) {

        return "/prestige subcommand params";
    }
}
