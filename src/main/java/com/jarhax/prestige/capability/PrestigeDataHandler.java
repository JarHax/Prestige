package com.jarhax.prestige.capability;

import com.jarhax.prestige.api.Reward;

import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@EventBusSubscriber
public class PrestigeDataHandler {

    @CapabilityInject(IPrestigeData.class)
    public static final Capability<IPrestigeData> CAPABILITY = null;

    public static IPrestigeData getStageData (EntityPlayer player) {

        return player != null && player.hasCapability(CAPABILITY, EnumFacing.DOWN) ? player.getCapability(CAPABILITY, EnumFacing.DOWN) : null;
    }

    @SubscribeEvent
    public static void attachCapabilities (AttachCapabilitiesEvent<Entity> event) {

        if (event.getObject() instanceof EntityPlayer) {
            event.addCapability(new ResourceLocation("prestige", "playerdata"), new ProviderPrestige((EntityPlayer) event.getObject()));
        }
    }

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public static void onEntityJoinWorld (EntityJoinWorldEvent event) {

        if (event.getEntity() instanceof EntityPlayerSP && !event.getEntity().isDead) {

            // TODO send request packet
        }
    }

    @SubscribeEvent
    public static void clonePlayer (PlayerEvent.Clone event) {

        final long time = System.currentTimeMillis();
        final IPrestigeData original = getStageData(event.getOriginal());
        final IPrestigeData clone = getStageData((EntityPlayer) event.getEntity());

        if (original != null && clone != null) {

            for (final Reward reward : original.getAllUnlocked()) {

                clone.unlock(reward);
            }

            // TODO LOG.info("Preserving data for " + event.getOriginal().getName() + ". Took "
            // + (System.currentTimeMillis() - time) + "ms.");
        }
    }
}