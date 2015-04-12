package com.gmail.yaroslavlancelot.eafall.game.entity;

import java.util.WeakHashMap;

public class Area {
    /** Area pool to prevent new areas creation. It's weak do not store it if unneded */
    private static final WeakHashMap<String, Area> sHolder = new WeakHashMap<String, Area>(30);
    public final float left;
    public final float top;
    public final float width;
    public final float height;


    private Area(float left, float top, float width, float height) {
        this.left = left;
        this.top = top;
        this.width = width;
        this.height = height;
    }

    /** trying to reuse existing area. If there is no such areas - creates new */
    public synchronized static Area getArea(float left, float top, float width, float height) {
        String key = "" + left + top + width + height;
        Area value;
        value = sHolder.get(key);
        if (value == null) {
            value = new Area(left, top, width, height);
            sHolder.put(key, value);
        }
        return value;
    }

    public boolean contains(float x, float y) {
        return left < (left + width) && top < (top + height)  // check for empty first
                && x >= left && x < (left + width) && y >= top && y < (top + height);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || !(o instanceof Area)) {
            return false;
        }
        if (super.equals(o)) {
            return true;
        }
        Area area = (Area) o;
        return area.left == left && area.top == top && area.width == width && area.height == height;
    }

    @Override
    public int hashCode() {
        return createHash((int) left, (int) top, (int) width, (int) height);
    }

    /** calculates and returns hash code based on four integers */
    public static int createHash(int left, int top, int width, int height) {
        int hash = 17;
        hash = ((hash + left) << 5) - (hash + left);
        hash = ((hash + top) << 5) - (hash + top);
        hash = ((hash + width) << 5) - (hash + width);
        hash = ((hash + height) << 5) - (hash + height);
        return hash;
    }


    @Override
    public String toString() {
        return "area : [" + (int) left + "," + (int) top + "," + (int) (left + width) + "," + (int) (top + height) + "]";
    }
}
