package com.jarhax.prestige.client.gui;

import com.jarhax.prestige.client.gui.objects.*;
import com.jarhax.prestige.client.utils.RenderUtils;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.Sys;
import org.lwjgl.opengl.GL11;

import java.io.IOException;
import java.util.*;

import static org.lwjgl.opengl.GL11.GL_STACK_OVERFLOW;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;

public class GuiPrestige extends GuiScreen {
    
    private int guiWidth;
    private int guiHeight;
    private int left;
    private int top;
    
    private int prevMX;
    private int prevMY;
    
    private List<GuiObject> guiObjects;
    private GuiObjectBackGround backGround;
    
    private GuiObject selectedObject;
    private final boolean editing;
    
    private Map<GuiObject, List<GuiObject>> connections = new HashMap<>();
    
    public GuiPrestige(boolean editing) {
        this.editing = editing;
    }
    
    @Override
    public void initGui() {
        
        this.guiWidth = 256;
        this.guiHeight = 256;
        super.initGui();
        this.left = this.width / 2 - this.guiWidth / 2;
        this.top = this.height / 2 - this.guiHeight / 2;
        this.guiObjects = new ArrayList<>();
        this.backGround = new GuiObjectBackGround(this, this.left, this.top, this.guiWidth, this.guiHeight);
        this.guiObjects.add(this.backGround);
        //        this.guiObjects.add(new GuiObjectTest(this, left + 20,top + 20,32,32));
        //        this.guiObjects.add(new GuiMenu(this, this.left-55, this.top, 100, 100));
        
    }
    
    @Override
    public void updateScreen() {
        
        super.updateScreen();
        for(final GuiObject object : this.guiObjects) {
            object.update();
        }
        this.guiObjects.stream().filter(o -> !o.equals(this.backGround)).forEach(object -> object.setVisible(true));
        for(GuiObject object : guiObjects) {
            if(!object.isAlwaysVisible()) {
                
                if(backGround.collides(object)) {
                    object.setVisible(true);
                } else {
                    object.setVisible(false);
                }
            }
        }
    }
    
    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        
        GlStateManager.pushMatrix();
        backGround.draw(left, top, mouseX, mouseY, partialTicks);
        mc.getTextureManager().bindTexture(new ResourceLocation("prestige", "textures/gui/gui_prestige_line.png"));
        for(Map.Entry<GuiObject, List<GuiObject>> entry : connections.entrySet()) {
            GuiObject start = entry.getKey();
            for(GuiObject end : entry.getValue()) {
                GlStateManager.pushMatrix();
                double angle = Math.atan2(end.getY() - start.getY(), end.getX() - start.getX()) * 180 / Math.PI;
//                angle = System.currentTimeMillis();
                GL11.glTranslated(start.getX() + (start.getWidth()/2), start.getY() + (start.getHeight()/2),0);
                GL11.glRotated(angle, 0,0,1);
                RenderUtils.drawTexturedModalRect(0, 0, RenderUtils.remap((float) (System.nanoTime()/1000000000.0),1,0,0,16),0, (float) Math.sqrt((end.getX()-start.getX())*(end.getX()-start.getX()) + (end.getY()-start.getY()) * (end.getY()-start.getY())),4);
                
                
                GL11.glTranslated(-(start.getX() + (start.getWidth()/2)), -(start.getY() + (start.getHeight()/2)),0);
                GlStateManager.popMatrix();
//
            }
        }
        for(int i = 0; i < this.guiObjects.size(); i++) {
            final GuiObject object = this.guiObjects.get(i);
            if(object.equals(backGround)){
                continue;
            }
            if(object.isVisible()) {
                if(object.equals(selectedObject)) {
                    GlStateManager.color(0, 1, 1, 1);
                }
                object.draw(this.left, this.top, mouseX, mouseY, partialTicks);
                if(object.equals(selectedObject)) {
                    GlStateManager.color(1, 1, 1, 1);
                }
            }
        }
        GlStateManager.popMatrix();
        
        
        if(editing) {
            fontRenderer.drawString("EDITING MODE!", left + 5, top + 5, 0xFFFF0000, false);
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
        
        if(editing) {
            GuiObject collided = null;
            for(GuiObject object : guiObjects) {
                if(object.equals(backGround)) {
                    continue;
                }
                if(object.collides(mouseX, mouseY, mouseX, mouseY)) {
                    collided = object;
                }
            }
            
            if(mouseButton == 2) {
                if(collided == null) {
                    GuiObjectTest e = new GuiObjectTest(this, mouseX - 16, mouseY - 16);
                    getList().add(e);
                    selectedObject = e;
                } else {
                    selectedObject = collided;
                }
            } else if(mouseButton == 1) {
                selectedObject = null;
            } else if(mouseButton == 0) {
                if(collided != null && selectedObject!=null) {
                    if(!collided.equals(selectedObject)) {
                        List<GuiObject> list = connections.getOrDefault(selectedObject, new ArrayList<>());
                        list.add(collided);
                        connections.put(selectedObject, list);
                    }
                }
            }
        }
        this.prevMX = mouseX;
        this.prevMY = mouseY;
        if(mouseButton == 0) {
            
            for(final GuiObject object : this.guiObjects) {
                object.mouseClicked(mouseX, mouseY, mouseButton);
            }
        }
        
    }
    
    @Override
    protected void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick) {
        
        super.mouseClickMove(mouseX, mouseY, clickedMouseButton, timeSinceLastClick);
        
        for(final GuiObject object : this.guiObjects) {
            object.mouseClickMove(mouseX, mouseY, clickedMouseButton, timeSinceLastClick);
        }
        
        this.prevMX = mouseX;
        this.prevMY = mouseY;
        
    }
    
    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state) {
        
        super.mouseReleased(mouseX, mouseY, state);
        this.prevMX = -1;
        this.prevMY = -1;
        for(final GuiObject object : this.guiObjects) {
            object.mouseReleased(mouseX, mouseY, state);
        }
        
    }
    
    public int getPrevMX() {
        
        return this.prevMX;
    }
    
    public int getPrevMY() {
        
        return this.prevMY;
    }
    
    public int getGuiWidth() {
        
        return this.guiWidth;
    }
    
    public int getGuiHeight() {
        
        return this.guiHeight;
    }
    
    public int getLeft() {
        
        return this.left;
    }
    
    public int getTop() {
        
        return this.top;
    }
    
    public List<GuiObject> getList() {
        
        return this.guiObjects;
    }
}
