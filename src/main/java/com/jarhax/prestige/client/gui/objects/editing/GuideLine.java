package com.jarhax.prestige.client.gui.objects.editing;

import com.jarhax.prestige.client.utils.RenderUtils;

public class GuideLine {
    
    
    private double minX;
    private double maxX;
    private double minY;
    private double maxY;
    private boolean shouldDraw;
    
    public GuideLine(double minx, double maxX, double minY, double maxY) {
        this.minX = minx;
        this.maxX = maxX;
        this.minY = minY;
        this.maxY = maxY;
    }
    
    public void draw() {
        if(shouldDraw)
            RenderUtils.drawLineUntextured(minX, minY, maxX, maxY, 1, 0.2901960784313725f, 1, 2);
    }
    
    
    public double getMinX() {
        return minX;
    }
    
    public void setMinX(double minX) {
        this.minX = minX;
    }
    
    public double getMaxX() {
        return maxX;
    }
    
    public void setMaxX(double maxX) {
        this.maxX = maxX;
    }
    
    public double getMinY() {
        return minY;
    }
    
    public void setMinY(double minY) {
        this.minY = minY;
    }
    
    public double getMaxY() {
        return maxY;
    }
    
    public void setMaxY(double maxY) {
        this.maxY = maxY;
    }
    
    public boolean isShouldDraw() {
        return shouldDraw;
    }
    
    public void setShouldDraw(boolean shouldDraw) {
        this.shouldDraw = shouldDraw;
    }
}
