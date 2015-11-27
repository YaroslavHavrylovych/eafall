package com.gmail.yaroslavlancelot.eafall.game.touch;

import com.gmail.yaroslavlancelot.eafall.EaFallApplication;
import com.gmail.yaroslavlancelot.eafall.game.configuration.IConfig;

import org.andengine.engine.camera.Camera;
import org.andengine.entity.IEntity;
import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.shape.ITouchCallback;
import org.andengine.input.touch.TouchEvent;
import org.andengine.input.touch.detector.ClickDetector;
import org.andengine.util.Constants;

/** Helping functions to work with touch events */
public final class TouchHelper {
    /** Amount of time when clicks are treated as separated instead of a single series. In milliseconds. */
    public static volatile int mMultipleClickDividerTime = 1500; //ms
    /** Amount of time when clicks are treated as a single series. In milliseconds */
    public static volatile int mMultipleClickTime = 600; //ms
    /** Time to show hint after the first click in a multiple click series. In seconds */
    public static volatile int mMultipleClickHintTime = 1; //s

    private TouchHelper() {
    }

    /**
     * Converts screen coordinates (e.g. from motion event) to surface coordinates from camera.
     * <br/>
     * Pay attention that screen coordinates begin point is top-left meanwhile for surface
     * it's bottoms-left
     *
     * @param camera             surface of which will be used to get bounds
     * @param screenCoordinates  point screen coordinates
     * @param surfaceCoordinates calculation result
     */
    public static void convertScreenToSurfaceCoordinates(final Camera camera,
                                                         final float[] screenCoordinates,
                                                         final float[] surfaceCoordinates) {
        IConfig config = EaFallApplication.getConfig();
        //abscissa
        surfaceCoordinates[Constants.VERTEX_INDEX_X] = camera.getXMin()
                + screenCoordinates[Constants.VERTEX_INDEX_X] * camera.getWidth()
                / config.getDisplayWidth();
        //ordinate
        float screenHeight = config.getDisplayHeight();
        surfaceCoordinates[Constants.VERTEX_INDEX_Y] = camera.getYMin()
                + (screenHeight - screenCoordinates[Constants.VERTEX_INDEX_Y])
                * camera.getHeight() / screenHeight;
    }

    /**
     * Converts camera surface coordinates to local camera coordinates
     *
     * @param camera             surface of which will be used to get bounds
     * @param surfaceCoordinates point surface coordinates
     * @param cameraCoordinates  calculation result
     */
    public static void convertSurfaceToCameraCoordinates(final Camera camera,
                                                         final float[] surfaceCoordinates,
                                                         final float[] cameraCoordinates) {
        cameraCoordinates[Constants.VERTEX_INDEX_X] =
                surfaceCoordinates[Constants.VERTEX_INDEX_X] - camera.getXMin();
        cameraCoordinates[Constants.VERTEX_INDEX_Y] =
                surfaceCoordinates[Constants.VERTEX_INDEX_Y] - camera.getYMin();
    }

    /**
     * Converts local camera coordinates to surface coordinates
     *
     * @param camera             surface of which will be used to get bounds
     * @param cameraCoordinates  point in camera local coordinates
     * @param surfaceCoordinates calculation result
     */
    public static void convertCameraToSurfaceCoordinates(final Camera camera,
                                                         final float[] cameraCoordinates,
                                                         final float[] surfaceCoordinates) {
        surfaceCoordinates[Constants.VERTEX_INDEX_X] =
                camera.getXMin() + cameraCoordinates[Constants.VERTEX_INDEX_X];
        surfaceCoordinates[Constants.VERTEX_INDEX_Y] =
                camera.getYMin() + cameraCoordinates[Constants.VERTEX_INDEX_Y];
    }

    public interface OnClickListener {
        void onClick();
    }

    /**
     * Touch listener for {@link Scene} with general touch operations callbacks
     * (i.e. pressed, unpressed, clicked).
     */
    public static class SceneTouchListener implements IOnSceneTouchListener {
        /** true if current touch is simple click (just press down and up) */
        private boolean mIsItClickEvent;

        @Override
        public boolean onSceneTouchEvent(Scene pScene, TouchEvent event) {
            if (event.isActionDown()) {
                press();
            } else if (event.isActionCancel()) {
                unPress();
            } else if (event.isActionUp() && mIsItClickEvent) {
                click();
            }
            return true;
        }

