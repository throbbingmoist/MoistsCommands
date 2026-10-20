package com.moist.moistscommands.commands;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StringUtils;
import net.minecraft.world.World;

import com.moist.moistscommands.MoistsCommands;
import com.moist.moistscommands.config.Config;
import com.moist.moistscommands.helper.DelayedEventHelper;
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
            World world = player.worldObj;
            StringBuilder chatString = new StringBuilder();
            if (Config.backCooldown > 0
                && DelayedEventHelper.checkCommandCooldown(player, getCommandName(), world.getTotalWorldTime())) {
                chatString.append(EnumChatFormatting.RED)
                    .append("/")
                    .append(this.getCommandName())
                    .append(" is on cooldown! (")
                    .append(
                        StringUtils.ticksToElapsedTime(
                            Math.toIntExact(DelayedEventHelper.commandCooldownTime(player, getCommandName()))))
                    .append(")");
                sender.addChatMessage(new ChatComponentText(chatString.toString()));
                return;
            } else {
                if (Config.backCooldown > 0) DelayedEventHelper.addCommandCooldown(
                    player,
                    getCommandName(),
                    world.getTotalWorldTime() + (Config.homeCooldown * 20L));
            }
            chatString.append("Returning to last location");
            Location lastLocation = PlayerLocation.getLastLocation(player);
            if (lastLocation != null) {
                Teleport.tpWithBack(player, lastLocation, (Config.secondsDelay * 20), chatString);
            }
        } catch (Exception e) {
            MoistsCommands.LOG.catching(e);
        }
    }
}
