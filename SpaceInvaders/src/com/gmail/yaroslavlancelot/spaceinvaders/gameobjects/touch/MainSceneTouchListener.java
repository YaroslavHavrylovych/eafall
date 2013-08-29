package com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.touch;

import android.content.Context;
import android.view.ScaleGestureDetector;
import com.gmail.yaroslavlancelot.spaceinvaders.utils.TouchUtils;
import org.andengine.engine.camera.SmoothCamera;
import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.Scene;
import org.andengine.input.touch.TouchEvent;

public class MainSceneTouchListener implements IOnSceneTouchListener {
    /** previous event abscissa */
    private float yPos;
    /** previous event ordinate */
    private float xPos;
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

    public MainSceneTouchListener(SmoothCamera camera, Context context) {
        mCamera = camera;
        mMapZoomScaleGestureDetector = new ScaleGestureDetector(context, new MapZoomScaleGestureDetector());
        mCameraWidth = mCamera.getWidth();
        mCameraHeight = mCamera.getHeight();
    }

    @Override
    public boolean onSceneTouchEvent(final Scene pScene, final TouchEvent pSceneTouchEvent) {
        //zoom
        mMapZoomScaleGestureDetector.onTouchEvent(pSceneTouchEvent.getMotionEvent());
        if (pSceneTouchEvent.getMotionEvent().getPointerCount() >= 2)
            return mIsInPreviousEventWasMoreThanOneFinger = true;

        if (mIsInPreviousEventWasMoreThanOneFinger && pSceneTouchEvent.getAction() != TouchEvent.ACTION_DOWN) {
            xPos = pSceneTouchEvent.getX();
            yPos = pSceneTouchEvent.getY();
            mIsInPreviousEventWasMoreThanOneFinger = false;
            return true;
        }

        // moving
        switch (pSceneTouchEvent.getAction()) {
            case TouchEvent.ACTION_DOWN:
                xPos = pSceneTouchEvent.getX();
                yPos = pSceneTouchEvent.getY();
                break;
            case TouchEvent.ACTION_CANCEL:
            case TouchEvent.ACTION_MOVE:
                float newPositionX = mCamera.getCenterX() + (xPos - pSceneTouchEvent.getX()),
                        newPositionY = mCamera.getCenterY() + (yPos - pSceneTouchEvent.getY());

                newPositionX = TouchUtils.stickToBorderOrLeftValue(newPositionX, 0, mCameraWidth);
                newPositionY = TouchUtils.stickToBorderOrLeftValue(newPositionY, 0, mCameraHeight);

                if (xPos != newPositionX || yPos != newPositionY) {
                    mCamera.setCenter(newPositionX, newPositionY);
                    xPos = pSceneTouchEvent.getX();
                    yPos = pSceneTouchEvent.getY();
                }
                break;
        }
        return true;
    }

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
