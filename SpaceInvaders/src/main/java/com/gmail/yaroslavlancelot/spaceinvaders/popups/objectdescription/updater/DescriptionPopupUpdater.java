package com.gmail.yaroslavlancelot.spaceinvaders.popups.objectdescription.updater;

import org.andengine.entity.shape.RectangularShape;
import org.andengine.entity.text.Text;

/** used for update object description popup */
public interface DescriptionPopupUpdater {
    /** update object image */
    void updateImage(RectangularShape drawArea, Object objectId, String raceName, String teamName);

    /** update object description */
    void updateDescription(RectangularShape drawArea, Object objectId, String raceName, String teamName);

    /** update addition information area */
    void updateAdditionInfo(RectangularShape drawArea, Object objectId, String raceName, String teamName);

    /** update described object name */
    void updateObjectNameText(Text text, Object objectId, String raceName);

    /** clear description popup */
    void clear();

    /** updates only description area of the description popup */
    public static interface DescriptionAreaUpdater {
        /** update description area (and attach all items) */
        void updateDescription(RectangularShape drawArea, final Object objectId,
                               final String raceName, final String teamName);

        /** detach all objects (e.g. popup hidden) */
        void clearDescription();
    }
}
