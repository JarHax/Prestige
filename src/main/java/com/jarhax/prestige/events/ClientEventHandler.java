package com.jarhax.prestige.events;


import com.jarhax.prestige.client.gui.GuiPrestige;
import com.jarhax.prestige.config.Config;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;

public class ClientEventHandler {
    
    @SubscribeEvent
    public void playerLogin(PlayerEvent.PlayerLoggedInEvent event) {
//        if(Config.newWorldMode) {
//            Minecraft.getMinecraft().displayGuiScreen(new GuiPrestige());
//        }
    }
    
}
