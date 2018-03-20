package com.jarhax.prestige.client.gui.objects;


import com.jarhax.prestige.client.gui.GuiPrestige;
import com.jarhax.prestige.client.utils.RenderUtils;
import net.minecraft.util.ResourceLocation;


public class GuiObjectBackGround extends GuiObject {
    
    
    private static final ResourceLocation BACKGROUND = new ResourceLocation("prestige", "textures/gui/gui_prestige_background.png");
    private int offsetX;
    private int offsetY;
    
    
    public GuiObjectBackGround(GuiPrestige parent, int x, int y, int width, int height) {
        super(parent, x, y, width, height);
        offsetX = x;
        offsetY = y;
    }
    
    @Override
    public void draw(int left, int top, int mouseX, int mouseY, float partialTicks) {
        
        if(visible) {
            mc.getTextureManager().bindTexture(BACKGROUND);
            RenderUtils.drawTexturedModalRect(getX(), getY(), offsetX, offsetY, getWidth(), getHeight());
        }
    }
    
    
    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }
    
    @Override
    public void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick) {
        super.mouseClickMove(mouseX, mouseY, clickedMouseButton, timeSinceLastClick);
        //        if(mouseX >= getX() && mouseX <= getX() + getWidth()) {
        //            if(mouseY >= getY() && mouseY <= getY() + getHeight()) {
        offsetX += (getParent().getPrevMX() - mouseX);
        offsetY += (getParent().getPrevMY() - mouseY);
    
    
        //            }
        //        }
        
    }
    
    @Override
    public void mouseReleased(int mouseX, int mouseY, int state) {
        super.mouseReleased(mouseX, mouseY, state);
    }
}
