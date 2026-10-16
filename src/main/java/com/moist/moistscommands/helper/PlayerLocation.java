package com.moist.moistscommands.helper;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.UUID;

import javax.annotation.Nullable;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;

import com.moist.moistscommands.MoistsCommands;
import com.moist.moistscommands.config.Config;

public class PlayerLocation {

    public static HashMap<UUID, PlayerLocation> storedLocations;
    public static boolean hasBeenUpdated = false;

    public PlayerLocation(EntityPlayer player) {
        this.lastLocation = new Location(player);
        this.playerHomes = new HashMap<String, Location>();
    }

    public PlayerLocation(NBTTagCompound compound) {
        this.lastLocation = Location.fromNBT(compound.getCompoundTag("last_location"));
        this.playerHomes = new HashMap<String, Location>();
        NBTTagCompound playerHomes = compound.getCompoundTag("homes");
        for (String key : playerHomes.func_150296_c()) {
            this.playerHomes.put(key, Location.fromNBT(playerHomes.getCompoundTag(key)));
        }
    }

    public Location lastLocation;
    public HashMap<String, Location> playerHomes;

    public static void updateLastLocation(EntityPlayer player) {
        if (!storedLocations.containsKey(player.getPersistentID())) {
            storedLocations.put(player.getPersistentID(), new PlayerLocation(player));
        }
        storedLocations.get(player.getPersistentID()).lastLocation = new Location(player);
        hasBeenUpdated = true;
    }

    public NBTTagCompound toNBT() {
        NBTTagCompound compound = new NBTTagCompound();
        compound.setTag("last_location", lastLocation.toNBT());
        NBTTagCompound playerHomes = new NBTTagCompound();
        compound.setTag("homes", playerHomes);
        this.playerHomes.forEach(((string, location) -> { playerHomes.setTag(string, location.toNBT()); }));
        return compound;
    }

    public static void setLastLocation(EntityPlayer player, Location location) {
        if (!storedLocations.containsKey(player.getPersistentID())) {
            storedLocations.put(player.getPersistentID(), new PlayerLocation(player));
        }
        storedLocations.get(player.getPersistentID()).lastLocation = location;
        hasBeenUpdated = true;
    }

    public static Location getLastLocation(EntityPlayer player) {
        if (!storedLocations.containsKey(player.getPersistentID())) {
            return null;
        }
        return storedLocations.get(player.getPersistentID()).lastLocation;
    }

    public static Location getHome(EntityPlayer player, String name) {
        PlayerLocation location = storedLocations.get(player.getPersistentID());
        if (location == null) {
            return null;
        }
        if (location.playerHomes != null && location.playerHomes.containsKey(name)) {
            MoistsCommands.LOG.info(
                "Got home at {}",
                location.playerHomes.get(name)
                    .toString());
            return location.playerHomes.get(name);
        }
        return null;
    }

    public static boolean setHome(EntityPlayer player, String name, boolean ignore_limits) {
        if (!storedLocations.containsKey(player.getPersistentID())) {
            storedLocations.put(player.getPersistentID(), new PlayerLocation(player));
        }
        PlayerLocation loc = storedLocations.get(player.getPersistentID());
        Location home = new Location(player);
        MoistsCommands.LOG.info(
            "Set home at {}, array length {}",
            home.toString(),
            Arrays.toString(
                storedLocations.keySet()
                    .toArray()));
        if (ignore_limits || (loc.playerHomes.keySet()
            .toArray().length < Config.maxHomes || loc.playerHomes.containsKey(name))) {
            delHome(player, name);
            hasBeenUpdated = true;
            storedLocations.get(player.getPersistentID()).playerHomes.put(name, home);
            return true;
        }
        return false;
    }

    public static boolean delHome(EntityPlayer player, String name) {
        if (!storedLocations.containsKey(player.getPersistentID())) {
            return false;
        }
        hasBeenUpdated = true;
        return storedLocations.get(player.getPersistentID()).playerHomes.remove(name) != null;
    }

    public static int getHomeCount(EntityPlayer player) {
        if (!storedLocations.containsKey(player.getPersistentID())) {
            return 0;
        }
        return storedLocations.get(player.getPersistentID()).playerHomes.values()
            .toArray().length;
    }

    public static @Nullable HashMap<String, Location> listPlayerHomes(EntityPlayer player) {
        if (!storedLocations.containsKey(player.getPersistentID())) {
            return null;
        }
        return storedLocations.get(player.getPersistentID()).playerHomes;
    }

    public static void initializeLocations() {
        storedLocations = new HashMap<UUID, PlayerLocation>();
    }

    public static void readLocationFile(File file) {
        MoistsCommands.LOG.info(
            "{} {}",
            file.isFile() ? "Loading saved locations from" : "Creating new locations.dat at",
            file.getPath());
        PlayerLocation.initializeLocations();
        if (file.exists()) {
            try {
                NBTTagCompound loadedNBT = CompressedStreamTools.read(file);
                for (String key : loadedNBT.func_150296_c()) {
                    storedLocations.put(UUID.fromString(key), new PlayerLocation(loadedNBT.getCompoundTag(key)));
                }
            } catch (IOException e) {
                MoistsCommands.LOG.catching(e);
            }
        } else {
            saveLocationFile(file);
        }
    }

    public static void saveLocationFile(File file) {
        if (!hasBeenUpdated) {
            return;
        }
        MoistsCommands.LOG.info("Saving locations to file {}", file.getPath());
        try {
            NBTTagCompound nbtToSave = new NBTTagCompound();
            storedLocations
                .forEach((playerID, location) -> { nbtToSave.setTag(playerID.toString(), location.toNBT()); });
            CompressedStreamTools.safeWrite(nbtToSave, file);
            hasBeenUpdated = false;
        } catch (IOException e) {
            MoistsCommands.LOG.catching(e);
        }
    }

}
