package com.jarhax.prestige.client.utils;

import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;

public class RenderUtils {

    public static void drawTexturedModalRect (int x, int y, float textureX, float textureY, int width, int height) {

        final Tessellator tessellator = Tessellator.getInstance();
        final BufferBuilder bufferbuilder = tessellator.getBuffer();
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
        bufferbuilder.pos(x, y + height, 0).tex(textureX * 0.00390625F, (textureY + height) * 0.00390625F).endVertex();
        bufferbuilder.pos(x + width, y + height, 0).tex((textureX + width) * 0.00390625F, (textureY + height) * 0.00390625F).endVertex();
        bufferbuilder.pos(x + width, y, 0).tex((textureX + width) * 0.00390625F, textureY * 0.00390625F).endVertex();
        bufferbuilder.pos(x, y, 0).tex(textureX * 0.00390625F, textureY * 0.00390625F).endVertex();
        tessellator.draw();
    }
}
