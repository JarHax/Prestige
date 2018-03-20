package com.jarhax.prestige.client.utils;

import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;

public class RenderUtils {
    
    public static void drawTexturedModalRect(int x, int y, float textureX, float textureY, int width, int height) {
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
        bufferbuilder.pos((double) (x), (double) (y + height), (double) 0).tex((double) (textureX * 0.00390625F), (double) ((textureY + height) * 0.00390625F)).endVertex();
        bufferbuilder.pos((double) (x + width), (double) (y + height), (double) 0).tex(((textureX + width) * 0.00390625F), (double) ((textureY + height) * 0.00390625F)).endVertex();
        bufferbuilder.pos((double) (x + width), (double) (y), (double) 0).tex((double) ((textureX + width) * 0.00390625F), (double) ((textureY) * 0.00390625F)).endVertex();
        bufferbuilder.pos((double) (x), (double) (y), (double) 0).tex((double) ((textureX) * 0.00390625F), (double) ((textureY) * 0.00390625F)).endVertex();
        tessellator.draw();
    }
}
