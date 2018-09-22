package com.jarhax.prestige.client.gui;

import com.jarhax.prestige.Prestige;
import com.jarhax.prestige.api.Reward;
import com.jarhax.prestige.client.gui.objects.*;
import com.jarhax.prestige.client.utils.RenderUtils;
import com.jarhax.prestige.data.*;
import com.jarhax.prestige.packet.PacketGiveRewards;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.opengl.GL11;

import java.io.IOException;
import java.util.*;

public class GuiPrestige extends GuiPrestigeBase {
    
    
    private GuiObjectBackGround backGround;
    private GuiObjectBorder border;
    
    private final int north = 0;
    private final int east = 1;
    private final int south = 2;
    private final int west = 3;
    
    
    public void generateRewards() {
        guiObjects.clear();
        LinkedList<Reward> values = new LinkedList<>(Prestige.REGISTRY.values());
        values.sort(Comparator.comparing(Reward::getIdentifier));
        for(Reward reward : values) {
            GuiObjectReward rew;
            if(reward.isPlaced()) {
                rew = new GuiObjectReward(this, reward);
                if(Prestige.clientPlayerData.hasReward(reward)) {
                    rew.setPurchased(true);
                }
                this.guiObjects.put(rew.getReward().getIdentifier(), rew);
            }
        }
    }
    
    @Override
    public void onGuiClosed() {
        super.onGuiClosed();
        String[] rewards = new String[getRewardsToGive().size()];
        for(int i = 0; i < rewards.length; i++) {
            rewards[i] = getRewardsToGive().get(i).getReward().getIdentifier();
        }
        Prestige.NETWORK.sendToServer(new PacketGiveRewards(rewards));
        
    }
    
