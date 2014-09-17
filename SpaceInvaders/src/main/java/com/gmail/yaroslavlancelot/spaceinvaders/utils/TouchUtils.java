package com.gmail.yaroslavlancelot.spaceinvaders.utils;

import org.andengine.entity.shape.Area;
import org.andengine.entity.shape.ITouchCallback;

import org.andengine.input.touch.TouchEvent;

/** Helping functions to work with touch events */
public final class TouchUtils {
    private TouchUtils() {
    }

    /** Check is value is in range and return new value base in this information */
    public static float stickToBorderOrLeftValue(float value, float minValue, float maxValue) {
        if (value > maxValue)
            return maxValue;
        else if (value < minValue)
            return minValue;
        return value;
    }

    /**
     * TouchListener which contains stub for some common methods. Like press or click happens, or
     * un-press happens (user move finger out of element in pressed state)  on touchable element.
     */
    public static class CustomTouchListener implements ITouchCallback {
        /** contains is that's click event or not (just press down and up (up - out of border) */
        private boolean mIsItClickEvent;
        /** contains true if between press event and up event action move(out of bounds)/outside or cancel event happens */
        private boolean mIsOutOfBoundMoveHappens;
        /** used to find out is you move in bounds or not */
        private Area mArea;
        private boolean mActionDownPresent;


        /** parameters description you can see in javadoc to fields of this class */
        public CustomTouchListener(Area area) {
            mArea = area;
        }

        @Override
        public boolean onAreaTouched(final TouchEvent event, float touchAreaLocalX, float touchAreaLocalY) {
            switch (event.getAction()) {
                case TouchEvent.ACTION_DOWN: {
                    mIsItClickEvent = true;
                    mIsOutOfBoundMoveHappens = false;
                    mActionDownPresent = true;
                    press();
                    return true;
                }
                case TouchEvent.ACTION_UP: {
                    if (!mActionDownPresent) {
                        return false;
                    }
                    mActionDownPresent = false;
                    if (mIsItClickEvent) {
                        click();
                    }
                    return true;
                }
                case TouchEvent.ACTION_MOVE:
                case TouchEvent.ACTION_OUTSIDE:
                case TouchEvent.ACTION_CANCEL: {
                    if (!mActionDownPresent) {
                        return false;
                    }
                    if (mIsOutOfBoundMoveHappens) {
                        return false;
                    }
                    if (isTouchOutOfBound(event)) {
                        mIsItClickEvent = false;
                        mIsOutOfBoundMoveHappens = true;
                        unPress();
                        return false;
                    }
                    return true;
                }
            }
            return false;
        }

        /** element was pressed callback */
        public void press() {
        }

        /** callback after click on element happens. User touch down and up finger on element without cancelling or move outside */
        public void click() {
        }

        private boolean isTouchOutOfBound(final TouchEvent event) {
            return !mArea.contains(event.getX(), event.getY());
        }

        /** callback after user press element but then cancel press with moving outside of the element borders */
        public void unPress() {
        }
    }
}

