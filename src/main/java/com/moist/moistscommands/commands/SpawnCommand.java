package com.moist.moistscommands.commands;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.World;

import com.moist.moistscommands.MoistsCommands;
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
            ChunkCoordinates possibleSpawn = world.provider.getRandomizedSpawnPoint();
            StringBuilder chatString = new StringBuilder();
            chatString.append("Teleporting to spawn");
            Teleport.tpWithBack(
                player,
                new Location(possibleSpawn.posX, possibleSpawn.posY, possibleSpawn.posZ, 0, 90, 0),
                chatString);
        } catch (Exception e) {
            MoistsCommands.LOG.catching(e);
        }
    }
}