        public void press() {
            mIsItClickEvent = true;
        }

        public void unPress() {
            mIsItClickEvent = false;
        }

        public void click() {
            unPress();
        }
    }

    /**
     * Touch listener for {@link IEntity} with general touch operations callbacks
     * (i.e. pressed, unpressed, clicked).
     * <br/>
     * If you want to override any of them you have to invoke super method to prevent
     * breaking the general logic.
     */
    public static class EntityCustomTouch implements ITouchCallback {
        /** used to find out is you move in bounds or not */
        protected IEntity mObject;
        /** true if current touch is simple click (just press down and up) */
        private boolean mIsItClickEvent;


        /** parameters description you can see in javadoc to fields of this class */
        public EntityCustomTouch(IEntity object) {
            mObject = object;
        }

        @Override
        public boolean onAreaTouched(final TouchEvent event, float touchAreaLocalX, float touchAreaLocalY) {
            if (event.isActionDown()) {
                press();
            } else if (event.isActionCancel() || !mObject.contains(event.getX(), event.getY())) {
                unPress();
            } else if (event.isActionUp() && mIsItClickEvent) {
                click();
            }
            return true;
        }

        /** element was pressed callback */
        public void press() {
            mIsItClickEvent = true;
        }

        /** callback after user press element but then cancel press with moving outside of the element borders */
        public void unPress() {
            mIsItClickEvent = false;
        }

        /** callback after click on element happens. User touch down and up finger on element without cancelling or move outside */
        public void click() {
            unPress();
        }
    }

    /** Gives click callback without bounding an event. */
    public static class UnboundedClickListener implements ITouchCallback {
        private ClickDetector.IClickDetectorListener mClickListener;
        private ClickDetector mClickDetector;

        /**
         * Unbounded click listener constructor
         *
         * @param clickListener click listener to trigger
         */
        public UnboundedClickListener(ClickDetector.IClickDetectorListener clickListener) {
            mClickListener = clickListener;
            mClickDetector = new ClickDetector(mClickListener);
        }

        @Override
        public boolean onAreaTouched(final TouchEvent event, float touchAreaLocalX, float touchAreaLocalY) {
            mClickDetector.onManagedTouchEvent(event);
            return false;
        }
    }

    /**
     * Touch listener for {@link IEntity}
     * <br/>
     * Used to delegate {@link ITouchCallback#onAreaTouched(TouchEvent, float, float)} to
     * entity children. Touch will be handled by the child with the smallest tag.
     * <br/>
     * WARNING : Even if touch doesn't handled by any child the method
     * {@link ITouchCallback#onAreaTouched(TouchEvent, float, float)}
     * will return true which will prevent from further touch handling by onyone.
     */
    public static class EntityTouchToChildren implements ITouchCallback {
        /** TouchCallback object */
        private IEntity mObject;
        /** contains child who started to handle touch event */
        private IEntity mTouchedChild;

        public EntityTouchToChildren(IEntity entity) {
            mObject = entity;
        }

        @Override
        public boolean onAreaTouched(TouchEvent sceneTouchEvent, float touchAreaLocalX, float touchAreaLocalY) {
            if (!mObject.isVisible()) {
                return false;
            }
            float sceneX = sceneTouchEvent.getX();
            float sceneY = sceneTouchEvent.getY();
            if (mTouchedChild == null) {
                if (sceneTouchEvent.isActionDown()) {
                    for (int i = 0; i < mObject.getChildCount(); i++) {
                        IEntity child = mObject.getChildByIndex(i);
                        if (child.contains(sceneX, sceneY)) {
                            if (child.onAreaTouched(
                                    sceneTouchEvent, touchAreaLocalX, touchAreaLocalY)) {
                                mTouchedChild = child;
                                break;
                            }
                        }
                    }
                }
            } else {
                boolean result =
                        mTouchedChild.onAreaTouched(sceneTouchEvent, touchAreaLocalX, touchAreaLocalY);
                if (sceneTouchEvent.isActionUp() || sceneTouchEvent.isActionCancel()) {
                    mTouchedChild = null;
                }
                return result;
            }
            return true;
        }
    }
}

