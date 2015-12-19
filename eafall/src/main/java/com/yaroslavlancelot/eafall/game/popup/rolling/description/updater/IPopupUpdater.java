package com.yaroslavlancelot.eafall.game.popup.rolling.description.updater;

import org.andengine.entity.shape.Shape;
import org.andengine.entity.sprite.ButtonSprite;
import org.andengine.entity.text.Text;

/** used for update object description popup */
public interface IPopupUpdater {
    /** update object image */
    void updateImage(Shape drawArea, Object objectId, String allianceName, String playerName);

    /** update object description */
    void updateDescription(Shape drawArea, Object objectId, String allianceName, String playerName);

    /** update addition information area */
    void updateAdditionInfo(Shape drawArea, Object objectId, String allianceName, String playerName);

    /** update described object name */
    void updateObjectNameText(Text text, Object objectId, String allianceName);

    /** updates (change visibility) of header buttons */
    void updateHeaderButtons(ButtonSprite leftArrow, ButtonSprite rightArrow, Object objectId, String playerName);

    /** clear description popup */
    void clear();

    /** updates only description area of the description popup */
    interface IDescriptionAreaUpdater {
        /** update description area (and attach all items) */
        void updateDescription(Shape drawArea, final Object objectId,
                               final String allianceName, final String playerName);

        /** detach all objects (e.g. popup hidden) */
        void clearDescription();
    }
}
