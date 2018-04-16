package com.jarhax.prestige.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.*;
import net.minecraft.client.renderer.GlStateManager;

public class GuiButtonTooltip extends GuiButton {
    
    public GuiScreen parent;
    public String text;
    
    public GuiButtonTooltip(int buttonId, int x, int y, String buttonText, GuiScreen parent, String text) {
        super(buttonId, x, y, buttonText);
        this.parent = parent;
        this.text = text;
    }
    
    public GuiButtonTooltip(int buttonId, int x, int y, int widthIn, int heightIn, String buttonText, GuiScreen parent, String text) {
        super(buttonId, x, y, widthIn, heightIn, buttonText);
        this.parent = parent;
        this.text = text;
    }
    
    @Override
    public void drawButton(Minecraft mc, int mouseX, int mouseY, float partialTicks) {
        super.drawButton(mc, mouseX, mouseY, partialTicks);
        
    }
    
    public void drawText(int mouseX, int mouseY) {
        if(visible) {
            GlStateManager.pushMatrix();
    
            GlStateManager.enableLighting();
            if(mouseX > x && mouseX < x + width) {
                if(mouseY > y && mouseY < y + height) {
                    parent.drawHoveringText(text, mouseX, mouseY);
                }
            }
            GlStateManager.disableLighting();
            GlStateManager.popMatrix();
        }
    }
}
