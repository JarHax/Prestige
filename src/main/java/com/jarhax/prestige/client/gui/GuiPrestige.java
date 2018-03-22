package com.jarhax.prestige.client.gui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.jarhax.prestige.client.gui.objects.*;

import net.minecraft.client.gui.GuiScreen;

public class GuiPrestige extends GuiScreen {

    private int guiWidth;
    private int guiHeight;
    private int left;
    private int top;

    private int prevMX;
    private int prevMY;

    private List<GuiObject> guiObjects;
    private GuiObjectBackGround backGround;

    @Override
    public void initGui () {

        this.guiWidth = 256;
        this.guiHeight = 256;
        super.initGui();
        this.left = this.width / 2 - this.guiWidth / 2;
        this.top = this.height / 2 - this.guiHeight / 2;
        this.guiObjects = new ArrayList<>();
        this.backGround = new GuiObjectBackGround(this, this.left, this.top, this.guiWidth, this.guiHeight);
        this.guiObjects.add(this.backGround);
        this.guiObjects.add(new GuiObjectTest(this, left + 20,top + 20,32,32));
    
    }

    @Override
    public void updateScreen () {

        super.updateScreen();
        for (final GuiObject object : this.guiObjects) {
            object.update();
        }
        this.guiObjects.stream().filter(o -> !o.equals(this.backGround)).forEach(object -> object.setVisible(true));
        for(GuiObject object : guiObjects) {
            if(object.equals(backGround)){
                continue;
            }
            if(backGround.collides(object)){
                object.setVisible(true);
            }else{
                object.setVisible(false);
            }
        }
    }

    @Override
    public void drawScreen (int mouseX, int mouseY, float partialTicks) {

        for (int i = 0; i < this.guiObjects.size(); i++) {
            final GuiObject object = this.guiObjects.get(i);
            if (object.isVisible()) {
                object.draw(this.left, this.top, mouseX, mouseY, partialTicks);
            }
        }

        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    public void drawBackground (int tint) {

        super.drawBackground(tint);
    }

    @Override
    public void handleMouseInput () throws IOException {

        super.handleMouseInput();
    }

    @Override
    public void handleKeyboardInput () throws IOException {

        super.handleKeyboardInput();
    }

    @Override
    protected void mouseClicked (int mouseX, int mouseY, int mouseButton) throws IOException {

        super.mouseClicked(mouseX, mouseY, mouseButton);
        this.prevMX = mouseX;
        this.prevMY = mouseY;
        for (final GuiObject object : this.guiObjects) {
            object.mouseClicked(mouseX, mouseY, mouseButton);
        }

    }

    @Override
    protected void mouseClickMove (int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick) {

        super.mouseClickMove(mouseX, mouseY, clickedMouseButton, timeSinceLastClick);

        for (final GuiObject object : this.guiObjects) {
            object.mouseClickMove(mouseX, mouseY, clickedMouseButton, timeSinceLastClick);
        }

        this.prevMX = mouseX;
        this.prevMY = mouseY;

    }

    @Override
    protected void mouseReleased (int mouseX, int mouseY, int state) {

        super.mouseReleased(mouseX, mouseY, state);
        this.prevMX = -1;
        this.prevMY = -1;
        for (final GuiObject object : this.guiObjects) {
            object.mouseReleased(mouseX, mouseY, state);
        }

    }

    public int getPrevMX () {

        return this.prevMX;
    }

    public int getPrevMY () {

        return this.prevMY;
    }

    public int getGuiWidth () {

        return this.guiWidth;
    }

    public int getGuiHeight () {

        return this.guiHeight;
    }

    public int getLeft () {

        return this.left;
    }

    public int getTop () {

        return this.top;
    }

    public List<GuiObject> getList () {

        return this.guiObjects;
    }
}
