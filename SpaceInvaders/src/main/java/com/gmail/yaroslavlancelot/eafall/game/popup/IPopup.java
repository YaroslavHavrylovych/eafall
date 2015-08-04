package com.gmail.yaroslavlancelot.eafall.game.popup;

import org.andengine.engine.camera.Camera;

/** simplified popup interface */
public interface IPopup {
    boolean isShowing();

    void setCamera(final Camera pCamera);

    void setStateChangingListener(StateChangingListener stateChangingListener);

    void hidePopup();

    void showPopup();

    /** change popup state. Change or hide based on current showing status */
    void triggerPopup();

    interface StateChangingListener {
        void onShowed();

        void onHided();
    }
}
