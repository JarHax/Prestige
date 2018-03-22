package com.jarhax.prestige.client.utils;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;

public class RenderUtils {

    public static void drawTexturedModalRect (float x, float y, float textureX, float textureY, float width, float height) {

        final Tessellator tessellator = Tessellator.getInstance();
        final BufferBuilder bufferbuilder = tessellator.getBuffer();
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
        bufferbuilder.pos(x, y + height, 0).tex(textureX * 0.00390625F, (textureY + height) * 0.00390625F).endVertex();
        bufferbuilder.pos(x + width, y + height, 0).tex((textureX + width) * 0.00390625F, (textureY + height) * 0.00390625F).endVertex();
        bufferbuilder.pos(x + width, y, 0).tex((textureX + width) * 0.00390625F, textureY * 0.00390625F).endVertex();
        bufferbuilder.pos(x, y, 0).tex(textureX * 0.00390625F, textureY * 0.00390625F).endVertex();
        tessellator.draw();
    }

    public static void drawLine (double x, double y, double x2, double y2, float red, float green, float blue, float lineWidth) {

        final Tessellator tess = Tessellator.getInstance();
        final BufferBuilder buff = tess.getBuffer();

        GL11.glPushMatrix();
        GL11.glPushAttrib(GL11.GL_ALL_ATTRIB_BITS);
        GL11.glLineWidth(lineWidth);
        GL11.glBlendFunc(770, 1);
        buff.begin(3, DefaultVertexFormats.POSITION_TEX_COLOR);
        buff.pos(x, y, 0).tex(0, 0).color(red, green, blue, 1).endVertex();
        buff.pos(x2, y2, 0).tex((Math.sin(System.currentTimeMillis()) + 1) / 2, (Math.sin(System.currentTimeMillis()) + 1) / 2).color(red, green, blue, 1).endVertex();

        tess.draw();
        GL11.glBlendFunc(770, 771);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glPopAttrib();
        GL11.glPopMatrix();
    }

    public static float remap (float value, float from1, float to1, float from2, float to2) {

        return from2 + (value - from1) * (to2 - from2) / (to1 - from1);
    }
}
