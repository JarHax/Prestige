package com.jarhax.prestige.client.gui.objects;

import com.jarhax.prestige.api.Reward;
import com.jarhax.prestige.client.gui.GuiPrestige;
import com.jarhax.prestige.client.utils.RenderUtils;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import java.util.*;

public class GuiObjectReward extends GuiObject {
    
    private static final ResourceLocation BACKGROUND = new ResourceLocation("prestige", "textures/gui/gui_prestige_icons.png");
    private final Reward reward;
    
    private boolean brought;
    
    private boolean drawLines = true;
    
    public GuiObjectReward(GuiPrestige parent, int x, int y, int width, int height, Reward reward) {
        
        super(parent, x, y, width, height);
        this.reward = reward;
        this.brought = false;
    }
    
    public GuiObjectReward(GuiPrestige parent, int x, int y, Reward reward) {
        
        super(parent, x, y, 32, 32);
        this.reward = reward;
        this.brought = false;
    }
    
    @Override
    public void update() {
        
        super.update();
        for(Reward reward1 : getReward().getParents()) {
            if(getParent().getGuiObjects().get(reward1.getIdentifier()).isBrought()) {
                enabled = true;
            }
        }
    }
    
    @Override
    public void draw(int left, int top, int mouseX, int mouseY, float partialTicks) {
        
        if(!isBrought()) {
            GlStateManager.color(0.5f, 0.5f, 0.5f, 1f);
        }
        this.mc.getTextureManager().bindTexture(BACKGROUND);
        float offsetX = 0;
        float offsetY = 0;
        if(this.getX() < this.getParent().getLeft()) {
            offsetX = this.getParent().getLeft() - getX();
        }
        if(this.getY() < this.getParent().getTop()) {
            offsetY = this.getParent().getTop() - getY();
        }
        drawLines = offsetX == 0 && offsetY == 0;
        if(offsetX != 0 || offsetY != 0) {
            RenderUtils.drawTexturedModalRect(getX() + offsetX, getY() + offsetY, offsetX, offsetY, this.getWidth() - offsetX, this.getHeight() - offsetY);
        } else {
            if(this.getX() + getWidth() > this.getParent().getLeft() + this.getParent().getGuiWidth()) {
                offsetX = (this.getParent().getLeft() + this.getParent().getGuiWidth()) - (getX() + getWidth());
            }
            if(this.getY() + getHeight() > this.getParent().getTop() + this.getParent().getGuiHeight()) {
                offsetY = (this.getParent().getTop() + this.getParent().getGuiHeight()) - (getY() + getHeight());
            }
            drawLines = offsetX == 0 && offsetY == 0;
            if(offsetX != 0 || offsetY != 0) {
                RenderUtils.drawTexturedModalRect(getX(), getY(), 0, 0, this.getWidth() + offsetX, this.getHeight() + offsetY);
            } else {
                RenderUtils.drawTexturedModalRect(getX(), getY(), 0, 0, this.getWidth(), this.getHeight());
            }
            
        }
        if(!isBrought()) {
            GlStateManager.color(1, 1, 1, 1f);
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
    
    
    public boolean shouldDrawLines() {
        return drawLines;
    }
    
    public void renderIcon() {
        GL11.glPushMatrix();
        GL11.glTranslated(getX() + 8, getY() + 8, -50);
        mc.getRenderItem().renderItemIntoGUI(reward.getIcon(), 0, 0);
        GL11.glTranslated(-(getX() + 8), -(getY() + 8), 50);
        GL11.glPopMatrix();
    }
    
    
    public void drawText(int mouseX, int mouseY) {
        GL11.glPushMatrix();
        GlStateManager.disableAlpha();
        GlStateManager.disableBlend();
        GlStateManager.enableLighting();
        List<String> text = new LinkedList<>();
        text.add(reward.getTitle());
        text.add("");
        text.add("- " + reward.getDescription());
        getParent().drawHoveringText(text, mouseX, mouseY);
        GlStateManager.disableLighting();
        GlStateManager.enableBlend();
        GlStateManager.enableAlpha();
        GL11.glPopMatrix();
    }
    
    
    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        
        super.mouseClicked(mouseX, mouseY, mouseButton);
        if(collides(mouseX, mouseY, mouseX, mouseY))
            brought = !brought;
    }
    
    @Override
    public void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick) {
        
        super.mouseClickMove(mouseX, mouseY, clickedMouseButton, timeSinceLastClick);
        // if(mouseX >= parent.getLeft() && mouseX <= parent.getLeft() + parent.getGuiWidth())
        // {
        // if(mouseY >= parent.getTop() && mouseY <= parent.getTop() + parent.getGuiHeight()) {
        this.x += (this.getParent().getPrevMX() - mouseX) / 1.5;
        this.y += (this.getParent().getPrevMY() - mouseY) / 1.5;
        // }
        // }
    }
    
    @Override
    public void mouseReleased(int mouseX, int mouseY, int state) {
        
        super.mouseReleased(mouseX, mouseY, state);
    }
    
    public Reward getReward() {
        return reward;
    }
    
    public boolean isBrought() {
        return brought;
    }
    
}
