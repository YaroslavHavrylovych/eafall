package com.gmail.yaroslavlancelot.eafall.game.touch;

import android.view.VelocityTracker;

import com.gmail.yaroslavlancelot.eafall.game.configuration.Config;

import org.andengine.engine.camera.VelocityCamera;
import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.shape.ITouchCallback;
import org.andengine.input.touch.TouchEvent;
import org.andengine.input.touch.detector.PinchZoomDetector;
import org.andengine.input.touch.detector.ScrollDetector;

import java.util.ArrayList;
import java.util.List;

/**
 * Keep the track about touch functionality and can delegate action to
 * concrete handler. In advanced handle scroll and zoom logic.
 */
public class GameSceneTouchListener implements
        IOnSceneTouchListener,
        ICameraCoordinates,
        PinchZoomDetector.IPinchZoomDetectorListener, //zoom
        ScrollDetector.IScrollDetectorListener //scroll
{
    public static final float MAX_ZOOM_FACTOR = Config.getConfig().getMaxZoomFactor();
    public static final float MIN_ZOOM_FACTOR = 1;
    private PinchZoomDetector mZoomDetector;
    private ScrollDetector mScrollDetector;
    private float mInitialTouchZoomFactor;
    private VelocityTracker mVelocityTracker;
    /** camera for moving */
    private VelocityCamera mCamera;
    /** triggers before current handler and can be triggered instead */
    private List<ITouchCallback> mSceneClickListeners = new ArrayList<ITouchCallback>(2);

    public GameSceneTouchListener(VelocityCamera camera) {
        mCamera = camera;
        mZoomDetector = new PinchZoomDetector(this);
        mScrollDetector = new ScrollDetector(this);
    }

    public float getCameraCurrentWidth() {
        return mCamera.getWidth();
    }

    public float getCameraCurrentHeight() {
        return mCamera.getHeight();
    }

    public float getCameraCurrentCenterX() {
        return mCamera.getTargetCenterX();
    }

    public float getCameraCurrentCenterY() {
        return mCamera.getTargetCenterY();
    }

    public float getTargetZoomFactor() {
        return mCamera.getTargetZoomFactor();
    }

    public float getMaxZoomFactorChange() {
        return 5;
    }

    public void registerTouchListener(ITouchCallback touchListener) {
        mSceneClickListeners.add(touchListener);
    }

    public void clearTouchList() {
        mSceneClickListeners.clear();
    }

    @Override
    public boolean onSceneTouchEvent(final Scene pScene, final TouchEvent pSceneTouchEvent) {
        // check if it's click not on empty screen but on some hud element
        for (ITouchCallback touchListener : mSceneClickListeners) {
            if (touchListener.onAreaTouched(pSceneTouchEvent, 0, 0)) {
                return true;
            }
        }

        mZoomDetector.onSceneTouchEvent(pScene, pSceneTouchEvent);
        if (mZoomDetector.isZooming()) {
            mScrollDetector.setEnabled(false);
        } else {
            if (pSceneTouchEvent.isActionDown()) {
                mScrollDetector.setEnabled(true);
            }
            mScrollDetector.onTouchEvent(pSceneTouchEvent);
        }
        return true;
    }

    @Override
    public void onPinchZoomStarted(PinchZoomDetector pPinchZoomDetector, TouchEvent pSceneTouchEvent) {
        mInitialTouchZoomFactor = mCamera.getZoomFactor();
    }

    @Override
    public void onPinchZoom(PinchZoomDetector pPinchZoomDetector, TouchEvent pTouchEvent,
                            float pZoomFactor) {
        mCamera.setZoomFactor(
                StaticHelper.stickToBorderOrLeftValue(
                        mInitialTouchZoomFactor * pZoomFactor,
                        MIN_ZOOM_FACTOR, MAX_ZOOM_FACTOR));
    }

    @Override
    public void onPinchZoomFinished(PinchZoomDetector pPinchZoomDetector, TouchEvent pTouchEvent,
                                    float pZoomFactor) {
        //unused
    }

    @Override
    public void onScrollStarted(ScrollDetector pScollDetector, final TouchEvent pSceneTouchEvent,
                                int pPointerID, float pDistanceX, float pDistanceY) {
        mVelocityTracker = VelocityTracker.obtain();// Used for smooth scroll start
    }

    @Override
    public void onScroll(ScrollDetector pScollDetector, final TouchEvent pSceneTouchEvent,
                         int pPointerID, float pDistanceX, float pDistanceY) {
        // Add movement to velocity
        this.mVelocityTracker.addMovement(pSceneTouchEvent.getMotionEvent());
        // Move camera object (relative to zoom factor)
        final float zoomFactor = getTargetZoomFactor();
        mCamera.offsetCenter(-pDistanceX / zoomFactor, pDistanceY / zoomFactor);
    }

    @Override
    public void onScrollFinished(ScrollDetector pScollDetector, final TouchEvent pSceneTouchEvent,
                                 int pPointerID, float pDistanceX, float pDistanceY) {
        this.mVelocityTracker.computeCurrentVelocity(1000);
        final float velocityX = mVelocityTracker.getXVelocity();
        final float velocityY = mVelocityTracker.getYVelocity();
        final float zoomFactor = getTargetZoomFactor();
        this.mCamera.fling(velocityX / zoomFactor, velocityY / zoomFactor);
        this.mVelocityTracker.recycle();
    }
}
