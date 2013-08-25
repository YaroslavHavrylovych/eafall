package com.gmail.yaroslavlancelot.spaceinvaders.utils;

import com.gmail.yaroslavlancelot.spaceinvaders.GameActivity;

public class UnitPathUtil {
    public static final float MIN_DISTANCE_TO_SWITCH_TO_ANOTHER_POINT = 60;
    private static final float[] xArray1 = new float[]{32, 200, 400, 600, GameActivity.sCameraWidth - 32};
    private static final float[] xArray2 = new float[]{GameActivity.sCameraWidth - 32, 600, 400, 200, 32};
    private static final float[] yArray = new float[]{GameActivity.sCameraHeight / 2 - 32, 100, 40, 100, GameActivity.sCameraHeight / 2 - 32};

    private UnitPathUtil() {
    }

    public static UnitPath getUnitPathAccordingToStartAbscissa(float startX) {
        float min = startX - xArray1[0];
        if (Math.abs(min) < Math.abs(startX - xArray2[0])) return new UnitPath(xArray1, yArray);
        else return new UnitPath(xArray2, yArray);
    }

    public static float getDistanceBetweenPoints(float x1, float y1, float x2, float y2) {
        return (float) Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2));
    }

    public static class UnitPath {
        private float[] mX, mY;
        private int mCurrentPointIndex;

        private UnitPath(float[] x, float[] y) {
            mX = x;
            mY = y;
            mCurrentPointIndex = 1;
        }

        public void getNextPathPoint(float[] currentPoint, float[] nextPoint) {
            getNextPathPoint(currentPoint, nextPoint, mX, mY);
        }

        private void getNextPathPoint(float[] currentPoint, float[] nextPoint, final float[] xArray, final float[] yArray) {
            float x = currentPoint[0], y = currentPoint[1];
            // if we reach last point
            if (mCurrentPointIndex + 1 == xArray.length) {
                nextPoint[0] = xArray[xArray.length - 1];
                nextPoint[1] = yArray[yArray.length - 1];
                return;
            }
            // if it's not
            float distanceToNextPoint = getDistanceBetweenPoints(xArray[mCurrentPointIndex], yArray[mCurrentPointIndex], x, y);
            if (distanceToNextPoint < MIN_DISTANCE_TO_SWITCH_TO_ANOTHER_POINT)
                mCurrentPointIndex++;
            nextPoint[0] = xArray[mCurrentPointIndex];
            nextPoint[1] = yArray[mCurrentPointIndex];
        }
    }
}