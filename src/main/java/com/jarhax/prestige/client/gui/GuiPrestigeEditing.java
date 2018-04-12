package com.jarhax.prestige.client.gui;

import com.jarhax.prestige.Prestige;
import com.jarhax.prestige.api.Reward;
import com.jarhax.prestige.client.gui.objects.*;
import com.jarhax.prestige.client.gui.objects.editing.*;
import com.jarhax.prestige.client.utils.RenderUtils;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.*;
import org.lwjgl.opengl.GL11;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class GuiPrestigeEditing extends GuiPrestigeBase {
    
    
    private GuiObjectBackGround backGround;
    private GuiObjectBorder border;
    private Map<GuiObjectEditingReward, List<GuiObjectEditingReward>> connections = new HashMap<>();
    private List<GuiObjectEditingReward> unplacedRewards;
    private GuiObjectEditingReward selectedReward;
    private int yOff = 0;
    private GuiObjectEditingReward editingReward;
    
    private GuiObjectInformation informationPanel;
    
    @Override
    public void initGui() {
        
        this.guiWidth = 256;
        this.guiHeight = 256;
        super.initGui();
        this.left = this.width / 2 - this.guiWidth / 2;
        this.top = this.height / 2 - this.guiHeight / 2;
        this.guiObjects = new LinkedHashMap<>();
        unplacedRewards = new LinkedList<>();
        this.backGround = new GuiObjectBackGround(this, this.left, this.top, this.guiWidth, this.guiHeight);
        this.border = new GuiObjectBorder(this, left, top, guiWidth, guiHeight);
        this.informationPanel = new GuiObjectInformation(this, left + guiWidth + 5, top + 4, 100, guiHeight - 4);
        this.connections = new LinkedHashMap<>();
        int offX = 0;
        int offY = 0;
        
        for(Reward reward : Prestige.REGISTRY.values()) {
            GuiObjectEditingReward rew;
            if(reward.isPlaced()) {
                rew = new GuiObjectEditingReward(this, reward);
                rew.setPlaced(true);
            } else {
                rew = new GuiObjectEditingReward(this, this.left - 96 + (offX * 32), this.top + offY * 32, reward);
                rew.setPlaced(false);
            }
            
            
            this.unplacedRewards.add(rew);
            rew.setGridX(offX);
            rew.setGridY(offY);
            offX++;
            if(offX == 2) {
                offX = 0;
                offY++;
            }
            
        }
        for(GuiObjectEditingReward rewa : unplacedRewards) {
            if(rewa.isPlaced()) {
                for(Reward reward : rewa.getReward().getParents()) {
                    GuiObjectEditingReward rew = getEditingObject(reward.getIdentifier());
                    List<GuiObjectEditingReward> list = connections.getOrDefault(rew, new LinkedList<>());
                    if(rew.isPlaced())
                        list.add(rewa);
                    connections.put(rew, list);
                }
            }
        }
        // this.guiObjects.add(new GuiObjectTest(this, left + 20,top + 20,32,32));
        // this.guiObjects.add(new GuiMenu(this, this.left-55, this.top, 100, 100));
    }
    
    @Override
    public void updateScreen() {
        
        super.updateScreen();
        for(final GuiObject object : this.guiObjects.values()) {
            object.update();
        }
        this.guiObjects.values().forEach(object -> object.setVisible(true));
        for(final GuiObject object : this.guiObjects.values()) {
            if(!object.isAlwaysVisible()) {
                
                if(this.backGround.collides(object)) {
                    object.setVisible(true);
                } else {
                    object.setVisible(false);
                }
            }
        }
        this.unplacedRewards.stream().filter(obj -> !obj.isPlaced() && !obj.equals(selectedReward)).forEach(object -> object.setVisible(true));
        for(GuiObjectEditingReward object : unplacedRewards) {
            if(!object.isPlaced() || object.equals(selectedReward)) {
                continue;
            }
            if(!object.isAlwaysVisible()) {
                
                if(this.backGround.collides(object)) {
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
        this.backGround.draw(this.left, this.top, mouseX, mouseY, partialTicks);
        this.mc.getTextureManager().bindTexture(new ResourceLocation("prestige", "textures/gui/gui_prestige_line.png"));
        for(final Map.Entry<GuiObjectEditingReward, List<GuiObjectEditingReward>> entry : this.connections.entrySet()) {
            final GuiObjectEditingReward start = entry.getKey();
            if(!start.isVisible() || !start.shouldDrawLines()) {
                continue;
            }
            //drawConnections
            for(final GuiObjectEditingReward end : entry.getValue()) {
//                if(!end.shouldDrawLines())
//                    continue;
                GlStateManager.pushMatrix();
                final double angle = Math.atan2(end.getY() - start.getY(), end.getX() - start.getX()) * 180 / Math.PI;
                GL11.glTranslated(start.getX() + start.getWidth() / 2, start.getY() + start.getHeight() / 2, 0);
                GL11.glRotated(angle, 0, 0, 1);
                float length = (float) Math.sqrt((end.getX() - start.getX()) * (end.getX() - start.getX()) + (end.getY() - start.getY()) * (end.getY() - start.getY()));
                if(!end.isVisible()){
                    //get the distance to the border
                    length = (float) Math.sqrt((end.getX() - start.getX()) * (end.getX() - start.getX()) + (end.getY() - start.getY()) * (end.getY() - start.getY()));
                }
                RenderUtils.drawTexturedModalRect(0, 0, RenderUtils.remap((float) (System.nanoTime() / 2000000000.0), 1, 0, 0, 16), 0, length, 4);
                GL11.glTranslated(-(start.getX() + start.getWidth() / 2), -(start.getY() + start.getHeight() / 2), 0);
                GlStateManager.popMatrix();
            }
        }
        //draw objects
        GlStateManager.pushMatrix();
        for(GuiObject object : guiObjects.values()) {
            if(object.isVisible()) {
                object.draw(this.left, this.top, mouseX, mouseY, partialTicks);
                
            }
        }
        
        for(GuiObjectEditingReward reward : unplacedRewards) {
            if(!reward.equals(selectedReward))
                if(reward.isVisible())
                    if(reward.getY() >= this.getTop() && reward.getY() + reward.getHeight() <= this.getTop() + this.getGuiHeight()) {
                        if(reward.equals(editingReward)) {
                            GlStateManager.color(0, 1, 1, 1);
                        }
                        reward.draw(this.left, this.top, mouseX, mouseY, partialTicks);
                        if(reward.equals(editingReward)) {
                            GlStateManager.color(1, 1, 1, 1);
                        }
                    }
        }
        for(GuiObjectEditingReward reward : unplacedRewards) {
            if(!reward.equals(selectedReward)) {
                if(reward.isVisible())
                    if(reward.collides(mouseX, mouseY, mouseX, mouseY)) {
                        if(reward.getY() >= this.getTop() && reward.getY() + reward.getHeight() <= this.getTop() + this.getGuiHeight()) {
                            reward.drawText(mouseX, mouseY);
                        }
                    }
            }
        }
        
        //        GlStateManager.disableAlpha();
        
        
        GlStateManager.popMatrix();
        GlStateManager.popMatrix();
        
        
        GlStateManager.pushMatrix();
        GL11.glTranslated(0, 0, 500);
        this.border.draw(left, top, mouseX, mouseY, partialTicks);
        GL11.glTranslated(0, 0, -500);
        GlStateManager.popMatrix();
        
        if(selectedReward != null) {
            if(selectedReward.isMoving()) {
                selectedReward.setX(mouseX - selectedReward.getWidth() / 2);
                selectedReward.setY(mouseY - selectedReward.getHeight() / 2);
            }
            if(backGround.collides(selectedReward)) {
                GlStateManager.pushMatrix();
                GL11.glTranslated(0, 0, 500);
                GL11.glColor4d(1, 1, 1, 1);
                RenderUtils.drawLineUntextured(selectedReward.getX() + (selectedReward.getWidth() / 2), top, selectedReward.getX() + (selectedReward.getWidth() / 2), mouseY, 1, 1, 1, 2);
                RenderUtils.drawLineUntextured(left, selectedReward.getY() + (selectedReward.getHeight() / 2), mouseX, selectedReward.getY() + (selectedReward.getHeight() / 2), 1, 1, 1, 2);
                
                //bound box
                RenderUtils.drawLineUntextured(selectedReward.getX(), selectedReward.getY(), selectedReward.getX() + selectedReward.getWidth(), selectedReward.getY(), 1, 1, 1, 2);
                RenderUtils.drawLineUntextured(selectedReward.getX(), selectedReward.getY() + selectedReward.getHeight(), selectedReward.getX() + selectedReward.getWidth(), selectedReward.getY() + selectedReward.getHeight(), 1, 1, 1, 2);
                RenderUtils.drawLineUntextured(selectedReward.getX(), selectedReward.getY(), selectedReward.getX(), selectedReward.getY() + selectedReward.getHeight(), 1, 1, 1, 2);
                RenderUtils.drawLineUntextured(selectedReward.getX() + selectedReward.getWidth(), selectedReward.getY(), selectedReward.getX() + selectedReward.getWidth(), selectedReward.getY() + selectedReward.getHeight(), 1, 1, 1, 2);
                
                List<GuiObjectEditingReward> unplaced = unplacedRewards.stream().filter(GuiObjectEditingReward::isPlaced).collect(Collectors.toList());
                List<GuideLine> guideLines = new LinkedList<>();
                GuideLine y1 = new GuideLine(selectedReward.getX(), selectedReward.getX() + selectedReward.getWidth(), selectedReward.getY(), selectedReward.getY() + selectedReward.getHeight());
                GuideLine y2 = new GuideLine(selectedReward.getX(), selectedReward.getX() + selectedReward.getWidth(), selectedReward.getY(), selectedReward.getY() + selectedReward.getHeight());
                GuideLine x1 = new GuideLine(selectedReward.getX(), selectedReward.getX() + selectedReward.getWidth(), selectedReward.getY(), selectedReward.getY() + selectedReward.getHeight());
                GuideLine x2 = new GuideLine(selectedReward.getX(), selectedReward.getX() + selectedReward.getWidth(), selectedReward.getY(), selectedReward.getY() + selectedReward.getHeight());
                
                guideLines.add(y1);
                guideLines.add(y2);
                guideLines.add(x1);
                guideLines.add(x2);
                
                for(GuiObjectEditingReward reward : unplaced) {
                    if(reward.equals(selectedReward) || !reward.isVisible()) {
                        continue;
                    }
                    if(selectedReward.getX() == reward.getX()) {
                        y1.setMinX(Math.min(reward.getX(), y1.getMinX()));
                        y2.setMinX(Math.min(reward.getX() + reward.getWidth(), y2.getMaxX()));
                        y1.setMaxX(Math.max(reward.getX(), y1.getMinX()));
                        y2.setMaxX(Math.max(reward.getX() + reward.getWidth(), y2.getMaxX()));
                        
                        y1.setMinY(Math.min(reward.getY(), y1.getMinY()));
                        y2.setMinY(Math.min(reward.getY(), y2.getMinY()));
                        y1.setMaxY(Math.max(reward.getY() + reward.getHeight(), y1.getMaxY()));
                        y2.setMaxY(Math.max(reward.getY() + reward.getHeight(), y2.getMaxY()));
                        y1.setShouldDraw(true);
                        y2.setShouldDraw(true);
                    }
                    if(selectedReward.getX() + selectedReward.getWidth() == reward.getX()) {
                        y2.setMinX(y2.getMaxX());
                        y2.setMaxX(y2.getMaxX());
                        y2.setMinY(Math.min(reward.getY(), y2.getMinY()));
                        y2.setMaxY(Math.max(reward.getY() + reward.getHeight(), y2.getMaxY()));
                        y2.setShouldDraw(true);
                    }
                    if(selectedReward.getX() == reward.getX() + reward.getWidth()) {
                        y1.setMinX(y1.getMinX());
                        y1.setMaxX(y1.getMinX());
                        y1.setMinY(Math.min(reward.getY(), y1.getMinY()));
                        y1.setMaxY(Math.max(reward.getY() + reward.getHeight(), y1.getMaxY()));
                        y1.setShouldDraw(true);
                    }
                    
                    
                    if(selectedReward.getY() == reward.getY()) {
                        x1.setMinY(Math.min(reward.getY(), x1.getMinY()));
                        x1.setMaxY(Math.min(reward.getY(), x1.getMinY()));
                        x2.setMinY(Math.min(reward.getY() + reward.getHeight(), x2.getMaxY()));
                        x2.setMaxY(Math.min(reward.getY() + reward.getHeight(), x2.getMaxY()));
                        
                        x1.setMinX(Math.min(reward.getX(), x1.getMinX()));
                        x1.setMaxX(Math.max(reward.getX() + reward.getWidth(), x1.getMaxX()));
                        
                        x2.setMinX(Math.min(reward.getX(), x2.getMinX()));
                        x2.setMaxX(Math.max(reward.getX() + reward.getWidth(), x2.getMaxX()));
                        
                        x1.setShouldDraw(true);
                        x2.setShouldDraw(true);
                    }
                    
                    
                    if(selectedReward.getY() + selectedReward.getHeight() == reward.getY()) {
                        x2.setMinY(Math.min(reward.getY(), x2.getMaxY()));
                        x2.setMaxY(Math.min(reward.getY(), x2.getMaxY()));
                        x2.setMinX(Math.min(reward.getX(), x2.getMinX()));
                        x2.setMaxX(Math.max(reward.getX() + reward.getWidth(), x2.getMaxX()));
                        x2.setShouldDraw(true);
                    }
                    if(selectedReward.getY() == reward.getY() + reward.getHeight()) {
                        x1.setMinY(Math.min(reward.getY() + reward.getHeight(), x1.getMinY()));
                        x1.setMaxY(Math.min(reward.getY() + reward.getHeight(), x1.getMinY()));
                        x1.setMinX(Math.min(reward.getX(), x1.getMinX()));
                        x1.setMaxX(Math.max(reward.getX() + reward.getWidth(), x1.getMaxX()));
                        x1.setShouldDraw(true);
                    }
                    
                }
                
                guideLines.forEach(GuideLine::draw);
                
                fontRenderer.drawString(selectedReward.getX() - (selectedReward.getWidth() / 2) - left + "", left + 20, mouseY + 20, 0xFFFFFF, true);
                fontRenderer.drawString(selectedReward.getY() - (selectedReward.getWidth() / 2) - top + "", mouseX + 20, top + 20, 0xFFFFFF, true);
                GL11.glTranslated(0, 0, -500);
                GlStateManager.popMatrix();
            }
            GlStateManager.pushMatrix();
            GL11.glTranslated(0, 0, 500);
            selectedReward.draw(this.left, this.top, mouseX, mouseY, partialTicks);
            //            selectedReward.drawText(mouseX, mouseY);
            GL11.glTranslated(0, 0, -500);
            GlStateManager.popMatrix();
            
            
            //            for(GuiObjectEditingReward reward : unplacedRewards) {
            //                if(reward.isPlaced()){
            //                    continue;
            //                }
            //
            //            }
            
            
        }
        informationPanel.draw(left, top, mouseX, mouseY, partialTicks);
        
        //TODO side detection
        boolean[] sides = new boolean[4];
        int north = 0;
        int east = 1;
        int south = 2;
        int west = 3;
        
        for(GuiObjectEditingReward reward : unplacedRewards) {
            if(!reward.isPlaced()) {
                continue;
            }
            
            if((sides[north] && sides[east] && sides[west] && sides[south])) {
                break;
            }
            if(!sides[west]) {
                if(reward.getX() < left) {
                    sides[west] = true;
                }
            }
            if(!sides[east]) {
                if(reward.getX() > left + guiWidth) {
                    sides[east] = true;
                }
            }
            if(!sides[north]) {
                if(reward.getY() < top) {
                    sides[north] = true;
                }
            }
            if(!sides[south]) {
                if(reward.getY() > top + guiHeight) {
                    sides[south] = true;
                }
            }
            
        }
        if((sides[north] && sides[east] && sides[west] && sides[south])) {
            this.mc.getTextureManager().bindTexture(new ResourceLocation("prestige", "textures/gui/gui_prestige_line.png"));
            if(sides[north]) {
                GlStateManager.pushMatrix();
                GlStateManager.translate(0, 0, 500);
                RenderUtils.drawLineUntextured((left + guiWidth / 2) - 4, top + 25, left + guiWidth / 2, top + 20, 0, 1, 1, 5);
                RenderUtils.drawLineUntextured((left + guiWidth / 2) + 4, top + 25, left + guiWidth / 2, top + 20, 0, 1, 1, 5);
                RenderUtils.drawLineUntextured((left + guiWidth / 2) - 4, top + 25, left + guiWidth / 2 + 4, top + 25, 0, 1, 1, 5);
                
                GlStateManager.translate(0, 0, -500);
                GlStateManager.popMatrix();
            }
            
            if(sides[south]) {
                GlStateManager.pushMatrix();
                GlStateManager.translate(0, 0, 500);
                RenderUtils.drawLineUntextured((left + guiWidth / 2) - 4, top + guiHeight - 25, left + guiWidth / 2, top + guiHeight - 20, 0, 1, 1, 5);
                RenderUtils.drawLineUntextured((left + guiWidth / 2) + 4, top + guiHeight - 25, left + guiWidth / 2, top + guiHeight - 20, 0, 1, 1, 5);
                RenderUtils.drawLineUntextured((left + guiWidth / 2) - 4, top + guiHeight - 25, left + guiWidth / 2 + 4, top + guiHeight - 25, 0, 1, 1, 5);
                GlStateManager.translate(0, 0, -500);
                GlStateManager.popMatrix();
            }
            
            if(sides[east]) {
                GlStateManager.pushMatrix();
                GlStateManager.translate(0, 0, 500);
                RenderUtils.drawLineUntextured(left + guiWidth - 25, top + guiHeight / 2 - 4, left + guiWidth - 20, top + guiHeight / 2, 0, 1, 1, 5);
                RenderUtils.drawLineUntextured(left + guiWidth - 25, top + guiHeight / 2 + 4, left + guiWidth - 20, top + guiHeight / 2, 0, 1, 1, 5);
                RenderUtils.drawLineUntextured(left + guiWidth - 25, top + guiHeight / 2 - 4, left + guiWidth - 25, top + (guiHeight / 2) + 4, 0, 1, 1, 5);
                GlStateManager.translate(0, 0, -500);
                GlStateManager.popMatrix();
            }
            
            if(sides[west]) {
                GlStateManager.pushMatrix();
                GlStateManager.translate(0, 0, 500);
                RenderUtils.drawLineUntextured(left + 25, top + guiHeight / 2 - 4, left + 20, top + guiHeight / 2, 0, 1, 1, 5);
                RenderUtils.drawLineUntextured(left + 25, top + guiHeight / 2 + 4, left + 20, top + guiHeight / 2, 0, 1, 1, 5);
                RenderUtils.drawLineUntextured(left + 25, top + guiHeight / 2 - 4, left + 25, top + (guiHeight / 2) + 4, 0, 1, 1, 5);
                GlStateManager.translate(0, 0, -500);
                GlStateManager.popMatrix();
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
        int wheel = Mouse.getDWheel();
        int offset = 0;
        if(wheel > 0) {
            if(yOff < 0) {
                offset = 32;
                yOff++;
            }
        } else if(wheel < 0) {
            if(Math.abs(yOff) < unplacedRewards.size() / 2 - (this.getGuiWidth() / 32)) {
                offset = -32;
                yOff--;
            }
        }
        
        for(GuiObjectEditingReward reward : unplacedRewards) {
            if(!reward.isPlaced()) {
                reward.setY(reward.getY() + offset);
            }
        }
    }
    
    @Override
    public void handleKeyboardInput() throws IOException {
        
        super.handleKeyboardInput();
        
    }
    
    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        
        super.mouseClicked(mouseX, mouseY, mouseButton);
        this.prevMX = mouseX;
        this.prevMY = mouseY;
        
        if(mouseButton == 0) {
            backGround.mouseClicked(mouseX, mouseY, mouseButton);
            informationPanel.mouseClicked(mouseX, mouseY, mouseButton);
            for(final GuiObject object : this.guiObjects.values()) {
                object.mouseClicked(mouseX, mouseY, mouseButton);
            }
        }
        
        boolean successful = false;
        for(GuiObjectEditingReward reward : this.unplacedRewards) {
            if(reward.collides(mouseX, mouseY, mouseX, mouseY)) {
                if(reward.isPlaced() && !backGround.collides(reward)) {
                    continue;
                }
                if(mouseButton == 0) {
                    if(editingReward == null) {
                        selectedReward = reward;
                        selectedReward.setPlaced(false);
                        successful = true;
                    } else if(backGround.collides(mouseX, mouseY, mouseX, mouseY)) {
                        List<GuiObjectEditingReward> list = connections.getOrDefault(editingReward, new LinkedList<>());
                        if(list.contains(reward)) {
                            list.remove(reward);
                        } else {
                            list.add(reward);
                        }
                        connections.put(editingReward, list);
                    }
                } else if(mouseButton == 1 && selectedReward == null && reward.isPlaced()) {
                    if(reward.equals(editingReward)) {
                        editingReward = null;
                    } else {
                        editingReward = reward;
                    }
                    successful = true;
                }
            }
        }
        if(!successful) {
            if(!Keyboard.isKeyDown(Keyboard.KEY_LSHIFT))
                editingReward = null;
        }
    }
    
    @Override
    protected void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick) {
        
        super.mouseClickMove(mouseX, mouseY, clickedMouseButton, timeSinceLastClick);
        if(selectedReward == null && editingReward == null) {
            backGround.mouseClickMove(mouseX, mouseY, clickedMouseButton, timeSinceLastClick);
            informationPanel.mouseClickMove(mouseX, mouseY, clickedMouseButton, timeSinceLastClick);
            
            
            for(final GuiObject object : this.guiObjects.values()) {
                object.mouseClickMove(mouseX, mouseY, clickedMouseButton, timeSinceLastClick);
            }
            for(GuiObjectEditingReward object : this.unplacedRewards) {
                if(object.isPlaced()) {
                    object.mouseClickMove(mouseX, mouseY, clickedMouseButton, timeSinceLastClick);
                }
            }
            
        } else if(selectedReward != null) {
            selectedReward.setMoving(true);
        }
        this.prevMX = mouseX;
        this.prevMY = mouseY;
        
    }
    
    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state) {
        
        super.mouseReleased(mouseX, mouseY, state);
        this.prevMX = -1;
        this.prevMY = -1;
        backGround.mouseReleased(mouseX, mouseY, state);
        informationPanel.mouseReleased(mouseX, mouseY, state);
        for(final GuiObject object : this.guiObjects.values()) {
            object.mouseReleased(mouseX, mouseY, state);
        }
        if(selectedReward != null) {
            if(backGround.collides(selectedReward)) {
                selectedReward.setPlaced(true);
            } else {
                selectedReward.setPlaced(false);
                selectedReward.setX(this.left - 96 + (selectedReward.getGridX() * 32));
                selectedReward.setY(this.top + (selectedReward.getGridY() + yOff) * 32);
                selectedReward.setVisible(true);
                connections.remove(selectedReward);
                connections.values().forEach(l -> l.remove(selectedReward));
                selectedReward.getReward().getChildren().stream().forEach(rew -> rew.getParents().remove(selectedReward.getReward()));
                selectedReward.getReward().getChildren().clear();
                
            }
            selectedReward.setMoving(false);
            selectedReward = null;
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
    
    @Override
    public Map<String, GuiObject> getGuiObjects() {
        return super.getGuiObjects();
    }
    
    public GuiObjectEditingReward getEditingObject(String identifier) {
        for(GuiObjectEditingReward reward : unplacedRewards) {
            if(reward.getReward().getIdentifier().equals(identifier)) {
                return reward;
            }
        }
        return null;
    }
    
    public GuiObjectEditingReward getEditingReward() {
        return editingReward;
    }
    
    public GuiObjectInformation getInformationPanel() {
        return informationPanel;
    }
}
