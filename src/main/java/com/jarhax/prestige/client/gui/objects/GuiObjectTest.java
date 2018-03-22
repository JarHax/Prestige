package com.jarhax.prestige.client.gui.objects;

import com.jarhax.prestige.client.gui.GuiPrestige;
import com.jarhax.prestige.client.utils.RenderUtils;
import net.minecraft.util.ResourceLocation;

public class GuiObjectTest extends GuiObject {
    
    private static final ResourceLocation BACKGROUND = new ResourceLocation("prestige", "textures/gui/gui_prestige_icons.png");
    
    public GuiObjectTest(GuiPrestige parent, int x, int y, int width, int height) {
        
        super(parent, x, y, width, height);
    }
    
    
    public GuiObjectTest(GuiPrestige parent, int x, int y) {
        
        super(parent, x, y, 32, 32);
    }
    
    @Override
    public void update() {
        
        super.update();
    }
    
    @Override
    public void draw(int left, int top, int mouseX, int mouseY, float partialTicks) {
        
        this.mc.getTextureManager().bindTexture(BACKGROUND);
        RenderUtils.drawTexturedModalRect(this.getX(), this.getY(), 0, 0, this.getWidth(), this.getHeight());
    }
    
    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }
    
    @Override
    public void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick) {
        
        super.mouseClickMove(mouseX, mouseY, clickedMouseButton, timeSinceLastClick);
//        if(mouseX >= parent.getLeft() && mouseX <= parent.getLeft() + parent.getGuiWidth()) {
//            if(mouseY >= parent.getTop() && mouseY <= parent.getTop() + parent.getGuiHeight()) {
                this.x += (this.getParent().getPrevMX() - mouseX) / 1.5;
                this.y += (this.getParent().getPrevMY() - mouseY) / 1.5;
//            }
//        }
    }
    
    @Override
    public void mouseReleased(int mouseX, int mouseY, int state) {
        
        super.mouseReleased(mouseX, mouseY, state);
    }
}
