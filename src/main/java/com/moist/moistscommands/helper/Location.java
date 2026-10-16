package com.moist.moistscommands.helper;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;

public class Location {

    public double posX;
    public double posY;
    public double posZ;

    public float pitch;
    public float yaw;

    public int dimension;

    public Location(EntityPlayer player) {
        this.posX = player.posX;
        this.posY = player.posY;
        this.posZ = player.posZ;
        this.pitch = player.rotationPitch;
        this.yaw = player.rotationYaw;
        this.dimension = player.dimension;
    }

    public Location(int posX, int posY, int posZ, float rotationPitch, float rotationYaw, int dimension) {
        this.posX = posX;
        this.posY = posY;
        this.posZ = posZ;
        this.pitch = rotationPitch;
        this.yaw = rotationYaw;
        this.dimension = dimension;
    }

    public Location(double posX, double posY, double posZ, float rotationPitch, float rotationYaw, int dimension) {
        this.posX = posX;
        this.posY = posY;
        this.posZ = posZ;
        this.pitch = rotationPitch;
        this.yaw = rotationYaw;
        this.dimension = dimension;
    }

    public static Location fromNBT(NBTTagCompound compound) {
        return new Location(
            compound.getDouble("pos_x"),
            compound.getDouble("pos_y"),
            compound.getDouble("pos_z"),
            compound.getFloat("pitch"),
            compound.getFloat("yaw"),
            compound.getInteger("dimension"));
    }

    public NBTTagCompound toNBT() {
        NBTTagCompound location = new NBTTagCompound();

        location.setDouble("pos_x", this.posX);
        location.setDouble("pos_y", this.posY);
        location.setDouble("pos_z", this.posZ);
        location.setFloat("pitch", this.pitch);
        location.setFloat("yaw", this.yaw);
        location.setInteger("dimension", this.dimension);

        return location;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("pos[x:" + this.posX + ", y:" + this.posY + ",  z:" + this.posZ + "], ");
        stringBuilder.append("rot[pitch:" + this.pitch + ", yaw:" + this.yaw + "], ");
        stringBuilder.append("dimension[" + this.dimension + "]");
        return stringBuilder.toString();
    }
}
