package com.gmail.yaroslavlancelot.spaceinvaders.popups.objectdescription.updater;

import org.andengine.entity.shape.RectangularShape;

/** used for update object description */
public interface DescriptionUpdater {
    /** update object image */
    void updateImage(RectangularShape drawArea, int objectId, String raceName, String teamName);

    /** update object description */
    void updateDescription(RectangularShape drawArea, int objectId, String raceName, String teamName);

    /** update addition information area */
    void updateAdditionInfo(RectangularShape drawArea, int objectId, String raceName, String teamName);

    /** clear description popup */
    void clear();

    /** currently use it for getting drawArea coordinates to register touch areas on screen */
    void initDescriptionArea(float offsetX, float offsetY);
}
