package com.gmail.yaroslavlancelot.eafall.game.touch;

import org.andengine.entity.IEntity;
import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.shape.ITouchCallback;
import org.andengine.input.touch.TouchEvent;

/** Helping functions to work with touch events */
public final class StaticHelper {
    private StaticHelper() {
    }

    /** Ranging the value and stick to max/min if needed */
    public static float stick(float value, float minValue, float maxValue) {
        if (value > maxValue)
            return maxValue;
        else if (value < minValue)
            return minValue;
        return value;
    }

    /** touch listener that always return true */
    public static enum EmptyTouch implements ITouchCallback {
        INSTANCE;

        public static EmptyTouch getInstance() {
            return INSTANCE;
        }

        @Override
        public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float touchAreaLocalX, float touchAreaLocalY) {
            return true;
        }
    }

    public static interface OnClickListener {
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

