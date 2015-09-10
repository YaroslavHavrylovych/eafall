package com.gmail.yaroslavlancelot.eafall.game.popup.rolling;

import com.gmail.yaroslavlancelot.eafall.android.LoggerHelper;
import com.gmail.yaroslavlancelot.eafall.game.popup.PopupScene;

import org.andengine.engine.camera.Camera;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.Sprite;

/** Base popups class. Touch on HUD except any hud element will cause popup closing */
public abstract class RollingPopup extends PopupScene implements IRollingPopup {
    private static final String TAG = RollingPopup.class.getCanonicalName();
    protected Sprite mBackgroundSprite;

    public RollingPopup(Scene scene, Camera camera) {
        super(scene, camera);
    }

    @Override
    public boolean isBusy() {
        return false;
    }

    @Override
    public void triggerPopup() {
        LoggerHelper.printDebugMessage(TAG, "showPopup popup = " + !mIsPopupShowing);
        if (mIsPopupShowing) {
            hidePopup();
        } else {
            showPopup();
        }
    }
}
