package com.moist.moistscommands.commands.homes;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;

import com.moist.moistscommands.MoistsCommands;
import com.moist.moistscommands.config.Config;
import com.moist.moistscommands.helper.Location;
import com.moist.moistscommands.helper.PlayerLocation;
import com.moist.moistscommands.helper.Teleport;

public class HomeCommand extends CommandBase {

    @Override
    public String getCommandName() {
        return "home";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "home <home name> (optional)";
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
            if (homeName.equals("list")) {
                HashMap<String, Location> playerHomes = PlayerLocation.listPlayerHomes(player);
                StringBuilder chatString = new StringBuilder();
                if (playerHomes == null || playerHomes.isEmpty()) {
                    chatString // if you have no homes for whatever reason, you cant list them.
                        .append(EnumChatFormatting.RED)
                        .append("Your Homes (")
                        .append(EnumChatFormatting.GOLD)
                        .append(0);
                    if (Config.maxHomes != 0) {
                        chatString.append(EnumChatFormatting.RED)
                            .append("/")
                            .append(EnumChatFormatting.GOLD)
                            .append(Config.maxHomes);
                    }
                    chatString.append(EnumChatFormatting.RED)
                        .append("): None found!");
                    sender.addChatMessage(new ChatComponentText(chatString.toString()));
                    return;
                }
                chatString.append("Your Homes (")
                    .append(EnumChatFormatting.GOLD)
                    .append(PlayerLocation.getHomeCount(player));
                if (Config.maxHomes != 0) {
                    chatString.append(EnumChatFormatting.RESET)
                        .append("/")
                        .append(EnumChatFormatting.GOLD)
                        .append(Config.maxHomes);
                }
                chatString.append(EnumChatFormatting.RESET)
                    .append("): ");
                for (String name : playerHomes.keySet()) {
                    if (!playerHomes.keySet()
                        .iterator()
                        .next()
                        .equals(name)) {
                        chatString.append(", ");
                    }
                    chatString.append(EnumChatFormatting.GOLD)
                        .append(name)
                        .append(EnumChatFormatting.RESET);
                }
                sender.addChatMessage(new ChatComponentText(chatString.toString()));
            } else {
                Location home = PlayerLocation.getHome(player, homeName);
                StringBuilder chatString = new StringBuilder();
                if (home == null) {
                    chatString.append(EnumChatFormatting.RED)
                        .append("Could not find home '")
                        .append(EnumChatFormatting.GOLD)
                        .append(homeName)
                        .append(EnumChatFormatting.RED)
                        .append("'!");
                    player.addChatMessage(new ChatComponentText(chatString.toString()));
                    return;
                }
                chatString.append("Teleporting to '")
                    .append(EnumChatFormatting.GOLD)
                    .append(homeName)
                    .append(EnumChatFormatting.RESET)
                    .append("'");
                Teleport.tpWithBack(player, home, chatString);
            }
        } catch (Exception e) {
            MoistsCommands.LOG.catching(e);
        }
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args) {
        try {
            EntityPlayerMP player = getCommandSenderAsPlayer(sender);
            HashSet<String> strings = new HashSet<>(
                PlayerLocation.storedLocations.get(player.getUniqueID()).playerHomes.keySet());
            strings.add("list");
            String[] homesArray = strings.toArray(new String[0]);
            Arrays.sort(homesArray, (k1, k2) -> {
                if (Objects.equals(k1, "list")) return -1;
                if (Objects.equals(k2, "list")) return 1;
                return k1.compareTo(k2);
            });
            return getListOfStringsMatchingLastWord(args, homesArray);
        } catch (Exception e) {
            MoistsCommands.LOG.catching(e);
        }
        return super.addTabCompletionOptions(sender, args);
    }
}
