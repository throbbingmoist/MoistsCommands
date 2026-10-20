package com.moist.moistscommands.commands;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StringUtils;
import net.minecraft.world.World;

import com.moist.moistscommands.MoistsCommands;
import com.moist.moistscommands.config.Config;
import com.moist.moistscommands.helper.DelayedEventHelper;
import com.moist.moistscommands.helper.Location;
import com.moist.moistscommands.helper.Teleport;

public class SpawnCommand extends CommandBase {

    @Override
    public String getCommandName() {
        return "spawn";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "";
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
            if (Config.spawnCooldown > 0
                && DelayedEventHelper.checkCommandCooldown(player, "spawn", world.getTotalWorldTime())) {
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
                if (Config.spawnCooldown > 0) DelayedEventHelper.addCommandCooldown(
                    player,
                    getCommandName(),
                    world.getTotalWorldTime() + (Config.homeCooldown * 20L));
            }
            ChunkCoordinates possibleSpawn = world.provider.getRandomizedSpawnPoint();
            chatString.append("Teleporting to spawn");
            Teleport.tpWithBack(
                player,
                new Location(possibleSpawn.posX + 0.5, possibleSpawn.posY + 0.5, possibleSpawn.posZ + 0.5, 0, 90, 0),
                (Config.secondsDelay * 20),
                chatString);
        } catch (Exception e) {
            MoistsCommands.LOG.catching(e);
        }
    }
}
