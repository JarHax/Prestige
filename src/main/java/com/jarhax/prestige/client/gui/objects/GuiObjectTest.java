package com.jarhax.prestige.client.gui.objects;

import com.jarhax.prestige.client.gui.GuiPrestige;
import com.jarhax.prestige.client.utils.RenderUtils;

import net.minecraft.util.ResourceLocation;

public class GuiObjectTest extends GuiObject {

    private static final ResourceLocation BACKGROUND = new ResourceLocation("prestige", "textures/gui/gui_prestige_background1.png");
    private int offsetX;
    private int offsetY;

    public GuiObjectTest (GuiPrestige parent, int x, int y, int width, int height) {

        super(parent, x, y, width, height);
        this.offsetX = x;
        this.offsetY = y;
    }

    @Override
    public void update () {

        super.update();
    }

    @Override
    public void draw (int left, int top, int mouseX, int mouseY, float partialTicks) {

        this.mc.getTextureManager().bindTexture(BACKGROUND);
        RenderUtils.drawTexturedModalRect(this.getX(), this.getY(), this.offsetX, this.offsetY, this.getWidth(), this.getHeight());
    }

    @Override
    public void mouseClicked (int mouseX, int mouseY, int mouseButton) {

        super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    public void mouseClickMove (int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick) {

        super.mouseClickMove(mouseX, mouseY, clickedMouseButton, timeSinceLastClick);
        this.x += this.getParent().getPrevMX() - mouseX;
        this.y += this.getParent().getPrevMY() - mouseY;
        this.offsetX = this.x * 2;
        this.offsetY = this.y * 2;

    }

    @Override
    public void mouseReleased (int mouseX, int mouseY, int state) {

        super.mouseReleased(mouseX, mouseY, state);
    }
}