    @Override
    public void initGui() {
        
        this.guiWidth = 256;
        this.guiHeight = 256;
        super.initGui();
        this.left = this.width / 2 - this.guiWidth / 2;
        this.top = this.height / 2 - this.guiHeight / 2;
        player = mc.player;
        this.guiObjects = new LinkedHashMap<>();
        this.backGround = new GuiObjectBackGround(this, this.left, this.top, this.guiWidth, this.guiHeight);
        this.border = new GuiObjectBorder(this, left, top, guiWidth, guiHeight);
        
        generateRewards();
        rewardsToGive = new LinkedList<>();
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
        this.guiObjects.values().forEach(object -> object.setVisible(true));
        for(GuiObjectReward object : guiObjects.values()) {
            if(!object.isPlaced()) {
                continue;
            }
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
        ScaledResolution resolution = new ScaledResolution(mc);
        int scale = resolution.getScaleFactor();
        GL11.glEnable(GL11.GL_SCISSOR_TEST);
        GL11.glScissor((left + 4) * scale, (top - 1 + 4) * scale, (guiWidth - 8) * scale, (guiHeight + 1 - 8) * scale);
        
        for(GuiObjectReward parent : guiObjects.values()) {
            Vec3d start = new Vec3d(parent.getX() + parent.getWidth() / 2, parent.getY() + parent.getHeight() / 2, 0);
            for(Reward child : parent.getReward().getChildren()) {
                GuiObjectReward childObject = getObject(child.getIdentifier());
                if(childObject == null) {
                    continue;
                }
                Vec3d end = new Vec3d(childObject.getX() + childObject.getWidth() / 2, childObject.getY() + childObject.getHeight() / 2, 0);
                GlStateManager.pushMatrix();
                final double angle = Math.atan2(childObject.getY() - parent.getY(), childObject.getX() - parent.getX()) * 180 / Math.PI;
                GL11.glTranslated(parent.getX() + parent.getWidth() / 2, parent.getY() + parent.getHeight() / 2, 0);
                GL11.glRotated(angle, 0, 0, 1);
                float length = (float) start.distanceTo(end);
                RenderUtils.drawTexturedModalRect(0, 0, RenderUtils.remap((float) (System.nanoTime() / 1000000000.0), 1, 0, 0, 16), 0, length, 4);
                GL11.glTranslated(-(parent.getX() + parent.getWidth() / 2), -(parent.getY() + parent.getHeight() / 2), 0);
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
        
        for(GuiObjectReward reward : guiObjects.values()) {
            if(reward.isVisible())
                if(reward.getY() >= this.getTop() && reward.getY() + reward.getHeight() <= this.getTop() + this.getGuiHeight()) {
                    reward.draw(this.left, this.top, mouseX, mouseY, partialTicks);
                }
        }
        
        GL11.glDisable(GL11.GL_SCISSOR_TEST);
        
        GlStateManager.popMatrix();
        GlStateManager.popMatrix();
        
        
        GlStateManager.pushMatrix();
        GL11.glTranslated(0, 0, 500);
        this.border.draw(left, top, mouseX, mouseY, partialTicks);
        fontRenderer.drawString("Prestige points: " + Prestige.clientPlayerData.getPrestige(), left + 5, top + 5, 0);
        GL11.glTranslated(0, 0, 0);
        GlStateManager.popMatrix();
        for(GuiObjectReward reward : guiObjects.values()) {
            if(reward.isVisible())
                if(reward.collides(mouseX, mouseY, mouseX, mouseY)) {
                    if(reward.getY() >= this.getTop() && reward.getY() + reward.getHeight() <= this.getTop() + this.getGuiHeight()) {
                        reward.drawText(mouseX, mouseY);
                    }
                }
        }
        
        boolean[] sides = new boolean[4];
        for(GuiObjectReward reward : guiObjects.values()) {
            if(!reward.isPlaced()) {
                continue;
            }
            if((sides[north] && sides[east] && sides[west] && sides[south])) {
                break;
            }
            if(!sides[west]) {
                if(reward.getX() < left) {
                    sides[west] = true;
                }
            }
            if(!sides[east]) {
                if(reward.getX() > left + guiWidth) {
                    sides[east] = true;
                }
            }
            if(!sides[north]) {
                if(reward.getY() < top) {
                    sides[north] = true;
                }
            }
            if(!sides[south]) {
                if(reward.getY() > top + guiHeight) {
                    sides[south] = true;
                }
            }
            
        }
        if((sides[north] || sides[east] || sides[west] || sides[south])) {
            this.mc.getTextureManager().bindTexture(new ResourceLocation("prestige", "textures/gui/gui_prestige_line.png"));
            if(sides[north]) {
                GlStateManager.pushMatrix();
                GlStateManager.translate(0, 0, 500);
                RenderUtils.drawLineUntextured((left + guiWidth / 2) - 4, top + 25, left + guiWidth / 2, top + 20, 0, 1, 1, 5);
                RenderUtils.drawLineUntextured((left + guiWidth / 2) + 4, top + 25, left + guiWidth / 2, top + 20, 0, 1, 1, 5);
                RenderUtils.drawLineUntextured((left + guiWidth / 2) - 4, top + 25, left + guiWidth / 2 + 4, top + 25, 0, 1, 1, 5);
                
                GlStateManager.translate(0, 0, -500);
                GlStateManager.popMatrix();
            }
            
            if(sides[south]) {
                GlStateManager.pushMatrix();
                GlStateManager.translate(0, 0, 500);
                RenderUtils.drawLineUntextured((left + guiWidth / 2) - 4, top + guiHeight - 25, left + guiWidth / 2, top + guiHeight - 20, 0, 1, 1, 5);
                RenderUtils.drawLineUntextured((left + guiWidth / 2) + 4, top + guiHeight - 25, left + guiWidth / 2, top + guiHeight - 20, 0, 1, 1, 5);
                RenderUtils.drawLineUntextured((left + guiWidth / 2) - 4, top + guiHeight - 25, left + guiWidth / 2 + 4, top + guiHeight - 25, 0, 1, 1, 5);
                GlStateManager.translate(0, 0, -500);
                GlStateManager.popMatrix();
            }
            
            if(sides[east]) {
                GlStateManager.pushMatrix();
                GlStateManager.translate(0, 0, 500);
                RenderUtils.drawLineUntextured(left + guiWidth - 25, top + guiHeight / 2 - 4, left + guiWidth - 20, top + guiHeight / 2, 0, 1, 1, 5);
                RenderUtils.drawLineUntextured(left + guiWidth - 25, top + guiHeight / 2 + 4, left + guiWidth - 20, top + guiHeight / 2, 0, 1, 1, 5);
                RenderUtils.drawLineUntextured(left + guiWidth - 25, top + guiHeight / 2 - 4, left + guiWidth - 25, top + (guiHeight / 2) + 4, 0, 1, 1, 5);
                GlStateManager.translate(0, 0, -500);
                GlStateManager.popMatrix();
            }
            
            if(sides[west]) {
                GlStateManager.pushMatrix();
                GlStateManager.translate(0, 0, 500);
                RenderUtils.drawLineUntextured(left + 25, top + guiHeight / 2 - 4, left + 20, top + guiHeight / 2, 0, 1, 1, 5);
                RenderUtils.drawLineUntextured(left + 25, top + guiHeight / 2 + 4, left + 20, top + guiHeight / 2, 0, 1, 1, 5);
                RenderUtils.drawLineUntextured(left + 25, top + guiHeight / 2 - 4, left + 25, top + (guiHeight / 2) + 4, 0, 1, 1, 5);
                GlStateManager.translate(0, 0, -500);
                GlStateManager.popMatrix();
            }
        }
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
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        super.keyTyped(typedChar, keyCode);
    }
    
    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        
        super.mouseClicked(mouseX, mouseY, mouseButton);
        this.prevMX = mouseX;
        this.prevMY = mouseY;
        
        if(mouseButton == 0) {
            boolean valid = true;
            
            for(final GuiObject object : this.guiObjects.values()) {
                if(object.collides(mouseX, mouseY, mouseX, mouseY)) {
                    valid = false;
                }
                object.mouseClicked(mouseX, mouseY, mouseButton);
                
            }
            
            if(valid)
                backGround.mouseClicked(mouseX, mouseY, mouseButton);
        }
        
        
    }
    
    
    @Override
    protected void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick) {
        
        super.mouseClickMove(mouseX, mouseY, clickedMouseButton, timeSinceLastClick);
        
        for(GuiObjectReward object : this.guiObjects.values()) {
            object.mouseClickMove(mouseX, mouseY, clickedMouseButton, timeSinceLastClick);
        }
        backGround.mouseClickMove(mouseX, mouseY, clickedMouseButton, timeSinceLastClick);
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
    
    public GuiObjectReward getObject(String identifier) {
        for(GuiObjectReward reward : guiObjects.values()) {
            if(reward.getReward().getIdentifier().equals(identifier)) {
                return reward;
            }
        }
        return null;
    }
}