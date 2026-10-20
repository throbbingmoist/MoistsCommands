package com.moist.moistscommands.helper;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ChatComponentText;

import com.github.bsideup.jabel.Desugar;
import com.moist.moistscommands.config.Config;

public class Teleport {

    public static void tpWithBack(EntityPlayerMP player, Location location, Integer delay) {
        PlayerLocation.updateLastLocation(player);
        int targetDim = location.dimension;
        if (player.dimension != targetDim) {
            player.travelToDimension(targetDim);
        }
        if (delay.equals(0)) {
            player.playerNetServerHandler
                .setPlayerLocation(location.posX, location.posY, location.posZ, location.yaw, location.pitch);
        } else {
            DelayedEventHelper.scheduleEvent(() -> {
                if (player.playerNetServerHandler != null && !player.isDead) {
                    player.playerNetServerHandler
                        .setPlayerLocation(location.posX, location.posY, location.posZ, location.yaw, location.pitch);
                }
            }, (int) (player.worldObj.getTotalWorldTime() + delay));
        }
    }

    public static void tpWithBack(EntityPlayerMP player, Location location, Integer delay, StringBuilder builder) {
        PlayerLocation.updateLastLocation(player);
        int targetDim = location.dimension;
        if (player.dimension != targetDim) {
            player.travelToDimension(targetDim);
        }

        if (delay.equals(0)) {
            builder.append(".");
            player.addChatMessage(new ChatComponentText(builder.toString()));
            player.playerNetServerHandler
                .setPlayerLocation(location.posX, location.posY, location.posZ, location.yaw, location.pitch);
        } else {
            builder.append(" in ")
                .append(Config.secondsDelay)
                .append(" seconds.");
            player.addChatMessage(new ChatComponentText(builder.toString()));
            DelayedEventHelper.scheduleEvent(() -> {
                if (player.playerNetServerHandler != null && !player.isDead) {
                    player.playerNetServerHandler
                        .setPlayerLocation(location.posX, location.posY, location.posZ, location.yaw, location.pitch);
                }
            }, (int) (player.worldObj.getTotalWorldTime() + delay));
        }
    }

    @Desugar
    public record TeleportRequest(EntityPlayerMP sender, EntityPlayerMP target) {
        // Not yet used.
        // TODO:Make teleport request system.
    }
}
