package com.jarhax.prestige.client.gui.objects;

import com.jarhax.prestige.client.gui.GuiPrestigeBase;
import com.jarhax.prestige.client.utils.RenderUtils;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

public class GuiObjectBorder extends GuiObject {
    
    private static final ResourceLocation BACKGROUND_BORDER = new ResourceLocation("prestige", "textures/gui/gui_prestige_background_border.png");
    
    
    public GuiObjectBorder(GuiPrestigeBase parent, int x, int y, int width, int height) {
        
        super(parent, x, y, width, height);
        this.setAlwaysVisible(true);
    }
    
    @Override
    public void draw(int left, int top, int mouseX, int mouseY, float partialTicks) {
        
        if(this.visible) {
            GlStateManager.pushMatrix();
            this.mc.getTextureManager().bindTexture(BACKGROUND_BORDER);
            //corners
            RenderUtils.drawTexturedModalRect(this.getX(), this.getY(), 0, 0, 16, 16, 16,16);
            RenderUtils.drawTexturedModalRect(this.getX(), this.getY()+16, 0, 16, 16, 256-32, 16,getHeight()-32);
            RenderUtils.drawTexturedModalRect(this.getX(), this.getY()+getHeight()-16, 0, 256-16, 16, 16, 16, 16);
            RenderUtils.drawTexturedModalRect(this.getX()+getWidth()-16, this.getY(), 256-16, 0, 16, 16, 16,16);
            RenderUtils.drawTexturedModalRect(this.getX()+getWidth()-16, this.getY()+16, 256-16, 16, 16, 256-32, 16,getHeight()-32);
            RenderUtils.drawTexturedModalRect(this.getX()+getWidth()-16, this.getY()+getHeight()-16, 256-16 , 256-16, 16, 16, 16, 16);
            RenderUtils.drawTexturedModalRect(this.getX()+16, this.getY(), 16, 0, 256-32, 16, this.getWidth()-32, 16);
            RenderUtils.drawTexturedModalRect(this.getX()+16, this.getY()+getHeight()-16, 16, 256-16, 256-32, 16, this.getWidth()-32, 16);
            //
            
//
//            RenderUtils.drawTexturedModalRect(this.getX(), this.getY(), 0, 0, 16, 16, this.getWidth(), this.getHeight());
            
            //            RenderUtils.drawTexturedModalRect(this.getX(), this.getY(), 0, 0, 256,256,this.getWidth(), this.getHeight());
            GlStateManager.popMatrix();
        }
    }
}
