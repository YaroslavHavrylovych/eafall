package com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.touch;

import android.content.Context;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;

import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.ICameraCoordinates;
import com.gmail.yaroslavlancelot.spaceinvaders.utils.TouchUtils;

import org.andengine.engine.camera.SmoothCamera;
import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.shape.ITouchCallback;
import org.andengine.input.touch.TouchEvent;

import java.util.ArrayList;
import java.util.List;

/** main game activity touch listener */
public class MainSceneTouchListener implements IOnSceneTouchListener, ICameraCoordinates {
    private final float mScreenToSceneRatio;
    /** previous event abscissa */
    private float mTouchY;
    /** previous event ordinate */
    private float mTouchX;
    /** if user want to move camera after taking one finger from the screen just after zoom */
    private boolean mIsInPreviousEventWasMoreThanOneFinger = false;
    /** camera width */
    private float mCameraMaxWidth;
    /** camera height */
    private float mCameraMaxHeight;
    /** camera current width */
    private float mCameraCurrentWidth;
    /** camera current height */
    private float mCameraCurrentHeight;
    /** camera current width */
    private float mCameraCurrentCenterX;
    /** camera current height */
    private float mCameraCurrentCenterY;
    /** using for handling zoo (with two fingers) events */
    private ScaleGestureDetector mMapZoomScaleGestureDetector;
    /** camera for moving */
    private SmoothCamera mCamera;
    /** additional touch listeners that will be invoked after this touch listener finish it's work handling */
    private List<ITouchCallback> mSceneClickListeners = new ArrayList<ITouchCallback>(2);

    public MainSceneTouchListener(SmoothCamera camera, Context context, float screenToSceneRatio) {
        mCamera = camera;
        mMapZoomScaleGestureDetector = new ScaleGestureDetector(context, new MapZoomScaleGestureDetector());
        mCameraCurrentWidth = mCameraMaxWidth = mCamera.getWidth();
        mCameraCurrentHeight = mCameraMaxHeight = mCamera.getHeight();
        mCameraCurrentCenterX = mCamera.getCenterX();
        mCameraCurrentCenterY = mCamera.getCenterY();

        mScreenToSceneRatio = screenToSceneRatio;
    }

    public float getCameraCurrentWidth() {
        return mCameraCurrentWidth;
    }

    public float getCameraCurrentHeight() {
        return mCameraCurrentHeight;
    }

    public float getCameraCurrentCenterX() {
        return mCameraCurrentCenterX;
    }

    public float getCameraCurrentCenterY() {
        return mCameraCurrentCenterY;
    }

    public float getTargetZoomFactor() {
        return mCamera.getTargetZoomFactor();
    }

    public float getMaxZoomFactorChange() {
        return mCamera.getMaxZoomFactorChange();
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

        //zoom
        mMapZoomScaleGestureDetector.onTouchEvent(pSceneTouchEvent.getMotionEvent());
        if (pSceneTouchEvent.getMotionEvent().getPointerCount() >= 2)
            return mIsInPreviousEventWasMoreThanOneFinger = true;
        float motionEventX = pSceneTouchEvent.getMotionEvent().getX();
        float motionEventY = pSceneTouchEvent.getMotionEvent().getY();
        if (mIsInPreviousEventWasMoreThanOneFinger && pSceneTouchEvent.getAction() != TouchEvent.ACTION_DOWN) {
            mTouchX = motionEventX;
            mTouchY = motionEventY;
            mIsInPreviousEventWasMoreThanOneFinger = false;
            return true;
        }

        // moving
        switch (pSceneTouchEvent.getMotionEvent().getAction()) {
            case MotionEvent.ACTION_DOWN:
                mTouchX = motionEventX;
                mTouchY = motionEventY;
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_MOVE:
                float newPositionX = mCamera.getCenterX() + (mTouchX - motionEventX) / mCamera.getZoomFactor() / mScreenToSceneRatio,
                        newPositionY = mCamera.getCenterY() + (mTouchY - motionEventY) / mCamera.getZoomFactor() / mScreenToSceneRatio;
                newPositionX = TouchUtils.stickToBorderOrLeftValue(newPositionX, 0, mCameraMaxWidth);
                newPositionY = TouchUtils.stickToBorderOrLeftValue(newPositionY, 0, mCameraMaxHeight);
                if (mCameraCurrentCenterX != newPositionX || mCameraCurrentCenterY != newPositionY) {
                    mCamera.setCenter(newPositionX, newPositionY);
                    mTouchX = motionEventX;
                    mTouchY = motionEventY;
                    mCameraCurrentCenterX = newPositionX;
                    mCameraCurrentCenterY = newPositionY;
                }
                break;
        }

        return true;
    }

    /** used to perform scaling operations */
    private class MapZoomScaleGestureDetector extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        private float mMaximumZoomFactor = 3.f;
        private float mMinimumZoomFactor = 1.f;

        @Override
        public boolean onScale(final ScaleGestureDetector detector) {
            float scaleFactor = detector.getScaleFactor();
            float nextZoomFactor = mCamera.getZoomFactor() * scaleFactor;
            nextZoomFactor = TouchUtils.stickToBorderOrLeftValue(nextZoomFactor, mMinimumZoomFactor, mMaximumZoomFactor);
            if (nextZoomFactor != mCamera.getZoomFactor())
                mCamera.setZoomFactor(nextZoomFactor);
            mCameraCurrentWidth = mCamera.getWidth();
            mCameraCurrentWidth = mCamera.getHeight();
            return true;
        }
    }
}
