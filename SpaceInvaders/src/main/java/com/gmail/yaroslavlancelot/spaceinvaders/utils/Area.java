package com.gmail.yaroslavlancelot.spaceinvaders.utils;

public class Area {
    public final float left;
    public final float top;
    public final float width;
    public final float height;


    public Area(final float left, final float top, final float width, final float height) {
        this.left = left;
        this.top = top;
        this.width = width;
        this.height = height;
    }

    public boolean contains(float x, float y) {
        return left < (left + width) && top < (top + height)  // check for empty first
                && x >= left && x < (left + width) && y >= top && y < (top + height);
    }

    @Override
    public String toString() {
        return "area : [" + (int)left + "," + (int)top + "," + (int)(left + width) + "," + (int)(top + height) + "]";
    }
}
