package com.gmail.yaroslavlancelot.eafall.game.popup;

import com.gmail.yaroslavlancelot.eafall.game.popup.rolling.IRollingPopup;
import com.gmail.yaroslavlancelot.eafall.game.touch.TouchHelper;

import org.andengine.engine.camera.Camera;
import org.andengine.entity.scene.CameraScene;
import org.andengine.entity.scene.Scene;

/**
 * @author Yaroslav Havrylovych
 */
public class PopupScene extends CameraScene implements IPopup {
    /** true if popup is showing now and false in other way */
    protected boolean mIsPopupShowing;
    /** scene which holding the current one */
    protected Scene mScene;
    /** state change callback */
    protected IRollingPopup.StateChangingListener mStateChangingListener;

    // ===========================================================
    // Constants
    // ===========================================================
    protected PopupScene(Scene scene, Camera camera) {
        super(camera);
        mScene = scene;
        setBackgroundEnabled(false);
        setTouchAreaBindingOnActionDownEnabled(true);
        setOnAreaTouchTraversalFrontToBack();

        TouchHelper.SceneTouchListener sceneTouchListener = createSceneTouchHandler();
        if (sceneTouchListener != null) setOnSceneTouchListener(sceneTouchListener);
    }


    // ===========================================================
    // Fields
    // ===========================================================

    // ===========================================================
    // Constructors
    // ===========================================================

    // ===========================================================
    // Getter & Setter
    // ===========================================================

    // ===========================================================
    // Methods for/from SuperClass/Interfaces
    // ===========================================================

    @Override
    public boolean isShowing() {
        return mIsPopupShowing;
    }

    @Override
    public void setStateChangeListener(StateChangingListener stateChangingListener) {
        mStateChangingListener = stateChangingListener;
    }

    @Override
    public void removeStateChangeListener() {
        mStateChangingListener = null;
    }

    @Override
    public void showPopup() {
        if (isShowing()) {
            return;
        }
        attachPopupScene();
        mIsPopupShowing = true;
        if (mStateChangingListener != null) {
            mStateChangingListener.onShowed();
        }
    }

    @Override
    public void hidePopup() {
        if (!isShowing()) {
            return;
        }
        detachPopupScene();
        mIsPopupShowing = false;
        if (mStateChangingListener != null) {
            mStateChangingListener.onHided();
        }
    }


    // ===========================================================
    // Methods
    // ===========================================================

    protected TouchHelper.SceneTouchListener createSceneTouchHandler() {
        return new TouchHelper.SceneTouchListener() {
            @Override
            public void click() {
                super.click();
                hidePopup();
            }
        };
    }

    /** detach from the screen */
    protected void detachPopupScene() {
        mScene.clearChildScene();
    }

    /** attach to the screen */
    protected void attachPopupScene() {
        mScene.setChildScene(this, false, true, true);
    }

    // ===========================================================
    // Inner and Anonymous Classes
    // ===========================================================
}
