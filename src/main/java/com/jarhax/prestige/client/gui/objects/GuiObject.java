package com.jarhax.prestige.client.gui.objects;

import java.awt.Rectangle;

import com.jarhax.prestige.client.gui.GuiPrestige;

import net.minecraft.client.Minecraft;

public abstract class GuiObject {

    protected boolean visible;
    protected boolean enabled;
    /**
     * Location relative to the parent gui's left
     */
    protected int x;
    /**
     * Location relative to the parent gui's top
     */
    protected int y;
    protected int width;
    protected int height;

    protected final GuiPrestige parent;

    protected Minecraft mc;

    public GuiObject (GuiPrestige parent, int x, int y, int width, int height) {

        this.parent = parent;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.visible = true;
        this.enabled = true;
        this.mc = Minecraft.getMinecraft();
    }

    public boolean collides (GuiObject other) {

        return this.collides(other.getX(), other.getY(), other.getWidth(), other.getHeight());
    }

    public boolean collides (int x1, int y1, int x2, int y2) {

        final Rectangle rect = new Rectangle(x1, y1, x2, y2);
        final Rectangle rect1 = new Rectangle(this.x, this.y, this.width, this.height);
        // TODO make this use custom code instead of Rectangle
        return rect.intersects(rect1);
    }

    public void update () {

    }

    public abstract void draw (int left, int top, int mouseX, int mouseY, float partialTicks);

    public void onClick (int mx, int my, int button) {

    }

    public void mouseClicked (int mouseX, int mouseY, int mouseButton) {

    }

    public void mouseClickMove (int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick) {

    }

    public void mouseReleased (int mouseX, int mouseY, int state) {

    }

    public boolean isVisible () {

        return this.visible;
    }

    public void setVisible (boolean visible) {

        this.visible = visible;
    }

    public boolean isEnabled () {

        return this.enabled;
    }

    public void setEnabled (boolean enabled) {

        this.enabled = enabled;
    }

    public int getX () {

        return this.x;
    }

    public void setX (int x) {

        this.x = x;
    }

    public int getY () {

        return this.y;
    }

    public void setY (int y) {

        this.y = y;
    }

    public int getWidth () {

        return this.width;
    }

    public void setWidth (int width) {

        this.width = width;
    }

    public int getHeight () {

        return this.height;
    }

    public void setHeight (int height) {

        this.height = height;
    }

    public GuiPrestige getParent () {

        return this.parent;
    }
}
