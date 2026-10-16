package com.moist.moistscommands.commands.homes;

import java.util.List;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;

import com.moist.moistscommands.MoistsCommands;
import com.moist.moistscommands.helper.PlayerLocation;

public class DelhomeCommand extends CommandBase {

    @Override
    public String getCommandName() {
        return "delhome";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "delhome <home>";
    }

    public boolean canCommandSenderUseCommand(ICommandSender sender) {
        return sender instanceof EntityPlayer;
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
            if (PlayerLocation.delHome(player, homeName)) {
                chatString.append("Deleted home '")
                    .append(EnumChatFormatting.GOLD)
                    .append(homeName)
                    .append(EnumChatFormatting.RESET)
                    .append("'");
            } else {
                chatString.append(EnumChatFormatting.RED)
                    .append("Could not find home '")
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

    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args) {
        try {
            EntityPlayerMP player = getCommandSenderAsPlayer(sender);
            return getListOfStringsMatchingLastWord(
                args,
                PlayerLocation.storedLocations.get(player.getUniqueID()).playerHomes.keySet()
                    .toArray(new String[0]));
        } catch (Exception e) {
            MoistsCommands.LOG.catching(e);
        }
        return super.addTabCompletionOptions(sender, args);
    }
}
