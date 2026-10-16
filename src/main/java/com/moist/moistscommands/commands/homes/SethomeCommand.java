package com.moist.moistscommands.commands.homes;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;

import com.moist.moistscommands.MoistsCommands;
import com.moist.moistscommands.helper.PlayerLocation;

public class SethomeCommand extends CommandBase {

    @Override
    public String getCommandName() {
        return "sethome";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "sethome <home>";
    }

    public boolean canCommandSenderUseCommand(ICommandSender sender) {
        return sender instanceof EntityPlayer;
    }

    public boolean canCommandSenderUseBypassLimit(ICommandSender sender) {
        return sender.canCommandSenderUseCommand(this.getRequiredPermissionLevel(), this.getCommandName());
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        String homeName = "home";
        if (args.length > 0) {
            homeName = args[0];
        }
        try {
            EntityPlayerMP player = getCommandSenderAsPlayer(sender);
            StringBuilder chatString = new StringBuilder();
            boolean success = PlayerLocation.setHome(player, homeName, canCommandSenderUseBypassLimit(sender));
            if (success) {
                chatString.append("Set new home '")
                    .append(EnumChatFormatting.GOLD)
                    .append(homeName)
                    .append(EnumChatFormatting.RESET)
                    .append("'");
            } else {
                chatString.append(EnumChatFormatting.RED)
                    .append("Too many homes! Could not set home '")
                    .append(EnumChatFormatting.GOLD)
                    .append(homeName)
                    .append(EnumChatFormatting.RED)
                    .append("'!");
            }
            sender.addChatMessage(new ChatComponentText(chatString.toString()));
        } catch (Exception e) {
            MoistsCommands.LOG.catching(e);
        }
    }
}
