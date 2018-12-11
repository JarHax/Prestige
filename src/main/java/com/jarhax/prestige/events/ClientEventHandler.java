package com.jarhax.prestige.events;


import com.jarhax.prestige.client.gui.GuiPrestige;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.client.settings.*;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.*;
import net.minecraftforge.fml.relauncher.*;
import org.lwjgl.input.Keyboard;

public class ClientEventHandler {
    
    @SubscribeEvent
    public void playerLogin(PlayerEvent.PlayerLoggedInEvent event) {
        //        if(Config.newWorldMode) {
        //            Minecraft.getMinecraft().displayGuiScreen(new GuiPrestige());
        //        }
    }
    
    public static KeyBinding open;
    
    public static void initKeys() {
        open = new KeyBinding("prestige.key.open", KeyConflictContext.UNIVERSAL, KeyModifier.ALT, Keyboard.KEY_F4, "prestige.key.category");
        ClientRegistry.registerKeyBinding(open);
    }
    
    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public void onKeyInput(InputEvent.KeyInputEvent event) {
        if(open.isPressed()) {
            Minecraft.getMinecraft().displayGuiScreen(new GuiPrestige());
        }
    }
    
}
