package com.gmail.yaroslavlancelot.eafall.game.popup;

import com.gmail.yaroslavlancelot.eafall.android.LoggerHelper;
import com.gmail.yaroslavlancelot.eafall.game.touch.StaticHelper;

import org.andengine.engine.camera.hud.HUD;
import org.andengine.entity.IEntity;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.scene.Scene;

/** Base popups class. Touch on HUD except any hud element will cause popup closing */
public abstract class PopupHud extends HUD implements IPopup {
    /** for logs */
    private static final String TAG = PopupHud.class.getCanonicalName();
    /** represent boolean value which true if popup is showing now and false in other way */
    protected boolean mIsPopupShowing;
    /** area for popup (all other area is transparent) */
    //TODO replace it with entity and replace instead of attach
    protected Rectangle mPopupRectangle;
    /** popup attached scene */
    private Scene mScene;
    /** state changing intent */
    private StateChangingListener mStateChangingListener;

    public PopupHud(Scene scene) {
        mScene = scene;
        setBackgroundEnabled(false);
        setTouchAreaBindingOnActionDownEnabled(true);
        setOnAreaTouchTraversalBackToFront();

        setOnSceneTouchListener(new StaticHelper.SceneTouchListener() {
            @Override
            public void click() {
                super.click();
                hidePopup();
            }
        });
    }

    public void setStateChangingListener(StateChangingListener stateChangingListener) {
        mStateChangingListener = stateChangingListener;
    }

    /**
     * check is popup is showing and if it's not detach all elements and unregister
     * all touch areas for mPopupRectangle
     */
    @Override
    public void hidePopup() {
        if (!mIsPopupShowing) {
            return;
        }
        detachPopup();
        IEntity child;
        for (int i = 0; i < mPopupRectangle.getChildCount(); i++) {
            child = mPopupRectangle.getChildByIndex(i);
            unregisterTouchArea(child);
        }
        detachChild(mPopupRectangle);
        unregisterTouchArea(mPopupRectangle);
        mIsPopupShowing = false;
        if (mStateChangingListener != null) {
            mStateChangingListener.onHided();
        }
    }

    /**
     * check if popup is showing and if it's then unregister all touch areas and detach
     * all elements for mPopupRectangle
     */
    @Override
    public void showPopup() {
        if (mIsPopupShowing) {
            return;
        }
        if (mStateChangingListener != null) {
            mStateChangingListener.onShowed();
        }
        attachPopup();
        IEntity child;
        for (int i = 0; i < mPopupRectangle.getChildCount(); i++) {
            child = mPopupRectangle.getChildByIndex(i);
            registerTouchArea(child);
        }
        attachChild(mPopupRectangle);
        registerTouchArea(mPopupRectangle);
        mIsPopupShowing = true;
    }

    /** will showPopup or hidePopup popup depending on current state */
    @Override
    public void triggerPopup() {
        LoggerHelper.printDebugMessage(TAG, "showPopup popup = " + !mIsPopupShowing);
        if (mIsPopupShowing) {
            hidePopup();
        } else {
            showPopup();
        }
    }

    @Override
    public boolean isShowing() {
        return mIsPopupShowing;
    }

    /** detach from the screen */
    protected void detachPopup() {
        mScene.clearChildScene();
    }

    /** attach to the screen */
    protected void attachPopup() {
        mScene.setChildScene(this);
    }
}
