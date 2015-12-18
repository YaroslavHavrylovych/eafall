package com.yaroslavlancelot.eafall.game.popup;

/**
 * Basic popup functionality
 *
 * @author Yaroslav Havrylovych
 */
public interface IPopup {
    boolean isShowing();

    void setStateChangeListener(StateChangingListener stateChangingListener);

    void removeStateChangeListener();

    void showPopup();

    void hidePopup();

    interface StateChangingListener {
        void onShowed();

        void onHided();
    }
}
