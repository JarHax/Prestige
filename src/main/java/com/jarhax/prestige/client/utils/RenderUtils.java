package com.jarhax.prestige.client.utils;

import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import org.lwjgl.opengl.GL11;


public class RenderUtils {
    
    public static void drawTexturedModalRect(float x, float y, float textureX, float textureY, float width, float height) {
        
        final Tessellator tessellator = Tessellator.getInstance();
        final BufferBuilder bufferbuilder = tessellator.getBuffer();
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
        bufferbuilder.pos(x, y + height, 0).tex(textureX * 0.00390625F, (textureY + height) * 0.00390625F).endVertex();
        bufferbuilder.pos(x + width, y + height, 0).tex((textureX + width) * 0.00390625F, (textureY + height) * 0.00390625F).endVertex();
        bufferbuilder.pos(x + width, y, 0).tex((textureX + width) * 0.00390625F, textureY * 0.00390625F).endVertex();
        bufferbuilder.pos(x, y, 0).tex(textureX * 0.00390625F, textureY * 0.00390625F).endVertex();
        tessellator.draw();
    }
    
    public static void drawRect(float left, float top, float right, float bottom, int color) {
        if(left < right) {
            float i = left;
            left = right;
            right = i;
        }
        
        if(top < bottom) {
            float j = top;
            top = bottom;
            bottom = j;
        }
        
        float f3 = (float) (color >> 24 & 255) / 255.0F;
        float f = (float) (color >> 16 & 255) / 255.0F;
        float f1 = (float) (color >> 8 & 255) / 255.0F;
        float f2 = (float) (color & 255) / 255.0F;
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        GlStateManager.color(f, f1, f2, f3);
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION);
        bufferbuilder.pos((double) left, (double) bottom, 0.0D).endVertex();
        bufferbuilder.pos((double) right, (double) bottom, 0.0D).endVertex();
        bufferbuilder.pos((double) right, (double) top, 0.0D).endVertex();
        bufferbuilder.pos((double) left, (double) top, 0.0D).endVertex();
        tessellator.draw();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }
    
    public static void drawLineUntextured(double x, double y, double x2, double y2, float red, float green, float blue, float lineWidth) {
        
        final Tessellator tess = Tessellator.getInstance();
        final BufferBuilder buff = tess.getBuffer();
        
        GL11.glPushMatrix();
        GL11.glPushAttrib(GL11.GL_ALL_ATTRIB_BITS);
        GlStateManager.disableTexture2D();
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glLineWidth(lineWidth);
        buff.begin(3, DefaultVertexFormats.POSITION_COLOR);
        buff.pos(x, y, 0).color(red, green, blue, 1).endVertex();
        buff.pos(x2, y2, 0).color(red, green, blue, 1).endVertex();
        tess.draw();
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.enableTexture2D();
        
        GL11.glPopAttrib();
        GL11.glPopMatrix();
    }
    
    public static void drawLine(double x, double y, double x2, double y2, float red, float green, float blue, float lineWidth) {
        
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
    
    public static float remap(float value, float from1, float to1, float from2, float to2) {
        
        return from2 + (value - from1) * (to2 - from2) / (to1 - from1);
    }
}
