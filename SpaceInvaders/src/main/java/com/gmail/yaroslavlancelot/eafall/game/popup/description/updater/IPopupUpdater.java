package com.gmail.yaroslavlancelot.eafall.game.popup.description.updater;

import org.andengine.entity.shape.Shape;
import org.andengine.entity.text.Text;

/** used for update object description popup */
public interface IPopupUpdater {
    /** update object image */
    void updateImage(Shape drawArea, Object objectId, String raceName, String teamName);

    /** update object description */
    void updateDescription(Shape drawArea, Object objectId, String raceName, String teamName);

    /** update addition information area */
    void updateAdditionInfo(Shape drawArea, Object objectId, String raceName, String teamName);

    /** update described object name */
    void updateObjectNameText(Text text, Object objectId, String raceName);

    /** clear description popup */
    void clear();

    /** updates only description area of the description popup */
    public static interface IDescriptionAreaUpdater {
        /** update description area (and attach all items) */
        void updateDescription(Shape drawArea, final Object objectId,
                               final String raceName, final String teamName);

        /** detach all objects (e.g. popup hidden) */
        void clearDescription();
    }
}
