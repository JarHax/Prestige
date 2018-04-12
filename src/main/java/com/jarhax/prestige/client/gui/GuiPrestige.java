package com.jarhax.prestige.client.gui;

import com.jarhax.prestige.Prestige;
import com.jarhax.prestige.api.Reward;
import com.jarhax.prestige.client.gui.objects.*;
import com.jarhax.prestige.client.utils.RenderUtils;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import java.io.IOException;
import java.util.*;

public class GuiPrestige extends GuiPrestigeBase {
    
    private GuiObjectBackGround backGround;
    private GuiObjectBorder border;
    
    
    private Map<GuiObjectReward, List<GuiObjectReward>> connections = new HashMap<>();
    
    @Override
    public void initGui() {
        
        this.guiWidth = 256;
        this.guiHeight = 256;
        super.initGui();
        this.left = this.width / 2 - this.guiWidth / 2;
        this.top = this.height / 2 - this.guiHeight / 2;
        guiObjects = new LinkedHashMap<>();
        this.backGround = new GuiObjectBackGround(this, this.left, this.top, this.guiWidth, this.guiHeight);
        this.border = new GuiObjectBorder(this, left, top, guiWidth, guiHeight);
        this.connections = new LinkedHashMap<>();
        for(Reward reward : Prestige.REGISTRY.values()) {
            if(reward.isPlaced())
                guiObjects.put(reward.getIdentifier(), new GuiObjectReward(this, reward.getX(), reward.getY(), reward));
        }
        for(Map.Entry<String, GuiObject> entry : this.guiObjects.entrySet()) {
            if(entry.getValue() instanceof GuiObjectReward) {
                for(Reward reward : ((GuiObjectReward) entry.getValue()).getReward().getParents()) {
                    GuiObjectReward rew = (GuiObjectReward) this.guiObjects.get(reward.getIdentifier());
                    if(!reward.isPlaced() || !rew.getReward().isPlaced()){
                        continue;
                    }
                    
                    List<GuiObjectReward> list = connections.getOrDefault(rew, new LinkedList<>());
                    list.add((GuiObjectReward) entry.getValue());
                    connections.put(rew, list);
                }
            }
        }
        // this.guiObjects.add(new GuiObjectTest(this, left + 20,top + 20,32,32));
        // this.guiObjects.add(new GuiMenu(this, this.left-55, this.top, 100, 100));
        
    }
    
    @Override
    public void updateScreen() {
        
        super.updateScreen();
        for(final GuiObject object : this.guiObjects.values()) {
            object.update();
        }
        this.guiObjects.values().forEach(object -> object.setVisible(true));
        for(final GuiObject object : this.guiObjects.values()) {
            if(!object.isAlwaysVisible()) {
                
                if(this.backGround.collides(object)) {
                    object.setVisible(true);
                } else {
                    object.setVisible(false);
                }
            }
        }
    }
    
    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        
        GlStateManager.pushMatrix();
        this.backGround.draw(this.left, this.top, mouseX, mouseY, partialTicks);
        this.mc.getTextureManager().bindTexture(new ResourceLocation("prestige", "textures/gui/gui_prestige_line.png"));
        for(final Map.Entry<GuiObjectReward, List<GuiObjectReward>> entry : this.connections.entrySet()) {
            final GuiObjectReward start = entry.getKey();
            if(!start.isVisible() || !start.shouldDrawLines()) {
                continue;
            }
            //drawConnections
            for(final GuiObjectReward end : entry.getValue()) {
                if(!end.isVisible() || !end.shouldDrawLines())
                    continue;
                GlStateManager.pushMatrix();
                final double angle = Math.atan2(end.getY() - start.getY(), end.getX() - start.getX()) * 180 / Math.PI;
                GL11.glTranslated(start.getX() + start.getWidth() / 2, start.getY() + start.getHeight() / 2, 0);
                GL11.glRotated(angle, 0, 0, 1);
                float length = (float) Math.sqrt((end.getX() - start.getX()) * (end.getX() - start.getX()) + (end.getY() - start.getY()) * (end.getY() - start.getY()));
                RenderUtils.drawTexturedModalRect(0, 0, RenderUtils.remap((float) (System.nanoTime() / 2000000000.0), 1, 0, 0, 16), 0, length, 4);
                GL11.glTranslated(-(start.getX() + start.getWidth() / 2), -(start.getY() + start.getHeight() / 2), 0);
                GlStateManager.popMatrix();
            }
        }
        //draw objects
        GlStateManager.pushMatrix();
        for(GuiObject object : guiObjects.values()) {
            if(object.isVisible()) {
                
                object.draw(this.left, this.top, mouseX, mouseY, partialTicks);
                
            }
        }
        //        GlStateManager.disableAlpha();
        GlStateManager.popMatrix();
        GL11.glTranslated(0, 0, 500);
        this.border.draw(left, top, mouseX, mouseY, partialTicks);
        GL11.glTranslated(0, 0, -500);
        GlStateManager.popMatrix();
        
        super.drawScreen(mouseX, mouseY, partialTicks);
    }
    
    @Override
    public void drawBackground(int tint) {
        
        super.drawBackground(tint);
    }
    
    @Override
    public void handleMouseInput() throws IOException {
        
        super.handleMouseInput();
    }
    
    @Override
    public void handleKeyboardInput() throws IOException {
        
        super.handleKeyboardInput();
    }
    
    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        
        super.mouseClicked(mouseX, mouseY, mouseButton);
        this.prevMX = mouseX;
        this.prevMY = mouseY;
        if(mouseButton == 0) {
            backGround.mouseClicked(mouseX, mouseY, mouseButton);
            for(final GuiObject object : this.guiObjects.values()) {
                object.mouseClicked(mouseX, mouseY, mouseButton);
            }
        }
        
    }
    
    @Override
    protected void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick) {
        
        super.mouseClickMove(mouseX, mouseY, clickedMouseButton, timeSinceLastClick);
        backGround.mouseClickMove(mouseX, mouseY, clickedMouseButton, timeSinceLastClick);
        for(final GuiObject object : this.guiObjects.values()) {
            object.mouseClickMove(mouseX, mouseY, clickedMouseButton, timeSinceLastClick);
        }
        
        this.prevMX = mouseX;
        this.prevMY = mouseY;
        
    }
    
    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state) {
        
        super.mouseReleased(mouseX, mouseY, state);
        this.prevMX = -1;
        this.prevMY = -1;
        backGround.mouseReleased(mouseX, mouseY, state);
        for(final GuiObject object : this.guiObjects.values()) {
            object.mouseReleased(mouseX, mouseY, state);
        }
        
    }
    
    
}
