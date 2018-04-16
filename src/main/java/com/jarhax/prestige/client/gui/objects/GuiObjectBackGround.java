package com.jarhax.prestige.client.gui.objects;

import com.jarhax.prestige.client.gui.*;
import com.jarhax.prestige.client.utils.RenderUtils;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

public class GuiObjectBackGround extends GuiObject {

    private static final ResourceLocation BACKGROUND = new ResourceLocation("prestige", "textures/gui/gui_prestige_background.png");
    private static final ResourceLocation BACKGROUND_ALT = new ResourceLocation("prestige", "textures/gui/gui_prestige_background_alt.png");

    private float offsetX;
    private float offsetY;

    private float offsetAltX;
    private float offsetAltY;

    public GuiObjectBackGround (GuiPrestigeBase parent, int x, int y, int width, int height) {

        super(parent, x, y, width, height);
        this.offsetX = x;
        this.offsetY = y;
        this.setAlwaysVisible(true);
    }

    @Override
    public void draw (int left, int top, int mouseX, int mouseY, float partialTicks) {

        if (this.visible) {
            this.mc.getTextureManager().bindTexture(BACKGROUND);
            RenderUtils.drawTexturedModalRect(this.getX() + 2, this.getY() + 2, this.offsetX, this.offsetY, this.getWidth() - 4, this.getHeight() - 4);

            GlStateManager.pushMatrix();
            GlStateManager.scale(2, 2, 2);
            this.mc.getTextureManager().bindTexture(BACKGROUND_ALT);
            GlStateManager.enableBlend();
            GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_COLOR);
            RenderUtils.drawTexturedModalRect((this.getX() + 2) / 2, (this.getY() + 2) / 2, this.offsetAltX, this.offsetAltY, (this.getWidth() - 2) / 2, (this.getHeight() - 2) / 2);
            GlStateManager.disableBlend();
            GlStateManager.scale(1, 1, 1);
            GlStateManager.popMatrix();

        }
    }

    @Override
    public void mouseClicked (int mouseX, int mouseY, int mouseButton) {

        super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    public void mouseClickMove (int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick) {

        super.mouseClickMove(mouseX, mouseY, clickedMouseButton, timeSinceLastClick);
        // if(mouseX >= parent.getLeft() && mouseX <= parent.getLeft() + parent.getGuiWidth())
        // {
        // if(mouseY >= parent.getTop() && mouseY <= parent.getTop() + parent.getGuiHeight()) {
        this.offsetX += (this.getParent().getPrevMX() - mouseX) / 3f;
        this.offsetY += (this.getParent().getPrevMY() - mouseY) / 3f;
        // offsetX = Math.min(offsetX, 20);
        // offsetX = Math.max(offsetX, -20);
        // offsetY = Math.min(offsetY, 20);
        // offsetY = Math.max(offsetY, -20);

        this.offsetAltX += (this.getParent().getPrevMX() - mouseX) / 2.75f;
        this.offsetAltY += (this.getParent().getPrevMY() - mouseY) / 2.75f;

        // offsetAltX = Math.min(offsetX, 60);
        // offsetAltX = Math.max(offsetX, -60);
        // offsetAltY = Math.min(offsetY, 60);
        // offsetAltY = Math.max(offsetY, -60);

        // }
        // }

    }

    @Override
    public void mouseReleased (int mouseX, int mouseY, int state) {

        super.mouseReleased(mouseX, mouseY, state);
    }
}
