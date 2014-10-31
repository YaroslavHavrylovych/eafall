package com.gmail.yaroslavlancelot.spaceinvaders.popups;

import com.gmail.yaroslavlancelot.spaceinvaders.utils.TouchUtils;

import org.andengine.engine.camera.hud.HUD;
import org.andengine.entity.IEntity;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.scene.ITouchArea;
import org.andengine.entity.scene.Scene;

/** Base popups class. Touch on HUD except any hud element will cause popup closing */
public class PopupHud extends HUD {
    /** represent boolean value which true if popup is showing now and false in other way */
    protected boolean mIsPopupShowing;
    /** area for popup (all other area is transparent) */
    protected Rectangle mPopupRectangle;
    /** popup attached scene */
    private Scene mScene;


    public PopupHud(Scene scene) {
        mScene = scene;
        setBackgroundEnabled(false);
        setTouchAreaBindingOnActionDownEnabled(true);
        setOnAreaTouchTraversalBackToFront();

        setOnSceneTouchListener(new TouchUtils.SceneTouchListener() {
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
    protected synchronized void hidePopup() {
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
    }

    /** detach from the screen */
    public void detachPopup() {
        mScene.clearChildScene();
    }

    /**
     * check if popup is showing and if it's then unregister all touch areas and detach
     * all elements for mPopupRectangle
     */
    protected synchronized void showPopup() {
        if (mIsPopupShowing) {
            return;
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

    /** attach to the screen */
    public void attachPopup() {
        mScene.setChildScene(this);
    }
}
