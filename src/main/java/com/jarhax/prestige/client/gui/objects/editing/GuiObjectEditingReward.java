package com.jarhax.prestige.client.gui.objects.editing;

import com.jarhax.prestige.api.Reward;
import com.jarhax.prestige.client.gui.GuiPrestigeBase;
import com.jarhax.prestige.client.gui.objects.GuiObject;
import com.jarhax.prestige.client.utils.RenderUtils;
import net.minecraft.client.renderer.*;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import java.util.*;

public class GuiObjectEditingReward extends GuiObject {
    
    private static final ResourceLocation BACKGROUND = new ResourceLocation("prestige", "textures/gui/gui_prestige_icons.png");
    private final Reward reward;
    private int gridX;
    private int gridY;
    
    private boolean moving;
    
    public GuiObjectEditingReward(GuiPrestigeBase parent, int x, int y, int width, int height, Reward reward) {
        
        super(parent, x, y, width, height);
        this.reward = reward;
        setX(x);
        setY(y);
        
    }
    
    public GuiObjectEditingReward(GuiPrestigeBase parent, Reward reward) {
        
        super(parent, reward.getX(), reward.getY(), 32, 32);
        this.reward = reward;
        setX(reward.getX());
        setY(reward.getY());
        
    }
    
    public GuiObjectEditingReward(GuiPrestigeBase parent, int x, int y, Reward reward) {
        
        super(parent, x, y, 32, 32);
        this.reward = reward;
        setX(x);
        setY(y);
        
    }
    
    @Override
    public void update() {
        
        super.update();
    }
    
    @Override
    public void draw(int left, int top, int mouseX, int mouseY, float partialTicks) {
        
        this.mc.getTextureManager().bindTexture(BACKGROUND);
    
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(770, 771);
        if(!isPlaced()) {
            RenderUtils.drawTexturedModalRect(getX(), getY(), 0, 0, this.getWidth(), this.getHeight());
            renderIcon();
        } else {
            
            this.mc.getTextureManager().bindTexture(BACKGROUND);
            float offsetX = 0;
            float offsetY = 0;
            if(this.getX() < this.getParent().getLeft()) {
                offsetX = this.getParent().getLeft() - getX();
            }
            if(this.getY() < this.getParent().getTop()) {
                offsetY = this.getParent().getTop() - getY();
            }
            if(offsetX != 0 || offsetY != 0) {
                RenderUtils.drawTexturedModalRect(getX() + offsetX, getY() + offsetY, offsetX, offsetY, this.getWidth() - offsetX, this.getHeight() - offsetY);
            } else {
                if(this.getX() + getWidth() > this.getParent().getLeft() + this.getParent().getGuiWidth()) {
                    offsetX = (this.getParent().getLeft() + this.getParent().getGuiWidth()) - (getX() + getWidth());
                }
                if(this.getY() + getHeight() > this.getParent().getTop() + this.getParent().getGuiHeight()) {
                    offsetY = (this.getParent().getTop() + this.getParent().getGuiHeight()) - (getY() + getHeight());
                }
                if(offsetX != 0 || offsetY != 0) {
                    RenderUtils.drawTexturedModalRect(getX(), getY(), 0, 0, this.getWidth() + offsetX, this.getHeight() + offsetY);
                } else {
                    RenderUtils.drawTexturedModalRect(getX(), getY(), 0, 0, this.getWidth(), this.getHeight());
                }
                
            }
            if(getX() + 8 > getParent().getLeft() && getX() + getWidth() - 8 < getParent().getLeft() + getParent().getGuiWidth()) {
                if(getY() + 8 > getParent().getTop() && getY() + getHeight() - 8 < getParent().getTop() + getParent().getGuiHeight()) {
                    renderIcon();
                }
            }
            
            if(collides(mouseX, mouseY, mouseX, mouseY)) {
                drawText(mouseX, mouseY);
            }
        }
    }
    
    
    public void renderIcon() {
        GL11.glPushMatrix();
        RenderHelper.enableGUIStandardItemLighting();
        GL11.glTranslated(getX() + 8, getY() + 8, -50);
        mc.getRenderItem().renderItemIntoGUI(reward.getIcon(), 0, 0);
        GL11.glTranslated(-(getX() + 8), -(getY() + 8), 50);
        RenderHelper.disableStandardItemLighting();
        GL11.glPopMatrix();
    }
    
    
    public void drawText(int mouseX, int mouseY) {
        GL11.glPushMatrix();
        GlStateManager.translate(0, 0, 500);
        GlStateManager.disableAlpha();
        GlStateManager.enableLighting();
        List<String> text = new LinkedList<>();
        text.add(reward.getTitle());
        text.add("");
        text.add("- " + reward.getDescription());
        text.add("- costs: " + reward.getCost());
    
        getParent().drawHoveringText(text, mouseX, mouseY);
        GlStateManager.disableLighting();
        GlStateManager.enableAlpha();
        GL11.glPopMatrix();
    }
    
    
    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }
    
    @Override
    public void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick) {
        
        super.mouseClickMove(mouseX, mouseY, clickedMouseButton, timeSinceLastClick);
        addX(-(((float) this.getParent().getPrevMX() - mouseX)) / 1f);
        addY(-(((float) this.getParent().getPrevMY() - mouseY)) / 1f);
        
        setX(Math.round(x));
        setY(Math.round(y));
    }
    
    @Override
    public void mouseReleased(int mouseX, int mouseY, int state) {
        
        super.mouseReleased(mouseX, mouseY, state);
        
    }
    
    public Reward getReward() {
        return reward;
    }
    
    public boolean isPlaced() {
        return reward.isPlaced();
    }
    
    public void setPlaced(boolean placed) {
        reward.setPlaced(placed);
    }
    
    public int getGridX() {
        return gridX;
    }
    
    public void setGridX(int gridX) {
        this.gridX = gridX;
    }
    
    public int getGridY() {
        return gridY;
    }
    
    public void setGridY(int gridY) {
        this.gridY = gridY;
    }
    
    
    public boolean isMoving() {
        return moving;
    }
    
    public void setMoving(boolean moving) {
        this.moving = moving;
    }
    
    
    @Override
    public void setX(float x) {
        super.setX(x);
        reward.setX((int) x);
    }
    
    @Override
    public void setY(float y) {
        super.setY(y);
        reward.setY((int) y);
    }
    
    @Override
    public float getX() {
        return reward.getX();
    }
    
    @Override
    public float getY() {
        return reward.getY();
    }
}
