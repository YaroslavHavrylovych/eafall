package com.yaroslavlancelot.eafall.game.entity.gameobject.unit.offence.path;

/** Used with move-by-click units (manual control) */
public class MoveBetweenPointsPath implements IUnitPath {
    private Point[] mPoints;
    private int mCurrentPoint;

    public MoveBetweenPointsPath(Point... points) {
        mPoints = points;
    }

    @Override
    public void getNextPathPoint(float[] currentPoint, float[] nextPoint) {
        float distanceToNextPoint = PathHelper.getDistanceBetweenPoints(
                currentPoint[0], currentPoint[1],
                mPoints[mCurrentPoint].mX, mPoints[mCurrentPoint].mY);
        if (distanceToNextPoint < PathHelper.MIN_DISTANCE_TO_SWITCH_TO_ANOTHER_POINT) {
            mCurrentPoint++;
            if (mCurrentPoint >= mPoints.length) {
                mCurrentPoint = 0;
            }
        }
        nextPoint[0] = mPoints[mCurrentPoint].mX;
        nextPoint[1] = mPoints[mCurrentPoint].mY;
    }

    @Override
    public int getTotalPathPoints() {
        return mPoints.length;
    }

    @Override
    public void setCurrentPathPoint(int n) {
        mCurrentPoint = 0;
    }

    @Override
    public int getCurrentPathPoint() {
        return mCurrentPoint;
    }

    public static class Point {
        private int mX;
        private int mY;

        public Point(int x, int y) {
            mX = x;
            mY = y;
        }

        public Point(int[] coords) {
            this(coords[0], coords[1]);
        }
    }
}
