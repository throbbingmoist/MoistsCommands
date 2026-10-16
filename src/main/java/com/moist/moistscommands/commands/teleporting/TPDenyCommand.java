package com.moist.moistscommands.commands.teleporting;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;

public class TPDenyCommand extends CommandBase {

    @Override
    public String getCommandName() {
        return "tpdeny";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "tpdeny";
    }

    public boolean canCommandSenderUseCommand(ICommandSender sender) {
        return sender instanceof EntityPlayer;
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {}
}
