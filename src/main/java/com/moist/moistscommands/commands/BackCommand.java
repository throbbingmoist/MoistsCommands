package com.moist.moistscommands.commands;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;

import com.moist.moistscommands.MoistsCommands;
import com.moist.moistscommands.helper.Location;
import com.moist.moistscommands.helper.PlayerLocation;
import com.moist.moistscommands.helper.Teleport;

public class BackCommand extends CommandBase {

    @Override
    public String getCommandName() {
        return "back";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "back";
    }

    public boolean canCommandSenderUseCommand(ICommandSender sender) {
        return sender instanceof EntityPlayer;
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        try {
            EntityPlayerMP player = getCommandSenderAsPlayer(sender);
            StringBuilder chatString = new StringBuilder();
            chatString.append("Returning to last location");
            Location lastLocation = PlayerLocation.getLastLocation(player);
            if (lastLocation != null) {
                Teleport.tpWithBack(player, lastLocation, chatString);
            }
        } catch (Exception e) {
            MoistsCommands.LOG.catching(e);
        }
    }
}
