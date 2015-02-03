package com.gmail.yaroslavlancelot.eafall.game.popup;

import org.andengine.engine.camera.Camera;

/** simplified popup interface */
public interface IPopup {
    void hidePopup();

    void showPopup();

    /** change popup state. Change or hide based on current showing status */
    void triggerPopup();

    boolean isShowing();

    void setCamera(final Camera pCamera);
}
