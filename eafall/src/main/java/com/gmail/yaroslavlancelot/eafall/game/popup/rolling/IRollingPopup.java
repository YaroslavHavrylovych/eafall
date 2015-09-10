package com.gmail.yaroslavlancelot.eafall.game.popup.rolling;

import com.gmail.yaroslavlancelot.eafall.game.popup.IPopup;

/**
 * Popups which can appear by rolling form the side.
 *
 * @author Yaroslav Havrylovych
 */
public interface IRollingPopup extends IPopup {
    boolean isBusy();

    /** change popup state. Change or hide based on current showing status */
    void triggerPopup();
}
