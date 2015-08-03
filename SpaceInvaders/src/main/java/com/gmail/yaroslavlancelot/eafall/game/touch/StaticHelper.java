package com.gmail.yaroslavlancelot.eafall.game.touch;

import com.gmail.yaroslavlancelot.eafall.EaFallApplication;
import com.gmail.yaroslavlancelot.eafall.game.configuration.IConfig;

import org.andengine.engine.camera.Camera;
import org.andengine.entity.IEntity;
import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.shape.ITouchCallback;
import org.andengine.input.touch.TouchEvent;
import org.andengine.util.Constants;

/** Helping functions to work with touch events */
public final class StaticHelper {
    private StaticHelper() {
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

    /** touch listener that always return true */
    public enum EmptyTouch implements ITouchCallback {
        INSTANCE;

        public static EmptyTouch getInstance() {
            return INSTANCE;
        }

        @Override
        public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float touchAreaLocalX, float touchAreaLocalY) {
            return true;
        }
    }

    public interface OnClickListener {
        void onClick();
    }

    /**
     * TouchListener which contains stub for some common methods. Like press or click happens, or
     * un-press happens (user move finger out of element in pressed state)  on touchable element.
     */
    public static class CustomTouchListener implements ITouchCallback {
        /** used to find out is you move in bounds or not */
        protected IEntity mObject;
        /** true if current touch is simple click (just press down and up) */
        private boolean mIsItClickEvent;


        /** parameters description you can see in javadoc to fields of this class */
        public CustomTouchListener(IEntity object) {
            mObject = object;
        }

        /** element was pressed callback */
        public void press() {
            mIsItClickEvent = true;
        }

        /**
         * check that {@link StaticHelper.CustomTouchListener#mObject}
         * contains x and y coordinates
         *
         * @return true if object visible and x and y both more then zero less then object
         * width and height
         */
        private boolean contains(float x, float y) {
            if (!mObject.isVisible()) {
                return false;
            }
            return x > 0 && y > 0 && x < mObject.getWidth() && y < mObject.getHeight();
        }

        /** callback after user press element but then cancel press with moving outside of the element borders */
        public void unPress() {
            mIsItClickEvent = false;
        }

        /** callback after click on element happens. User touch down and up finger on element without cancelling or move outside */
        public void click() {
            unPress();
        }

        @Override
        public boolean onAreaTouched(final TouchEvent event, float touchAreaLocalX, float touchAreaLocalY) {
            if (event.isActionDown()) {
                press();
            } else if (event.isActionCancel() || !contains(touchAreaLocalX, touchAreaLocalY)) {
                unPress();
            } else if (event.isActionUp() && mIsItClickEvent) {
                click();
            }
            return true;
        }
    }

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
}

