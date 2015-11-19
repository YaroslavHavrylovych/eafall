package com.gmail.yaroslavlancelot.eafall.game.touch;

import android.view.MotionEvent;
import android.view.VelocityTracker;

import com.gmail.yaroslavlancelot.eafall.EaFallApplication;
import com.gmail.yaroslavlancelot.eafall.game.camera.EaFallCamera;

import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.Scene;
import org.andengine.input.touch.TouchEvent;
import org.andengine.input.touch.detector.PinchZoomDetector;
import org.andengine.input.touch.detector.ScrollDetector;
import org.andengine.util.Constants;
import org.andengine.util.math.MathUtils;

/**
 * Keep the track about touch functionality and can delegate action to
 * concrete handler (Scroll, Pinch zoom etc). Handle camera-on-the-scene positions etc.
 *
 * @author Yaroslav Havrylovych
 */
public class GameSceneHandler implements
        IOnSceneTouchListener,
        ICameraHandler,
        EaFallCamera.ICameraMoveCallbacks,
        PinchZoomDetector.IPinchZoomDetectorListener, //zoom
        ScrollDetector.IScrollDetectorListener //scroll
{
    // ===========================================================
    // Constants
    // ===========================================================

    public static final float MAX_ZOOM_FACTOR = EaFallApplication.getConfig().getMaxZoomFactor();
    public static final float MIN_ZOOM_FACTOR = 1;

    // ===========================================================
    // Fields
    // ===========================================================

    /** camera for moving */
    private EaFallCamera mCamera;
    /*
     * Pinch Zoom
     */
    private PinchZoomDetector mZoomDetector;
    private float mInitialTouchZoomFactor;
    //surface coordinates
    private float[] mSurface_ZoomCenterPosition = new float[2];
    private float[] mSurface_CameraCenterPosition = new float[2];
    private float[] mCamera_ZoomCenterPosition = new float[2];
    private float[] mCamera_CameraCenterPosition = new float[2];
    /*
     * Scroll
     */
    private ScrollDetector mScrollDetector;
    private VelocityTracker mVelocityTracker;

    // ===========================================================
    // Constructors
    // ===========================================================

    public GameSceneHandler(EaFallCamera camera) {
        mCamera = camera;
        camera.setCenterChangedCallback(this);
        //scroll
        mScrollDetector = new ScrollDetector(this);
        //zoom
        mZoomDetector = new PinchZoomDetector(this);
        initPinchZoomMinimumDistance(mZoomDetector);
    }


    // ===========================================================
    // Getter & Setter
    // ===========================================================

    protected float getCenterY() {
        return mCamera.getCenterY();
    }

    @Override
    public float getMaxX() {
        return mCamera.getXMax();
    }

    @Override
    public float getMaxY() {
        return mCamera.getYMax();
    }

    @Override
    public float getMinX() {
        return mCamera.getXMin();
    }

    @Override
    public float getMinY() {
        return mCamera.getYMin();
    }

    @Override
    public float getZoomFactor() {
        return mCamera.getZoomFactor();
    }

    private void setZoomFactor(float zoomFactor) {
        mCamera.setZoomFactor(zoomFactor);
    }


    // ===========================================================
    // Methods for/from SuperClass/Interfaces
    // ===========================================================

    @Override
    public boolean onSceneTouchEvent(final Scene pScene, final TouchEvent pSceneTouchEvent) {
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
        //zoom center in screen coordinates
        MotionEvent motionEvent = pSceneTouchEvent.getMotionEvent();
        mSurface_ZoomCenterPosition[Constants.VERTEX_INDEX_X]
                = (motionEvent.getX(0) + motionEvent.getX(1)) / 2;
        mSurface_ZoomCenterPosition[Constants.VERTEX_INDEX_Y]
                = (motionEvent.getY(0) + motionEvent.getY(1)) / 2;
        //coordinates conversion
        TouchHelper.convertScreenToSurfaceCoordinates(mCamera,
                mSurface_ZoomCenterPosition, mSurface_ZoomCenterPosition);
    }

    @Override
    public void onPinchZoom(PinchZoomDetector pPinchZoomDetector, TouchEvent pTouchEvent,
                            float pZoomFactor) {
        float newZoom = MathUtils.bringToBounds(MIN_ZOOM_FACTOR, MAX_ZOOM_FACTOR,
                mInitialTouchZoomFactor * pZoomFactor);
        float zoomChange = newZoom - mCamera.getZoomFactor();
        //if zoomChange < 0 (zoom out) then this method works strange (and unneeded in general)
        if (zoomChange > 0) {
            repositionCameraWithZoom(zoomChange);
        }
        setZoomFactor(newZoom);
    }

    @Override
    public void onPinchZoomFinished(PinchZoomDetector pPinchZoomDetector, TouchEvent pTouchEvent,
                                    float pZoomFactor) {
        //still nothing here
    }

    @Override
    public void onScrollStarted(ScrollDetector pScrollDetector, final TouchEvent pSceneTouchEvent,
                                int pPointerID, float pDistanceX, float pDistanceY) {
        mVelocityTracker = VelocityTracker.obtain();// Used for smooth scroll start
    }

    @Override
    public void onScroll(ScrollDetector pScrollDetector, final TouchEvent pSceneTouchEvent,
                         int pPointerID, float pDistanceX, float pDistanceY) {
        // Add movement to velocity
        this.mVelocityTracker.addMovement(pSceneTouchEvent.getMotionEvent());
        // Move camera object (relative to zoom factor)
        final float zoomFactor = getZoomFactor();
        mCamera.offsetCenter(-pDistanceX / zoomFactor, pDistanceY / zoomFactor);
    }

    @Override
    public void onScrollFinished(ScrollDetector pScrollDetector, final TouchEvent pSceneTouchEvent,
                                 int pPointerID, float pDistanceX, float pDistanceY) {
        this.mVelocityTracker.computeCurrentVelocity(1000);
        final float velocityX = mVelocityTracker.getXVelocity();
        final float velocityY = mVelocityTracker.getYVelocity();
        final float zoomFactor = getZoomFactor();
        this.mCamera.fling(velocityX / zoomFactor, velocityY / zoomFactor);
        this.mVelocityTracker.recycle();
    }

    @Override
    public void cameraMove(final float deltaX, final float deltaY) {
        //unused here
    }

    // ===========================================================
    // Methods
    // ===========================================================

    public boolean smoothZoomInProgress() {
        return mCamera.isSmoothZoomInProgress();
    }

    /**
     * Set camera center in closer to zoom center position when we zoom-in
     * TODO later pay attention tha zoom center point has always to be under the two fingers it will change the logic/math of this method
     *
     * @param zoomChange changes in zoom factor between previous and new zoom
     */
    private void repositionCameraWithZoom(float zoomChange) {
        //camera zoom center
        TouchHelper.convertSurfaceToCameraCoordinates(mCamera,
                mSurface_ZoomCenterPosition, mCamera_ZoomCenterPosition);
        //camera center
        mSurface_CameraCenterPosition[Constants.VERTEX_INDEX_X] =
                mCamera.getCenterX();
        mSurface_CameraCenterPosition[Constants.VERTEX_INDEX_Y] =
                mCamera.getCenterY();
        TouchHelper.convertSurfaceToCameraCoordinates(mCamera,
                mSurface_CameraCenterPosition, mCamera_CameraCenterPosition);
        //camera calculate next camera center
        mSurface_CameraCenterPosition[Constants.VERTEX_INDEX_X] =
                mSurface_CameraCenterPosition[Constants.VERTEX_INDEX_X] +
                        (mCamera_ZoomCenterPosition[Constants.VERTEX_INDEX_X]
                                - mCamera_CameraCenterPosition[Constants.VERTEX_INDEX_X]) * zoomChange;
        mSurface_CameraCenterPosition[Constants.VERTEX_INDEX_Y] =
                mSurface_CameraCenterPosition[Constants.VERTEX_INDEX_Y] +
                        (mCamera_ZoomCenterPosition[Constants.VERTEX_INDEX_Y]
                                - mCamera_CameraCenterPosition[Constants.VERTEX_INDEX_Y]) * zoomChange;
        //surface camera center position
        mCamera.setCenter(mSurface_CameraCenterPosition[Constants.VERTEX_INDEX_X],
                mSurface_CameraCenterPosition[Constants.VERTEX_INDEX_Y]);
    }

    private static void initPinchZoomMinimumDistance(PinchZoomDetector zoomDetector) {
        zoomDetector.setTriggerPinchZoomMinimumDistance(EaFallApplication.getConfig().getDisplayWidth() / 25);
    }

    // ===========================================================
    // Inner and Anonymous Classes
    // ===========================================================

}
