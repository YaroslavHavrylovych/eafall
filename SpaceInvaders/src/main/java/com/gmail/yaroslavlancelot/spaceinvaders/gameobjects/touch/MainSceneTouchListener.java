package com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.touch;

import android.content.Context;
import android.view.ScaleGestureDetector;
import com.gmail.yaroslavlancelot.spaceinvaders.utils.TouchUtils;
import org.andengine.engine.camera.SmoothCamera;
import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.Scene;
import org.andengine.input.touch.TouchEvent;

/** main game activity touch listener */
public class MainSceneTouchListener implements IOnSceneTouchListener {
    private final float mScreenToSceneRatio;
    /** previous event abscissa */
    private float mTouchY;
    /** previous event ordinate */
    private float mTouchX;
    /** if user want to move camera after taking one finger from the screen just after zoom */
    private boolean mIsInPreviousEventWasMoreThanOneFinger = false;
    /** camera width */
    private float mCameraWidth;
    /** camera height */
    private float mCameraHeight;
    /** using for handling zoo (with two fingers) events */
    private ScaleGestureDetector mMapZoomScaleGestureDetector;
    /** camera for moving */
    private SmoothCamera mCamera;

    public MainSceneTouchListener(SmoothCamera camera, Context context, float screenToSceneRatio) {
        mCamera = camera;
        mMapZoomScaleGestureDetector = new ScaleGestureDetector(context, new MapZoomScaleGestureDetector());
        mCameraWidth = mCamera.getWidth();
        mCameraHeight = mCamera.getHeight();
        mScreenToSceneRatio = screenToSceneRatio;
    }

    @Override
    public boolean onSceneTouchEvent(final Scene pScene, final TouchEvent pSceneTouchEvent) {
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
        switch (pSceneTouchEvent.getAction()) {
            case TouchEvent.ACTION_DOWN:
                mTouchX = motionEventX;
                mTouchY = motionEventY;
                break;
            case TouchEvent.ACTION_CANCEL:
            case TouchEvent.ACTION_MOVE:
                float newPositionX = mCamera.getCenterX() + (mTouchX - motionEventX) / mCamera.getZoomFactor() / mScreenToSceneRatio,
                        newPositionY = mCamera.getCenterY() + (mTouchY - motionEventY) / mCamera.getZoomFactor() / mScreenToSceneRatio;
                newPositionX = TouchUtils.stickToBorderOrLeftValue(newPositionX, 0, mCameraWidth);
                newPositionY = TouchUtils.stickToBorderOrLeftValue(newPositionY, 0, mCameraHeight);
                if (mTouchX != newPositionX || mTouchY != newPositionY) {
                    mCamera.setCenter(newPositionX, newPositionY);
                    mTouchX = motionEventX;
                    mTouchY = motionEventY;
                }
                break;
        }
        return true;
    }

    /** used to perform scaling operations */
    private class MapZoomScaleGestureDetector extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        private float mMaximumZoomFactor = 2.f;
        private float mMinimumZoomFactor = 1.f;

        @Override
        public boolean onScale(final ScaleGestureDetector detector) {
            float scaleFactor = detector.getScaleFactor();
            float nextZoomFactor = mCamera.getZoomFactor() * scaleFactor;
            nextZoomFactor = TouchUtils.stickToBorderOrLeftValue(nextZoomFactor, mMinimumZoomFactor, mMaximumZoomFactor);
            if (nextZoomFactor != mCamera.getZoomFactor())
                mCamera.setZoomFactor(nextZoomFactor);
            return true;
        }
    }
}
