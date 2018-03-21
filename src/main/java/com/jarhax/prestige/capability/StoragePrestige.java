package com.jarhax.prestige.capability;

import com.jarhax.prestige.Prestige;
import com.jarhax.prestige.api.Reward;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.Constants.NBT;

public class StoragePrestige implements Capability.IStorage<IPrestigeData> {

    private static final String TAG = "UnlockedRewards";

    @Override
    public NBTBase writeNBT (Capability<IPrestigeData> capability, IPrestigeData instance, EnumFacing side) {

        final NBTTagCompound tag = new NBTTagCompound();

        final NBTTagList tagList = new NBTTagList();

        for (final Reward reward : instance.getAllUnlocked()) {

            tagList.appendTag(new NBTTagString(reward.getIdentifier()));
        }

        tag.setTag(TAG, tagList);

        return tag;
    }

    @Override
    public void readNBT (Capability<IPrestigeData> capability, IPrestigeData instance, EnumFacing side, NBTBase nbt) {

        final NBTTagCompound tag = (NBTTagCompound) nbt;

        final NBTTagList tagList = tag.getTagList(TAG, NBT.TAG_STRING);

        for (int index = 0; index < tagList.tagCount(); index++) {

            final Reward reward = Prestige.REGISTRY.get(tagList.getStringTagAt(index));

            if (reward != null) {

                instance.unlock(reward);
            }
        }
    }
}