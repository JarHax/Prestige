package com.jarhax.prestige.client.gui;

import com.google.common.collect.ImmutableList;
import com.jarhax.prestige.client.gui.objects.*;
import net.minecraft.client.gui.GuiScreen;

import java.io.IOException;
import java.util.*;

public class GuiPrestige extends GuiScreen {
    
    private int guiWidth;
    private int guiHeight;
    private int left;
    private int top;
    
    private int prevMX;
    private int prevMY;
    
    private List<GuiObject> guiObjects = new ArrayList<>();
    private GuiObjectBackGround backGround;
    
    @Override
    public void initGui() {
        guiWidth = 256;
        guiHeight = 256;
        super.initGui();
        left = width / 2 - guiWidth / 2;
        top = height / 2 - guiHeight / 2;
        guiObjects = new ArrayList<>();
        backGround = new GuiObjectBackGround(this, left, top, guiWidth, guiHeight);
        guiObjects.add(backGround);
        guiObjects.add(new GuiObjectTest(this, left+(mc.world.rand.nextInt(guiWidth)), top+(mc.world.rand.nextInt(guiHeight)), 20, 20));
        guiObjects.add(new GuiObjectTest(this, left+(mc.world.rand.nextInt(guiWidth)), top+(mc.world.rand.nextInt(guiHeight)), 20, 20));
        guiObjects.add(new GuiObjectTest(this, left+(mc.world.rand.nextInt(guiWidth)), top+(mc.world.rand.nextInt(guiHeight)), 20, 20));
        guiObjects.add(new GuiObjectTest(this, left+(mc.world.rand.nextInt(guiWidth)), top+(mc.world.rand.nextInt(guiHeight)), 20, 20));
    
    }
    
    @Override
    public void updateScreen() {
        super.updateScreen();
        for(GuiObject object : guiObjects) {
            object.update();
        }
        guiObjects.stream().filter(o -> !o.equals(backGround)).forEach(object -> {
            object.setVisible(backGround.collides(object));
        });
    }
    
    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        for(int i = 0; i < guiObjects.size(); i++) {
            GuiObject object = guiObjects.get(i);
            if(object.isVisible()) {
                object.draw(left, top, mouseX, mouseY, partialTicks);
            }
        }
        
        super.drawScreen(mouseX, mouseY, partialTicks);
    }
    
    @Override
    public void drawBackground(int tint) {
        super.drawBackground(tint);
    }
    
    @Override
    public void handleMouseInput() throws IOException {
        super.handleMouseInput();
    }
    
    @Override
    public void handleKeyboardInput() throws IOException {
        super.handleKeyboardInput();
    }
    
    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        prevMX = mouseX;
        prevMY = mouseY;
        for(GuiObject object : guiObjects) {
            object.mouseClicked(mouseX, mouseY, mouseButton);
        }
        
    }
    
    @Override
    protected void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick) {
        super.mouseClickMove(mouseX, mouseY, clickedMouseButton, timeSinceLastClick);
        
        for(GuiObject object : guiObjects) {
            object.mouseClickMove(mouseX, mouseY, clickedMouseButton, timeSinceLastClick);
        }
        
        prevMX = mouseX;
        prevMY = mouseY;
        
    }
    
    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state) {
        super.mouseReleased(mouseX, mouseY, state);
        prevMX = -1;
        prevMY = -1;
        for(GuiObject object : guiObjects) {
            object.mouseReleased(mouseX, mouseY, state);
        }
        
        
    }
    
    public int getPrevMX() {
        return prevMX;
    }
    
    public int getPrevMY() {
        return prevMY;
    }
    
    public int getGuiWidth() {
        return guiWidth;
    }
    
    public int getGuiHeight() {
        return guiHeight;
    }
    
    public int getLeft() {
        return left;
    }
    
    public int getTop() {
        return top;
    }
    
    public List<GuiObject> getList() {
        return guiObjects;
    }
}
