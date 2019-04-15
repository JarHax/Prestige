package com.jarhax.prestige.events;


import com.jarhax.prestige.Prestige;
import com.jarhax.prestige.config.Config;
import com.jarhax.prestige.packet.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.*;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.client.settings.*;
import net.minecraftforge.fml.client.config.GuiUtils;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.*;
import net.minecraftforge.fml.relauncher.*;
import org.lwjgl.input.Keyboard;

import java.util.Arrays;

public class ClientEventHandler {
    
    
    public static int gameTicks = 0;
    public static float partialTicks = 0;
    public static float deltaTime = 0;
    public static float totalTime = 0;
    
    private void calcDelta() {
        float oldTotal = totalTime;
        totalTime = gameTicks + partialTicks;
        deltaTime = totalTime - oldTotal;
    }
    
    @SubscribeEvent
    public void onTickRenderTick(TickEvent.RenderTickEvent event) {
        if(event.phase == TickEvent.Phase.START) {
            partialTicks = event.renderTickTime;
        } else {
            calcDelta();
        }
        
    }
    
    public static int lastGameTick;
    @SubscribeEvent
    public void clientTickEnd(TickEvent.ClientTickEvent event) {
        
        if(event.phase == TickEvent.Phase.END) {
            Minecraft mc = Minecraft.getMinecraft();
            
            GuiScreen gui = mc.currentScreen;
            if(gui == null || !gui.doesGuiPauseGame()) {
                gameTicks++;
                partialTicks = 0;
            }
            calcDelta();
            if(Prestige.clientPlayerData != null && Prestige.clientPlayerData.getRespTimer() >= 0 && totalTime%20==0) {
                Prestige.clientPlayerData.setRespTimer(Prestige.clientPlayerData.getRespTimer() - 1);
                lastGameTick = gameTicks;
            }
        }
    }
    
    
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
