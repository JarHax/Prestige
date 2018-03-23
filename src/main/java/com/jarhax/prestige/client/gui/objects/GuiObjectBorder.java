package com.jarhax.prestige.client.gui.objects;

import com.jarhax.prestige.client.gui.GuiPrestige;
import com.jarhax.prestige.client.utils.RenderUtils;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

public class GuiObjectBorder extends GuiObject {

    private static final ResourceLocation BACKGROUND_BORDER = new ResourceLocation("prestige", "textures/gui/gui_prestige_background_border.png");


    public GuiObjectBorder(GuiPrestige parent, int x, int y, int width, int height) {

        super(parent, x, y, width, height);
        this.setAlwaysVisible(true);
    }

    @Override
    public void draw (int left, int top, int mouseX, int mouseY, float partialTicks) {

        if (this.visible) {
            GlStateManager.pushMatrix();
            this.mc.getTextureManager().bindTexture(BACKGROUND_BORDER);
            RenderUtils.drawTexturedModalRect(this.getX(), this.getY(), 0, 0, this.getWidth(), this.getHeight());
            GlStateManager.popMatrix();
        }
    }
}
