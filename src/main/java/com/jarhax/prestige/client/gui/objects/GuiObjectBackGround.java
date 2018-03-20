package com.jarhax.prestige.client.gui.objects;


import com.jarhax.prestige.client.gui.GuiPrestige;
import com.jarhax.prestige.client.utils.RenderUtils;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;


public class GuiObjectBackGround extends GuiObject {
    
    
    private static final ResourceLocation BACKGROUND = new ResourceLocation("prestige", "textures/gui/gui_prestige_background.png");
    private static final ResourceLocation BACKGROUND_ALT = new ResourceLocation("prestige", "textures/gui/gui_prestige_background_alt.png");
    
    private float offsetX;
    private float offsetY;
    
    private float offsetAltX;
    private float offsetAltY;
    
    
    
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
            mc.getTextureManager().bindTexture(BACKGROUND_ALT);
            RenderUtils.drawTexturedModalRect(getX(), getY(), offsetAltX*2, offsetAltY*2, getWidth(), getHeight());
    
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
        offsetX += (getParent().getPrevMX() - mouseX)/3f;
        offsetY += (getParent().getPrevMY() - mouseY)/3f;
//        offsetX = Math.min(offsetX, 20);
//        offsetX = Math.max(offsetX, -20);
//        offsetY = Math.min(offsetY, 20);
//        offsetY = Math.max(offsetY, -20);
    
        offsetAltX += (getParent().getPrevMX() - mouseX)/2.5f;
        offsetAltY += (getParent().getPrevMY() - mouseY)/2.5f;
    
//        offsetAltX = Math.min(offsetX, 60);
//        offsetAltX = Math.max(offsetX, -60);
//        offsetAltY = Math.min(offsetY, 60);
//        offsetAltY = Math.max(offsetY, -60);
    
        //            }
        //        }
        
    }
    
    @Override
    public void mouseReleased(int mouseX, int mouseY, int state) {
        super.mouseReleased(mouseX, mouseY, state);
    }
}
