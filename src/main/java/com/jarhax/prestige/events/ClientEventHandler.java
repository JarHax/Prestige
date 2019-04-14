package com.jarhax.prestige.events;


import com.jarhax.prestige.Prestige;
import com.jarhax.prestige.config.Config;
import com.jarhax.prestige.packet.PacketSendPrestigeOpenCommand;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.*;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.client.settings.*;
import net.minecraftforge.fml.client.config.GuiUtils;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.relauncher.*;
import org.lwjgl.input.Keyboard;

import java.util.Arrays;

public class ClientEventHandler {
    
    public static KeyBinding open;
    
    public static void initKeys() {
        open = new KeyBinding("prestige.key.open", KeyConflictContext.UNIVERSAL, KeyModifier.NONE, Keyboard.KEY_P, "prestige.key.category");
        ClientRegistry.registerKeyBinding(open);
    }
    
    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public void onKeyInput(InputEvent.KeyInputEvent event) {
        if(open.isPressed()) {
            //¯\_(ツ)_/¯
            Prestige.NETWORK.sendToServer(new PacketSendPrestigeOpenCommand());
        }
    }
    
    
    @SubscribeEvent
    public void onGuiScreenInitGui(GuiScreenEvent.InitGuiEvent event) {
        final Minecraft minecraft = Minecraft.getMinecraft();
        if(event.getGui().getClass().getName().equals("com.bloodnbonesgaming.topography.client.gui.GuiCreateWorldTopography")) {
            event.getButtonList().add(new GuiButton(-1, 200, event.getGui().height - 20, 100, 20, "Prestige " + (Prestige.prestigeEnabled ? "Enabled" : "Disabled")) {
                public boolean mousePressed(Minecraft mc, int mouseX, int mouseY) {
                    if(super.mousePressed(mc, mouseX, mouseY)) {
                        Prestige.prestigeEnabled = !Prestige.prestigeEnabled;
                        this.displayString = "Prestige " + (Prestige.prestigeEnabled ? "Enabled" : "Disabled");
                        return true;
                    }
                    return false;
                }
                
                @Override
                public void drawButton(Minecraft mc, int mouseX, int mouseY, float partialTicks) {
                    super.drawButton(mc, mouseX, mouseY, partialTicks);
                    GlStateManager.pushMatrix();
                    GlStateManager.enableLighting();
                    if(mouseX > x && mouseX < x + width) {
                        if(mouseY > y && mouseY < y + height) {
                            GuiUtils.drawHoveringText(Arrays.asList(Config.prestigeButtonText), mouseX, mouseY, event.getGui().width, event.getGui().height, 250, mc.fontRenderer);
                        }
                    }
                    GlStateManager.disableLighting();
                    GlStateManager.popMatrix();
                }
            });
            
        } else if(event.getGui() instanceof GuiCreateWorld) {
            event.getButtonList().add(new GuiButton(-1, event.getGui().width / 2 - 75, 187 + 24, 150, 20, "Prestige " + (Prestige.prestigeEnabled ? "Enabled" : "Disabled")) {
                public boolean mousePressed(Minecraft mc, int mouseX, int mouseY) {
                    if(super.mousePressed(mc, mouseX, mouseY)) {
                        Prestige.prestigeEnabled = !Prestige.prestigeEnabled;
                        this.displayString = "Prestige " + (Prestige.prestigeEnabled ? "Enabled" : "Disabled");
                        return true;
                    }
                    return false;
                }
                
                @Override
                public void drawButton(Minecraft mc, int mouseX, int mouseY, float partialTicks) {
                    super.drawButton(mc, mouseX, mouseY, partialTicks);
                    GlStateManager.pushMatrix();
                    GlStateManager.enableLighting();
                    if(mouseX > x && mouseX < x + width) {
                        if(mouseY > y && mouseY < y + height) {
                            GuiUtils.drawHoveringText(Arrays.asList(Config.prestigeButtonText), mouseX, mouseY, event.getGui().width, event.getGui().height, 250, mc.fontRenderer);
                        }
                    }
                    GlStateManager.disableLighting();
                    GlStateManager.popMatrix();
                }
                
            });
        }
    }
}
