package com.jarhax.prestige.client.gui.objects.editing;

import com.jarhax.prestige.client.gui.*;
import com.jarhax.prestige.client.gui.objects.GuiObject;
import net.minecraft.client.renderer.*;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import org.lwjgl.opengl.GL11;

import java.util.List;

public class GuiObjectItemStack extends GuiObject {
    
    private static final ResourceLocation BACKGROUND = new ResourceLocation("prestige", "textures/gui/gui_prestige_icons.png");
    private final ItemStack stack;
    private int gridX;
    private int gridY;
    
    public GuiObjectItemStack(GuiPrestigeBase parent, int x, int y, int width, int height, ItemStack stack) {
        
        super(parent, x, y, width, height);
        this.stack = stack;
        setX(x);
        setY(y);
        
    }
    
    public GuiObjectItemStack(GuiPrestigeBase parent, int x, int y, ItemStack stack) {
        
        super(parent, x, y, 18, 18);
        this.stack = stack;
        setX(x);
        setY(y);
        
    }
    
    @Override
    public void update() {
        
        super.update();
    }
    
    @Override
    public void draw(int left, int top, int mouseX, int mouseY, float partialTicks) {
        
        //        this.mc.getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(770, 771);
        //        RenderUtils.drawTexturedModalRect(getX(), getY(), 0, 0, this.getWidth(), this.getHeight());
        renderStack();
    }
    
    
    public void renderStack() {
        GL11.glPushMatrix();
        RenderHelper.enableGUIStandardItemLighting();
        GL11.glTranslated(getX(), getY(), -50);
        mc.getRenderItem().renderItemIntoGUI(stack, 0, 0);
        GL11.glTranslated(-(getX()), -(getY()), 50);
        RenderHelper.disableStandardItemLighting();
        GL11.glPopMatrix();
    }
    
    
    public void drawText(int mouseX, int mouseY) {
        GL11.glPushMatrix();
        GlStateManager.translate(0, 0, 500);
        GlStateManager.disableAlpha();
        GlStateManager.enableLighting();
        List<String> tooltip = stack.getTooltip(parent.player, ITooltipFlag.TooltipFlags.NORMAL);
        
        //TODO would be cool if this also highlighted case specific stuff
        tooltip.set(0, tooltip.get(0).replaceAll(((GuiPrestigeEditing) parent).getFieldFilter().getText(), TextFormatting.YELLOW + ((GuiPrestigeEditing) parent).getFieldFilter().getText() + TextFormatting.RESET));
        getParent().drawHoveringText(tooltip, mouseX, mouseY);
        GlStateManager.disableLighting();
        GlStateManager.enableAlpha();
        GL11.glPopMatrix();
    }
    
    
    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        
        super.mouseClicked(mouseX, mouseY, mouseButton);
        if(collides(mouseX, mouseY)) {
            if(parent instanceof GuiPrestigeEditing) {
                ((GuiPrestigeEditing) parent).selectedStack = this;
                
            }
        }
    }
    
    @Override
    public void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick) {
        
        super.mouseClickMove(mouseX, mouseY, clickedMouseButton, timeSinceLastClick);
    }
    
    @Override
    public void mouseReleased(int mouseX, int mouseY, int state) {
        
        super.mouseReleased(mouseX, mouseY, state);
        
    }
    
    public ItemStack getStack() {
        return stack;
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
    
}
