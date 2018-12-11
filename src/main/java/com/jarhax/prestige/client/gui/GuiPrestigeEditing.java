package com.jarhax.prestige.client.gui;

import com.jarhax.prestige.Prestige;
import com.jarhax.prestige.api.Reward;
import com.jarhax.prestige.client.gui.objects.*;
import com.jarhax.prestige.client.gui.objects.editing.*;
import com.jarhax.prestige.client.utils.RenderUtils;
import net.minecraft.client.gui.*;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.*;
import net.minecraft.util.*;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import org.lwjgl.input.*;
import org.lwjgl.opengl.GL11;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class GuiPrestigeEditing extends GuiPrestigeBase {
    
    
    private GuiObjectBackGround backGround;
    private GuiObjectBorder border;
    private List<GuiObjectEditingReward> unplacedRewards;
    private List<GuiObjectItemStack> stackList;
    
    private GuiObjectEditingReward selectedReward;
    private int yOff = 0;
    //rows
    private int stackYOff = 0;
    private GuiObjectEditingReward editingReward;
    
    
    private GuiTextField fieldName;
    private GuiTextField fieldDesc;
    private GuiTextField fieldCost;
    private GuiTextField fieldSellPrice;
    private GuiTextField fieldID;
    private GuiTextField fieldFilter;
    
    private GuiButtonTooltip buttonCreateNewReward;
    private GuiButtonTooltip buttonRemoveReward;
    private GuiButtonTooltip buttonUpdateReward;
    private GuiButtonTooltip buttonSave;
    private GuiButtonTooltip buttonReset;
    
    
    private final int north = 0;
    private final int east = 1;
    private final int south = 2;
    private final int west = 3;
    
    private int stackY = 162;
    
    public GuiObjectItemStack selectedStack;
    
    public static final File FILE_TEMP = new File(Prestige.JSON_FILE.getParentFile(), "backup_file.json");
    public boolean saveChanges;
    
    private static boolean validateText(String input) {
        if(input == null || input.isEmpty()) {
            return true;
        }
        try {
            Integer.parseInt(input);
            return true;
        } catch(Exception e) {
            if(input.equalsIgnoreCase("-")){
                return true;
            }
            return false;
        }
    }
    
    public void generateRewards() {
        unplacedRewards.clear();
        int offX = 0;
        int offY = 0;
        LinkedList<Reward> values = new LinkedList<>(Prestige.REGISTRY.values());
        values.sort(Comparator.comparing(Reward::getIdentifier));
        for(Reward reward : values) {
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
    }
    
    public void generateStackList() {
        stackList.clear();
        stackYOff = 0;
        int offX = 0;
        int offY = 0;
        NonNullList<ItemStack> stacks = NonNullList.create();
        for(Item item : ForgeRegistries.ITEMS) {
            item.getSubItems(CreativeTabs.SEARCH, stacks);
        }
        NonNullList<ItemStack> filtered = NonNullList.create();
        String filter = fieldFilter.getText().toLowerCase();
        if(filter.length() > 0)
            for(ItemStack stack : stacks) {
                if(stack == null) {
                    Prestige.LOG.error("INVALID ITEMSTACK!! " + stack);
                    continue;
                }
                
                if(filter.startsWith("@(") && filter.endsWith(")") && !filter.equalsIgnoreCase("@()")) {
                    String[] mods = filter.split("@\\(")[1].split("\\)")[0].split(";");
                    boolean valid = false;
                    for(String s : mods) {
                        s = s.trim();
                        if(stack.getItem().getRegistryName().getResourceDomain().toLowerCase().startsWith(s.toLowerCase())) {
                            valid = true;
                        }
                    }
                    
                    if(valid) {
                        filtered.add(stack);
                    }
                } else {
                    if(stack.getDisplayName() == null || stack.getDisplayName().isEmpty() || !stack.getDisplayName().toLowerCase().contains(filter)) {
                        continue;
                    }
                    filtered.add(stack);
                }
            }
        if(filtered.isEmpty()) {
            filtered = stacks;
        }
        for(ItemStack stack : filtered) {
            
            GuiObjectItemStack st = new GuiObjectItemStack(this, this.left + guiWidth + 5 + (offX * 18), this.top + stackY + (offY * 18), stack);
            stackList.add(st);
            st.setGridX(offX);
            st.setGridY(offY);
            offX++;
            if(offX == 5) {
                offX = 0;
                offY++;
            }
        }
        if(stackList.size() > 0)
            selectedStack = stackList.get(0);
    }
    
    @Override
    public void initGui() {
        
        this.guiWidth = width - width / 2;
        this.guiHeight = height;
        super.initGui();
        this.left = this.width / 2 - this.guiWidth / 2;
        this.top = this.height / 2 - this.guiHeight / 2;
        this.guiObjects = new LinkedHashMap<>();
        unplacedRewards = new LinkedList<>();
        this.backGround = new GuiObjectBackGround(this, this.left, this.top, this.guiWidth, this.guiHeight);
        this.border = new GuiObjectBorder(this, left, top, guiWidth, guiHeight);
        
        int yOffset = 35;
        int count = 0;
        fieldID = new GuiTextField(0, fontRenderer, left + guiWidth + 5, top + yOffset + (28 * count++), 100, fontRenderer.FONT_HEIGHT + 4);
        fieldID.setMaxStringLength(Integer.MAX_VALUE);
        fieldName = new GuiTextField(0, fontRenderer, left + guiWidth + 5, top + yOffset + (28 * count++), 100, fontRenderer.FONT_HEIGHT + 4);
        fieldName.setMaxStringLength(Integer.MAX_VALUE);
        fieldDesc = new GuiTextField(0, fontRenderer, left + guiWidth + 5, top + yOffset + (28 * count++), 100, fontRenderer.FONT_HEIGHT + 4);
        fieldDesc.setMaxStringLength(Integer.MAX_VALUE);
        fieldCost = new GuiTextField(0, fontRenderer, left + guiWidth + 5, top + yOffset + (28 * count), 30, fontRenderer.FONT_HEIGHT + 4);
        fieldCost.setMaxStringLength(Integer.MAX_VALUE);
        fieldSellPrice = new GuiTextField(0, fontRenderer, left + guiWidth + 5 + 35, top + yOffset + (28 * count++), 50, fontRenderer.FONT_HEIGHT + 4);
        fieldSellPrice.setMaxStringLength(Integer.MAX_VALUE);
        fieldFilter = new GuiTextField(0, fontRenderer, left + guiWidth + 5, top + yOffset + (28 * count++), 100, fontRenderer.FONT_HEIGHT + 4);
        fieldFilter.setMaxStringLength(Integer.MAX_VALUE);
        
        fieldCost.setValidator(GuiPrestigeEditing::validateText);
        fieldSellPrice.setValidator(input -> validateText(input));
        fieldName.setEnabled(true);
        fieldDesc.setEnabled(true);
        fieldCost.setEnabled(true);
        fieldSellPrice.setEnabled(true);
        fieldID.setEnabled(true);
        fieldFilter.setEnabled(true);
        fieldCost.setText("0");
        fieldSellPrice.setText("0");
        count = 0;
        buttonCreateNewReward = new GuiButtonTooltip(0, left + guiWidth + 5 + (20 * count++), top, 20, 20, "+", this, "Add");
        buttonRemoveReward = new GuiButtonTooltip(1, left + guiWidth + 5 + (20 * count++), top, 20, 20, "-", this, "Remove");
        buttonUpdateReward = new GuiButtonTooltip(2, left + guiWidth + 5 + (20 * count++), top, 20, 20, "*", this, "Update");
        buttonSave = new GuiButtonTooltip(3, left + guiWidth + 5 + (20 * count++), top, 20, 20, "S", this, "Save");
        buttonReset = new GuiButtonTooltip(4, left + guiWidth + 5 + (20 * count), top, 20, 20, "R", this, "Reset");
        this.addButton(buttonCreateNewReward);
        this.addButton(buttonRemoveReward);
        this.addButton(buttonUpdateReward);
        this.addButton(buttonSave);
        this.addButton(buttonReset);
        
        
        generateRewards();
        
        
        stackList = new LinkedList<>();
        generateStackList();
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
        if(editingReward != null) {
            fieldName.updateCursorCounter();
            fieldDesc.updateCursorCounter();
            fieldCost.updateCursorCounter();
            fieldSellPrice.updateCursorCounter();
            fieldID.updateCursorCounter();
            fieldFilter.updateCursorCounter();
            
        }
        
        if(editingReward == null) {
            buttonCreateNewReward.visible = true;
        } else {
            buttonCreateNewReward.visible = false;
        }
        
        if(saveChanges) {
            saveChanges = false;
            saveTempFile();
        }
    }
    
    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        
        GlStateManager.pushMatrix();
        this.backGround.draw(this.left, this.top, mouseX, mouseY, partialTicks);
        this.mc.getTextureManager().bindTexture(new ResourceLocation("prestige", "textures/gui/gui_prestige_line.png"));
        ScaledResolution resolution = new ScaledResolution(mc);
        int scale = resolution.getScaleFactor();
        GL11.glScissor((left + 4) * scale, (top - 1 + 4) * scale, (guiWidth - 8) * scale, (guiHeight + 1 - 8) * scale);
        GL11.glEnable(GL11.GL_SCISSOR_TEST);
        for(GuiObjectEditingReward parent : unplacedRewards) {
            Vec3d start = new Vec3d(parent.getX() + parent.getWidth() / 2, parent.getY() + parent.getHeight() / 2, 0);
            for(Reward child : parent.getReward().getChildren()) {
                if(child == null || child.getIdentifier() == null) {
                    Prestige.LOG.error("Child was not found! Please notify their parent: " + parent.getReward().getIdentifier());
                    continue;
                }
                GuiObjectEditingReward childObject = getObject(child.getIdentifier());
                if(childObject == null) {
                    continue;
                }
                Vec3d end = new Vec3d(child.getX() + childObject.getWidth() / 2, child.getY() + childObject.getHeight() / 2, 0);
                GlStateManager.pushMatrix();
                final double angle = Math.atan2(child.getY() - parent.getY(), child.getX() - parent.getX()) * 180 / Math.PI;
                GL11.glTranslated(parent.getX() + parent.getWidth() / 2, parent.getY() + parent.getHeight() / 2, 0);
                GL11.glRotated(angle, 0, 0, 1);
                float length = (float) start.distanceTo(end);
                RenderUtils.drawTexturedModalRect(0, -2, RenderUtils.remap((float) (System.nanoTime() / 1000000000.0), 1, 0, 0, 16), 0, length, 4);
                GL11.glTranslated(-(parent.getX() + parent.getWidth() / 2), -(parent.getY() + parent.getHeight() / 2), 0);
                GlStateManager.popMatrix();
            }
        }
        GL11.glDisable(GL11.GL_SCISSOR_TEST);
        //draw objects
        GlStateManager.pushMatrix();
        for(GuiObject object : guiObjects.values()) {
            if(object.isVisible()) {
                object.draw(this.left, this.top, mouseX, mouseY, partialTicks);
                
            }
        }
        
        for(GuiObjectItemStack stack : stackList) {
            if(!stack.isVisible()) {
                continue;
            }
            if(stack.getY() >= top + stackY && stack.getY() + stack.getHeight() <= top + stackY + (5 * 18)) {
                if(selectedStack.equals(stack)) {
                    RenderUtils.drawLineUntextured(stack.getX(), stack.getY(), stack.getX2() - 2, stack.getY(), 0f, 0.8f, 0.8f, 2);
                    RenderUtils.drawLineUntextured(stack.getX(), stack.getY2() - 2, stack.getX2() - 2, stack.getY2() - 2, 0f, 0.8f, 0.8f, 2);
                    RenderUtils.drawLineUntextured(stack.getX(), stack.getY(), stack.getX(), stack.getY2() - 2, 0f, 0.8f, 0.8f, 2);
                    RenderUtils.drawLineUntextured(stack.getX2() - 2, stack.getY(), stack.getX2() - 2, stack.getY2() - 2, 0f, 0.8f, 0.8f, 2);
                }
                stack.draw(left, top, mouseX, mouseY, partialTicks);
            }
        }
        
        for(GuiObjectEditingReward reward : unplacedRewards) {
            if(!reward.equals(selectedReward)) {
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
        }
        
        
        GlStateManager.popMatrix();
        GlStateManager.popMatrix();
        
        
        GlStateManager.pushMatrix();
        GL11.glTranslated(0, 0, 500);
        this.border.draw(left, top, mouseX, mouseY, partialTicks);
        GL11.glTranslated(0, 0, 0);
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
                        y1.setX(Math.min(reward.getX(), y1.getMinX()));
                        y2.setX(Math.min(reward.getX() + reward.getWidth(), y2.getMaxX()));
                        
                        y1.setMinY(Math.min(reward.getY(), y1.getMinY()));
                        y1.setMaxY(Math.max(reward.getY() + reward.getHeight(), y1.getMaxY()));
                        y2.setMinY(Math.min(reward.getY(), y2.getMinY()));
                        y2.setMaxY(Math.max(reward.getY() + reward.getHeight(), y2.getMaxY()));
                        
                        y1.setShouldDraw(true);
                        y2.setShouldDraw(true);
                    }
                    if(selectedReward.getX() + selectedReward.getWidth() == reward.getX()) {
                        y2.setX(y2.getMaxX());
                        y2.setMinY(Math.min(reward.getY(), y2.getMinY()));
                        y2.setMaxY(Math.max(reward.getY() + reward.getHeight(), y2.getMaxY()));
                        y2.setShouldDraw(true);
                    }
                    if(selectedReward.getX() == reward.getX() + reward.getWidth()) {
                        y1.setX(y1.getMinX());
                        y1.setMinY(Math.min(reward.getY(), y1.getMinY()));
                        y1.setMaxY(Math.max(reward.getY() + reward.getHeight(), y1.getMaxY()));
                        y1.setShouldDraw(true);
                    }
                    
                    
                    if(selectedReward.getY() == reward.getY()) {
                        x1.setY(Math.min(reward.getY(), x1.getMinY()));
                        x2.setY(Math.min(reward.getY() + reward.getHeight(), x2.getMaxY()));
                        
                        x1.setMinX(Math.min(reward.getX(), x1.getMinX()));
                        x1.setMaxX(Math.max(reward.getX() + reward.getWidth(), x1.getMaxX()));
                        
                        x2.setMinX(Math.min(reward.getX(), x2.getMinX()));
                        x2.setMaxX(Math.max(reward.getX() + reward.getWidth(), x2.getMaxX()));
                        
                        x1.setShouldDraw(true);
                        x2.setShouldDraw(true);
                    }
                    
                    
                    if(selectedReward.getY() + selectedReward.getHeight() == reward.getY()) {
                        x2.setY(Math.min(reward.getY(), x2.getMaxY()));
                        x2.setMinX(Math.min(reward.getX(), x2.getMinX()));
                        x2.setMaxX(Math.max(reward.getX() + reward.getWidth(), x2.getMaxX()));
                        x2.setShouldDraw(true);
                    }
                    if(selectedReward.getY() == reward.getY() + reward.getHeight()) {
                        x1.setY(Math.min(reward.getY() + reward.getHeight(), x1.getMinY()));
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
            GL11.glTranslated(0, 0, -500);
            GlStateManager.popMatrix();
            
            
        }
        
        
        boolean[] sides = new boolean[4];
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
        if((sides[north] || sides[east] || sides[west] || sides[south])) {
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
        
        
        fontRenderer.drawStringWithShadow("Name: ", fieldName.x, fieldName.y - 11, 0xFFFFFF);
        fieldName.drawTextBox();
        fontRenderer.drawStringWithShadow("Description: ", fieldDesc.x, fieldDesc.y - 11, 0xFFFFFF);
        fieldDesc.drawTextBox();
        fontRenderer.drawStringWithShadow("Cost: ", fieldCost.x, fieldCost.y - 11, 0xFFFFFF);
        fieldCost.drawTextBox();
        
        fontRenderer.drawStringWithShadow("Sell Price: ", fieldSellPrice.x, fieldSellPrice.y - 11, 0xFFFFFF);
        fieldSellPrice.drawTextBox();
        
        fontRenderer.drawStringWithShadow("ID: ", fieldID.x, fieldID.y - 11, 0xFFFFFF);
        fieldID.drawTextBox();
        
        fontRenderer.drawStringWithShadow("Filter: ", fieldFilter.x, fieldFilter.y - 11, 0xFFFFFF);
        fieldFilter.drawTextBox();
        
        
        super.drawScreen(mouseX, mouseY, partialTicks);
        buttonCreateNewReward.drawText(mouseX, mouseY);
        buttonUpdateReward.drawText(mouseX, mouseY);
        buttonRemoveReward.drawText(mouseX, mouseY);
        buttonSave.drawText(mouseX, mouseY);
        buttonReset.drawText(mouseX, mouseY);
        {
            int butLeft = this.left + guiWidth + 5 + 18 * 5;
            int butLeftInner = butLeft + 2;
            
            int butRight = this.left + guiWidth + 5 + 18 * 5 + 10;
            
            RenderUtils.drawRect(butLeft, this.top + stackY, butRight, this.top + stackY + 10, 0xFF111111);
            RenderUtils.drawRect(butLeftInner, this.top + stackY + 2, butRight - 2, this.top + stackY + 8, 0xFF333333);
            
            RenderUtils.drawRect(butLeft, this.top + stackY + (5 * 18) - 10, butRight, this.top + stackY + (5 * 18), 0xFF111111);
            RenderUtils.drawRect(butLeftInner, this.top + stackY + (5 * 18) - 8, butRight - 2, this.top + stackY + (5 * 18) - 2, 0xFF333333);
            
            RenderUtils.drawLineUntextured(butLeft + 5, this.top + stackY + 2, butLeft + 2, this.top + stackY + 10 - 2, 0, 0.5f, 1, 4);
            RenderUtils.drawLineUntextured(butLeft + 5, this.top + stackY + 2, butLeft + 8, this.top + stackY + 10 - 2, 0, 0.5f, 1, 4);
            RenderUtils.drawLineUntextured(butLeft + 8, this.top + stackY + 8, butLeft + 2, this.top + stackY + 8, 0, 0.5f, 1, 4);
            
            RenderUtils.drawLineUntextured(butLeft + 5, this.top + stackY + (5 * 18) - 2, butLeft + 2, this.top + stackY - 6 + (5 * 18) - 2, 0, 0.5f, 1, 4);
            RenderUtils.drawLineUntextured(butLeft + 5, this.top + stackY + (5 * 18) - 2, butLeft + 8, this.top + stackY - 6 + (5 * 18) - 2, 0, 0.5f, 1, 4);
            RenderUtils.drawLineUntextured(butLeft + 8, this.top + stackY - 6 + (5 * 18) - 2, butLeft + 2, this.top + stackY - 6 + (5 * 18) - 2, 0, 0.5f, 1, 4);
        }
        {
            
            int butLeft = this.left - 32;
            int butLeftInner = this.left - 30;
            
            int butRight = this.left - 32 + 10;
            
            RenderUtils.drawRect(butLeft, this.top, butRight, this.top + 10, 0xFF111111);
            RenderUtils.drawRect(butLeftInner, this.top + 2, butRight - 2, this.top + 8, 0xFF333333);
            
            RenderUtils.drawRect(butLeft, this.top + guiHeight - 10, butRight, this.top + guiHeight, 0xFF111111);
            RenderUtils.drawRect(butLeftInner, this.top + guiHeight - 8, butRight - 2, this.top + guiHeight - 2, 0xFF333333);
            
            RenderUtils.drawLineUntextured(butLeft + 5, this.top + 2, butLeft + 2, this.top + 8, 0, 0.5f, 1, 4);
            RenderUtils.drawLineUntextured(butLeft + 5, this.top + 2, butLeft + 8, this.top + 8, 0, 0.5f, 1, 4);
            RenderUtils.drawLineUntextured(butLeft + 8, this.top + 8, butLeft + 2, this.top + 8, 0, 0.5f, 1, 4);
            
            RenderUtils.drawLineUntextured(butLeft + 5, this.top + guiHeight - 2, butLeft + 2, this.top + guiHeight - 8, 0, 0.5f, 1, 4);
            RenderUtils.drawLineUntextured(butLeft + 5, this.top + guiHeight - 2, butLeft + 8, this.top + guiHeight - 8, 0, 0.5f, 1, 4);
            RenderUtils.drawLineUntextured(butLeft + 8, this.top + guiHeight - 8, butLeft + 2, this.top + guiHeight - 8, 0, 0.5f, 1, 4);
        }
        for(GuiObjectEditingReward reward : unplacedRewards) {
            if(!reward.equals(selectedReward)) {
                if(reward.isVisible())
                    if(reward.collides(mouseX, mouseY)) {
                        if(reward.getY() >= this.getTop() && reward.getY() + reward.getHeight() <= this.getTop() + this.getGuiHeight()) {
                            reward.drawText(mouseX, mouseY);
                        }
                    }
            }
        }
        
        for(GuiObjectItemStack stack : stackList) {
            if(stack.isVisible()) {
                if(stack.collides(mouseX, mouseY)) {
                    if(stack.getY() >= top + stackY && stack.getY() + stack.getHeight() <= top + stackY + (5 * 18)) {
                        stack.drawText(mouseX, mouseY);
                    }
                    
                }
            }
        }
    }
    
    
    @Override
    public void drawBackground(int tint) {
        
        super.drawBackground(tint);
    }
    
    @Override
    public void handleMouseInput() throws IOException {
        
        super.handleMouseInput();
        
        int x = Mouse.getEventX() * this.width / this.mc.displayWidth;
        int y = this.height - Mouse.getEventY() * this.height / this.mc.displayHeight - 1;
        int wheel = Mouse.getDWheel();
        int offset = 0;
        if(x > this.left - 96 && x < this.left - 96 + 64) {
            if(wheel != 0) {
                if(wheel > 0) {
                    if(yOff < 0) {
                        offset = 32;
                        yOff++;
                    }
                } else {
                    if(Math.abs(yOff) < (unplacedRewards.size() - 2) / 2) {
                        offset = -32;
                        yOff--;
                    }
                }
                
                for(GuiObjectEditingReward reward : unplacedRewards) {
                    if(!reward.isPlaced()) {
                        reward.setY(reward.getY() + offset);
                    }
                }
                saveChanges = true;
            }
        }
        
        if(y >= top + stackY && y + 18 <= top + stackY + (6 * 18)) {
            if(x > this.left + guiWidth + 5 && x < this.left + guiWidth + 5 + 18 * 5) {
                if(wheel != 0) {
                    if(wheel > 0 && stackYOff < 0) {
                        offset = 18;
                        stackYOff++;
                    } else if(wheel < 0) {
                        if(Math.abs(stackYOff) < (stackList.size() - 25) / 5) {
                            offset = -18;
                            stackYOff--;
                        }
                    }
                    for(GuiObjectItemStack stack : stackList) {
                        stack.setY(stack.getY() + offset);
                    }
                    saveChanges = true;
                }
            }
        }
        
    }
    
    @Override
    public void handleKeyboardInput() throws IOException {
        Keyboard.enableRepeatEvents(true);
        super.handleKeyboardInput();
    }
    
    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        super.keyTyped(typedChar, keyCode);
        //TODO tab for sell price
        if(typedChar == '\t') {
            if(fieldID.isFocused()) {
                fieldName.setFocused(true);
                fieldID.setFocused(false);
            } else if(fieldName.isFocused()) {
                fieldDesc.setFocused(true);
                fieldName.setFocused(false);
            } else if(fieldDesc.isFocused()) {
                fieldCost.setFocused(true);
                fieldDesc.setFocused(false);
            } else if(fieldCost.isFocused()) {
                fieldSellPrice.setFocused(true);
                fieldCost.setFocused(false);
            } else if(fieldSellPrice.isFocused()) {
                fieldFilter.setFocused(true);
                fieldSellPrice.setFocused(false);
            } else if(fieldFilter.isFocused()) {
                fieldID.setFocused(true);
                fieldFilter.setFocused(false);
            }
        } else {
            fieldName.textboxKeyTyped(typedChar, keyCode);
            fieldDesc.textboxKeyTyped(typedChar, keyCode);
            fieldCost.textboxKeyTyped(typedChar, keyCode);
            fieldSellPrice.textboxKeyTyped(typedChar, keyCode);
            
            fieldID.textboxKeyTyped(typedChar, keyCode);
            if(fieldFilter.textboxKeyTyped(typedChar, keyCode)) {
                generateStackList();
            }
        }
    }
    
    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        
        super.mouseClicked(mouseX, mouseY, mouseButton);
        this.prevMX = mouseX;
        this.prevMY = mouseY;
        
        if(mouseButton == 0) {
            boolean clicked = false;
            int offset = 0;
            if(mouseX > this.left + guiWidth + 5 + 18 * 5 && mouseX < this.left + guiWidth + 5 + 18 * 5 + 10) {
                
                if(mouseY > this.top + stackY + (5 * 18) - 10 && mouseY < this.top + stackY + (5 * 18)) {
                    if(Math.abs(stackYOff) < (stackList.size() - 25) / 5) {
                        stackYOff--;
                        offset = -18;
                        clicked = true;
                    }
                }
                
                if(mouseY > this.top + stackY && mouseY < this.top + stackY + 10) {
                    if(stackYOff < 0) {
                        stackYOff++;
                        offset = 18;
                        clicked = true;
                    }
                }
                
                if(clicked) {
                    for(GuiObjectItemStack stack : stackList) {
                        stack.setY(stack.getY() + offset);
                    }
                }
            }
            
            if(mouseX > this.left - 32 && mouseX < this.left - 32 + 10) {
                
                if(mouseY > this.top && mouseY < this.top + 10) {
                    if(Math.abs(yOff) < (unplacedRewards.size() - 2) / 2) {
                        yOff--;
                        offset = -32;
                        clicked = true;
                    }
                }
                if(mouseY < this.top + guiHeight && mouseY > this.top + guiHeight - 10) {
                    if(yOff <= -1) {
                        yOff++;
                        offset = 32;
                        clicked = true;
                    }
                }
                
                if(clicked) {
                    for(GuiObjectEditingReward reward : unplacedRewards) {
                        if(!reward.isPlaced()) {
                            reward.setY(reward.getY() + offset);
                        }
                    }
                }
            }
            if(!clicked) {
                backGround.mouseClicked(mouseX, mouseY, mouseButton);
                for(final GuiObject object : this.guiObjects.values()) {
                    object.mouseClicked(mouseX, mouseY, mouseButton);
                }
            }
            
            stackList.stream().filter(stack -> stack.getY() >= top + stackY && stack.getY() + stack.getHeight() <= top + stackY + (5 * 18)).forEach(stack -> stack.mouseClicked(mouseX, mouseY, mouseButton));
            saveChanges = true;
        }
        
        boolean successful = false;
        for(GuiObjectEditingReward reward : this.unplacedRewards) {
            if(reward.collides(mouseX, mouseY, mouseX, mouseY)) {
                if(reward.isPlaced() && !backGround.collides(reward)) {
                    continue;
                }
                if(mouseButton == 0) {
                    if(!reward.isLocked()) {
                        if(editingReward != null && !backGround.collides(mouseX, mouseY, mouseX, mouseY)) {
                            selectedReward = reward;
                            selectedReward.setPlaced(false);
                            successful = true;
                            
                        }
                        if(editingReward == null) {
                            selectedReward = reward;
                            selectedReward.setPlaced(false);
                            successful = true;
                        } else if(backGround.collides(mouseX, mouseY, mouseX, mouseY) && !reward.equals(editingReward)) {
                            Set<Reward> children = editingReward.getReward().getChildren();
                            if(children.contains(reward.getReward())) {
                                editingReward.getReward().removeChild(reward.getReward());
                            } else {
                                editingReward.getReward().addChild(reward.getReward());
                            }
                            successful = true;
                        }
                    }
                } else if(mouseButton == 1 && selectedReward == null && reward.isPlaced()) {
                    if(reward.equals(editingReward)) {
                        editingReward = null;
                    } else {
                        editingReward = reward;
                        fieldName.setText(editingReward.getReward().getTitle());
                        fieldCost.setText(editingReward.getReward().getCost() + "");
                        fieldSellPrice.setText(editingReward.getReward().getSellPrice() + "");
                        fieldDesc.setText(editingReward.getReward().getDescription());
                        fieldID.setText(editingReward.getReward().getIdentifier());
                        selectedStack = getStack(editingReward.getReward().getIcon());
                    }
                    successful = true;
                }
            }
        }
        if(fieldName.mouseClicked(mouseX, mouseY, mouseButton)) {
            successful = true;
        }
        if(fieldDesc.mouseClicked(mouseX, mouseY, mouseButton)) {
            successful = true;
        }
        if(fieldCost.mouseClicked(mouseX, mouseY, mouseButton)) {
            successful = true;
        }
        if(fieldSellPrice.mouseClicked(mouseX, mouseY, mouseButton)) {
            successful = true;
        }
        if(fieldID.mouseClicked(mouseX, mouseY, mouseButton)) {
            successful = true;
        }
        if(fieldFilter.mouseClicked(mouseX, mouseY, mouseButton)) {
            successful = true;
        }
        if(!successful) {
            editingReward = null;
        } else {
            saveChanges = true;
        }
        
    }
    
    
    @Override
    protected void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick) {
        
        super.mouseClickMove(mouseX, mouseY, clickedMouseButton, timeSinceLastClick);
        //TODO make sure background only collision is what is wanted
        if(selectedReward == null && editingReward == null && backGround.collides(mouseX, mouseY)) {
            backGround.mouseClickMove(mouseX, mouseY, clickedMouseButton, timeSinceLastClick);
            
            
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
                for(Reward parent : selectedReward.getReward().getParents()) {
                    parent.removeChild(selectedReward.getReward());
                }
                for(Reward child : selectedReward.getReward().getChildren()) {
                    child.removeParent(selectedReward.getReward());
                }
                selectedReward.getReward().clearParents();
                selectedReward.getReward().clearChildren();
                
            }
            selectedReward.setMoving(false);
            selectedReward = null;
        }
        saveChanges = true;
    }
    
    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        super.actionPerformed(button);
        String id = fieldID.getText();
        if(button.id != buttonSave.id && button.id != buttonReset.id) {
            if(id.isEmpty()) {
                return;
            }
        }
        if(button.id == buttonCreateNewReward.id) {
            if(!Prestige.REGISTRY.containsKey(id)) {
                if(fieldCost.getText().isEmpty()) {
                    fieldCost.setText(0 + "");
                }
                if(fieldSellPrice.getText().isEmpty()) {
                    fieldSellPrice.setText("0");
                }
                Prestige.REGISTRY.put(id, new Reward(id, fieldName.getText(), 0, 0, Integer.parseInt(fieldCost.getText()), Integer.parseInt(fieldSellPrice.getText()), selectedStack.getStack(), fieldDesc.getText()));
                fieldName.setText("");
                fieldDesc.setText("");
                fieldID.setText("");
                fieldCost.setText("0");
                fieldSellPrice.setText("0");
            }
        }
        
        if(button.id == buttonRemoveReward.id) {
            if(Prestige.REGISTRY.containsKey(id)) {
                Reward removed = Prestige.REGISTRY.get(id);
                
                for(Reward reward : Prestige.REGISTRY.values()) {
                    reward.removeParent(removed);
                    reward.removeChild(removed);
                }
                removed.clearParents();
                removed.clearChildren();
                Prestige.REGISTRY.remove(id);
            }
        }
        if(button.id == buttonUpdateReward.id) {
            
            if(Prestige.REGISTRY.containsKey(id) || editingReward != null) {
                if(fieldCost.getText().isEmpty()) {
                    fieldCost.setText(0 + "");
                }
                if(fieldSellPrice.getText().isEmpty()) {
                    fieldSellPrice.setText(0 + "");
                }
                Reward updated;
                if(editingReward != null) {
                    updated = editingReward.getReward();
                } else {
                    updated = Prestige.REGISTRY.get(id);
                }
                updated.setTitle(fieldName.getText());
                updated.setDescription(fieldDesc.getText());
                updated.setCost(Integer.parseInt(fieldCost.getText()));
                updated.setSellPrice(Integer.parseInt(fieldSellPrice.getText()));
                updated.setIcon(selectedStack.getStack());
            }
        }
        if(button.id == buttonSave.id) {
            Prestige.INSTANCE.saveRewards();
        }
        if(button.id == buttonReset.id) {
            Prestige.INSTANCE.loadRewards();
            //            generateRewards();
        }
        
        yOff = 0;
        generateRewards();
        saveChanges = true;
    }
    
    
    public GuiObjectEditingReward getObject(String identifier) {
        for(GuiObjectEditingReward reward : unplacedRewards) {
            if(reward.getReward().getIdentifier().equals(identifier)) {
                return reward;
            }
        }
        return null;
    }
    
    public GuiObjectItemStack getStack(ItemStack stack) {
        for(GuiObjectItemStack itemStack : stackList) {
            if(itemStack.getStack().isItemEqual(stack)) {
                return itemStack;
            }
        }
        return stackList.get(0);
    }
    
    public GuiObjectEditingReward getEditingReward() {
        return editingReward;
    }
    
    
    public void saveTempFile() {
        try {
            if(!FILE_TEMP.exists()) {
                FILE_TEMP.getParentFile().mkdirs();
                FILE_TEMP.createNewFile();
            }
            BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_TEMP));
            String json = Prestige.GSON.toJson(Prestige.REGISTRY.values().toArray());
            writer.write(json);
            writer.close();
        } catch(Exception e) {
            Prestige.LOG.error("Unable to save Prestige backup JSON file!", e);
        }
    }
    
    
    public GuiTextField getFieldFilter() {
        return fieldFilter;
    }
}
