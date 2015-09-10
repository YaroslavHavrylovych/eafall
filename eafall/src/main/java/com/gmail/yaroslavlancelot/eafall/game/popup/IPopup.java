package com.gmail.yaroslavlancelot.eafall.game.popup;

/**
 * Basic popup functionality
 *
 * @author Yaroslav Havrylovych
 */
public interface IPopup {
    boolean isShowing();

    void setStateChangingListener(StateChangingListener stateChangingListener);

    void showPopup();

    void hidePopup();

    interface StateChangingListener {
        void onShowed();

        void onHided();
    }
}
