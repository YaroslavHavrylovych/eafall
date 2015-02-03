package com.gmail.yaroslavlancelot.eafall.game.popup;

import com.gmail.yaroslavlancelot.eafall.game.touch.StaticHelper;
import com.gmail.yaroslavlancelot.eafall.android.LoggerHelper;

import org.andengine.engine.camera.hud.HUD;
import org.andengine.entity.IEntity;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.scene.ITouchArea;
import org.andengine.entity.scene.Scene;

/** Base popups class. Touch on HUD except any hud element will cause popup closing */
public abstract class PopupHud extends HUD implements IPopup {
    /** for logs */
    private static final String TAG = PopupHud.class.getCanonicalName();
    /** represent boolean value which true if popup is showing now and false in other way */
    protected boolean mIsPopupShowing;
    /** area for popup (all other area is transparent) */
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

    /**
     * check is popup is showing and if it's not detach all elements and unregister
     * all touch areas for mPopupRectangle
     */
    @Override
    public synchronized void hidePopup() {
        if (!mIsPopupShowing) {
            return;
        }
        detachPopup();
        IEntity child;
        for (int i = 0; i < mPopupRectangle.getChildCount(); i++) {
            child = mPopupRectangle.getChildByIndex(i);
            if (child instanceof ITouchArea) {
                unregisterTouchArea((ITouchArea) child);
            }
        }
        detachChild(mPopupRectangle);
        unregisterTouchArea(mPopupRectangle);
        mIsPopupShowing = false;
        if (mStateChangingListener != null) {
            mStateChangingListener.afterHiding();
        }
    }

    /** detach from the screen */
    protected void detachPopup() {
        mScene.clearChildScene();
    }

    /**
     * check if popup is showing and if it's then unregister all touch areas and detach
     * all elements for mPopupRectangle
     */
    @Override
    public synchronized void showPopup() {
        if (mIsPopupShowing) {
            return;
        }
        if (mStateChangingListener != null) {
            mStateChangingListener.beforeShowing();
        }
        attachPopup();
        IEntity child;
        for (int i = 0; i < mPopupRectangle.getChildCount(); i++) {
            child = mPopupRectangle.getChildByIndex(i);
            if (child instanceof ITouchArea) {
                registerTouchArea((ITouchArea) child);
            }
        }
        attachChild(mPopupRectangle);
        registerTouchArea(mPopupRectangle);
        mIsPopupShowing = true;
    }

    /** will showPopup or hidePopup popup depending on current state */
    @Override
    public synchronized void triggerPopup() {
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

    /** attach to the screen */
    protected void attachPopup() {
        mScene.setChildScene(this);
    }

    public void setStateChangingListener(StateChangingListener stateChangingListener) {
        mStateChangingListener = stateChangingListener;
    }

    public static interface StateChangingListener {
        void beforeShowing();

        void afterHiding();
    }
}
