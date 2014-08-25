package com.gmail.yaroslavlancelot.spaceinvaders.popups.objectdescription;

import org.andengine.entity.shape.RectangularShape;

/** mark that class have something to draw (within giving bounds) */
public interface Drawer {
    /** all operations should be in this area */
    void draw(RectangularShape area);
}
