package com.gmail.yaroslavlancelot.spaceinvaders.popups.objectdescription.updater;

import org.andengine.entity.shape.RectangularShape;
import org.andengine.entity.text.Text;

/** used for update object description */
public interface DescriptionUpdater {
    /** update object image */
    void updateImage(RectangularShape drawArea, int objectId, String raceName, String teamName);

    /** update object description */
    void updateDescription(RectangularShape drawArea, int objectId, String raceName, String teamName);

    /** update addition information area */
    void updateAdditionInfo(RectangularShape drawArea, int objectId, String raceName, String teamName);

    /** update described object name */
    void updateObjectNameText(Text text, int objectId, String raceName);

    /** clear description popup */
    void clear();
}
