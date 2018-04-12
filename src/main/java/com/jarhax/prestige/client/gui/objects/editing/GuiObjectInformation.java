package com.jarhax.prestige.client.gui.objects.editing;

import com.jarhax.prestige.client.gui.*;
import com.jarhax.prestige.client.gui.objects.GuiObject;
import com.jarhax.prestige.client.utils.RenderUtils;
import net.minecraft.client.gui.Gui;

public class GuiObjectInformation extends GuiObject {
    
    private GuiPrestigeEditing parent;
    
    public GuiObjectInformation(GuiPrestigeBase parent, int x, int y, int width, int height) {
        super(parent, x, y, width, height);
        this.parent = (GuiPrestigeEditing) parent;
    }
    
    @Override
    public void draw(int left, int top, int mouseX, int mouseY, float partialTicks) {
//        RenderUtils.drawLineUntextured(x, y, x + width, y + height, 1, 0, 0, 3);
//        RenderUtils.drawLineUntextured(x + width, y, x, y+height, 1, 0, 0, 3);
        Gui.drawRect((int)x,(int)y,(int)x+width,(int)y+height, 0xFF222222);
    }
    
    @Override
    public GuiPrestigeEditing getParent() {
        return parent;
    }
}
