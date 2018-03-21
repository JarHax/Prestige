package com.jarhax.prestige.capability;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;

public class ProviderPrestige implements ICapabilitySerializable<NBTTagCompound> {

    IPrestigeData instance = PrestigeDataHandler.CAPABILITY.getDefaultInstance();

    public ProviderPrestige (EntityPlayer player) {

        this.instance.setPlayer(player);
    }

    @Override
    public boolean hasCapability (Capability<?> capability, EnumFacing facing) {

        return capability == PrestigeDataHandler.CAPABILITY;
    }

    @Override
    public <T> T getCapability (Capability<T> capability, EnumFacing facing) {

        return this.hasCapability(capability, facing) ? PrestigeDataHandler.CAPABILITY.<T> cast(this.instance) : null;
    }

    @Override
    public NBTTagCompound serializeNBT () {

        return (NBTTagCompound) PrestigeDataHandler.CAPABILITY.getStorage().writeNBT(PrestigeDataHandler.CAPABILITY, this.instance, null);
    }

    @Override
    public void deserializeNBT (NBTTagCompound nbt) {

        PrestigeDataHandler.CAPABILITY.getStorage().readNBT(PrestigeDataHandler.CAPABILITY, this.instance, null, nbt);
    }
}