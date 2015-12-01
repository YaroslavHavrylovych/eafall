package com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.unit.filtering;

import com.gmail.yaroslavlancelot.eafall.game.constant.SizeConstants;
import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.unit.Unit;
import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.unit.offence.path.PathHelper;

import org.andengine.util.math.MathUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Units map (later just grid) which split screen area on partitions (size of each 120*120) 16*9.
 * Each grid cell hold`s units list which are in this screen partition.
 * It helps to exclude from search the positions which are too far for the unit
 * (Unit can't see more than closest 2 without counting the current unit partition).
 * <br/>
 * Search between neighbor cells looks like a spiral which are always heads to the
 * units movement vector (right or left) and second cell is always opposite to the
 * planet direction.
 *
 * @author Yaroslav Havrylovych
 */
public class SquareUnitMap implements IUnitMap, IUnitMapUpdater {
    // ===========================================================
    // Constants
    // ===========================================================
    public final int N = 16;
    public final int M = 9;
    public final int N_1 = N - 1;
    public final int M_1 = M - 1;
    public final int PARTITION_SIZE = SizeConstants.FIELD_PARTITION_SIZE;
    public final int HALF_PARTITION_SIZE = PARTITION_SIZE / 2;
    private final boolean mLtr;

    // ===========================================================
    // Fields
    // ===========================================================
    private List<List<List<Unit>>> mPartitions;
    /** used as temporary list to store units */
    private List<Unit> mTmpUnitList = new ArrayList<>(40);

    // ===========================================================
    // Constructors
    // ===========================================================
    public SquareUnitMap(boolean ltr) {
        mLtr = ltr;
        mPartitions = new ArrayList<>(N);
        for (int i = 0; i < N; i++) {
            mPartitions.add(new ArrayList<List<Unit>>(M));
            for (int j = 0; j < M; j++) {
                mPartitions.get(i).add(new ArrayList<Unit>(10));
            }
        }
    }


    // ===========================================================
    // Getter & Setter
    // ===========================================================

    // ===========================================================
    // Methods for/from SuperClass/Interfaces
    // ===========================================================
    @Override
    public Unit getClosestUnit(float x, float y, float range) {
        int ix = (int) x, iy = (int) y, iRange = (int) range;
        int n = ix / PARTITION_SIZE;
        int m = (SizeConstants.GAME_FIELD_HEIGHT - iy) / PARTITION_SIZE;
        boolean top = y > SizeConstants.HALF_FIELD_HEIGHT;

        Unit unit = selectClosest(ix, iy, iRange, mPartitions.get(n).get(m));
        if (unit != null) {
            return unit;
        }
        int rangeInPartitions = Math.round(range / PARTITION_SIZE + 1) - 1;
        // different spiral for units from left and right side
        // ALSO difference depends on top or bottom of the screen
        // (as second spiral cell has to opposite to the planet)
        if (mLtr) {
            if (top) {
                return ltrTopSearch(n, m, rangeInPartitions, ix, iy, iRange);
            }
            return ltrBottomSearch(n, m, rangeInPartitions, ix, iy, iRange);
        }
        if (top) {
            return rtlTopSearch(n, m, rangeInPartitions, ix, iy, iRange);
        }
        return rtlBottomSearch(n, m, rangeInPartitions, ix, iy, iRange);
    }

    @Override
    public List<Unit> getInRange(final float x, final float y, final float range) {
        mTmpUnitList.clear();
        int ix = (int) x, iy = (int) y, iRange = (int) range;
        int n = ix / PARTITION_SIZE;
        int m = (SizeConstants.GAME_FIELD_HEIGHT - iy) / PARTITION_SIZE;
        addFromPartition(ix, iy, iRange, n, m);
        //check neighbour partitions
        int restN = ix - n * PARTITION_SIZE;
        int restM = iy - (N - m) * PARTITION_SIZE;
        if (restN < range) {
            addFromPartition(ix, iy, iRange, n - 1, m);
            if (restM < range) {
                addFromPartition(ix, iy, iRange, n - 1, m - 1);
            } else if (restM > range) {
                addFromPartition(ix, iy, iRange, n - 1, m + 1);
            }
        } else if (restN > range) {
            addFromPartition(ix, iy, iRange, n + 1, m);
            if (restM < range) {
                addFromPartition(ix, iy, iRange, n + 1, m - 1);
            } else if (restM > range) {
                addFromPartition(ix, iy, iRange, n + 1, m + 1);
            }
        } else {
            if (restM < range) {
                addFromPartition(ix, iy, iRange, n, m - 1);
            } else if (restM > range) {
                addFromPartition(ix, iy, iRange, n, m + 1);
            }
        }
        return mTmpUnitList;
    }

    @Override
    public List<Unit> getUnitOnSide(boolean left) {
        mTmpUnitList.clear();
        for (int i = 0; i < N / 2; i++) {
            for (int j = 0; j < M; j++) {
                mTmpUnitList.addAll(mPartitions.get(i).get(j));
            }
        }
        return mTmpUnitList;
    }

    // ===========================================================
    // Methods
    // ===========================================================

    @Override
    public void updatePositions(List<Unit> units) {
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < M; j++) {
                mPartitions.get(i).get(j).clear();
            }
        }
        int n, m;
        Unit unit;
        for (int i = 0; i < units.size(); i++) {
            unit = units.get(i);
            int x = (int) unit.getX() - 1; //exclude max value
            int y = (int) unit.getY() - 1; //exclude max value
            if (x < 0 || y < 0) {
                continue;
            }
            n = x / PARTITION_SIZE;
            m = (SizeConstants.GAME_FIELD_HEIGHT - y) / PARTITION_SIZE;
            mPartitions.get(n).get(m).add(unit);
        }
    }

    /**
     * add all units from the given partition to the {@link SquareUnitMap#mTmpUnitList}
     * if they're in the given range
     */
    private void addFromPartition(int x, int y, int range, int n, int m) {
        if (n < 0 || n > N_1 || m < 0 || m > N_1) {
            return;
        }
        List<Unit> partition = mPartitions.get(n).get(m);
        for (int i = 0; i < partition.size(); i++) {
            Unit unit = partition.get(i);
            if (PathHelper.getDistanceBetweenPoints(unit.getX(), unit.getY(), x, y) < range) {
                mTmpUnitList.add(unit);
            }
        }
    }

    /**
     * used to search an enemy for the unit from the top side of the screen
     * which move from right to left
     */
    private Unit rtlTopSearch(int n, int m, int rangeInPartitions, int x, int y, int range) {
        Unit unit;
        for (int i = 1; i <= rangeInPartitions; i++) {
            unit = unitSpiralSearchStep(n - i, m, m - i, -1, false, x, y, range);
            if (unit != null) {
                return unit;
            }
            unit = unitSpiralSearchStep(n - i, m - i, n + i, 1, true, x, y, range);
            if (unit != null) {
                return unit;
            }
            unit = unitSpiralSearchStep(n + i, m - i, m + i, 1, false, x, y, range);
            if (unit != null) {
                return unit;
            }
            unit = unitSpiralSearchStep(n + i, m + i, n - i, -1, true, x, y, range);
            if (unit != null) {
                return unit;
            }
        }
        return null;
    }

    /**
     * used to search an enemy for the unit from the bottom side of the screen
     * which move from right to left
     */
    private Unit rtlBottomSearch(int n, int m, int rangeInPartitions, int x, int y, int range) {
        Unit unit;
        for (int i = 1; i <= rangeInPartitions; i++) {
            unit = unitSpiralSearchStep(n - i, m, m + i, 1, false, x, y, range);
            if (unit != null) {
                return unit;
            }
            unit = unitSpiralSearchStep(n - i, m + i, n + i, 1, true, x, y, range);
            if (unit != null) {
                return unit;
            }
            unit = unitSpiralSearchStep(n + i, m + i, m - i, -1, false, x, y, range);
            if (unit != null) {
                return unit;
            }
            unit = unitSpiralSearchStep(n + i, m - i, n - i, -1, true, x, y, range);
            if (unit != null) {
                return unit;
            }
        }
        return null;
    }

    /**
     * used to search an enemy for the unit from the top side of the screen
     * which move from left to right
     */
    private Unit ltrTopSearch(int n, int m, int rangeInPartitions, int x, int y, int range) {
        Unit unit;
        for (int i = 1; i <= rangeInPartitions; i++) {
            unit = unitSpiralSearchStep(n + i, m, m - i, -1, false, x, y, range);
            if (unit != null) {
                return unit;
            }
            unit = unitSpiralSearchStep(n + i, m - i, n - i, -1, true, x, y, range);
            if (unit != null) {
                return unit;
            }
            unit = unitSpiralSearchStep(n - i, m - i, m + i, 1, false, x, y, range);
            if (unit != null) {
                return unit;
            }
            unit = unitSpiralSearchStep(n - i, m + i, n + i, 1, true, x, y, range);
            if (unit != null) {
                return unit;
            }
        }
        return null;
    }

    /**
     * used to search an enemy for the unit from the bottom side of the screen
     * which move from left to right
     */
    private Unit ltrBottomSearch(int n, int m, int rangeInPartitions, int x, int y, int range) {
        Unit unit;
        for (int i = 1; i <= rangeInPartitions; i++) {
            unit = unitSpiralSearchStep(n + i, m, m + i, 1, false, x, y, range);
            if (unit != null) {
                return unit;
            }
            unit = unitSpiralSearchStep(n + i, m + i, n - i, -1, true, x, y, range);
            if (unit != null) {
                return unit;
            }
            unit = unitSpiralSearchStep(n - i, m + i, m - i, -1, false, x, y, range);
            if (unit != null) {
                return unit;
            }
            unit = unitSpiralSearchStep(n - i, m - i, n + i, 1, true, x, y, range);
            if (unit != null) {
                return unit;
            }
        }
        return null;
    }

    /** search for the unit in one row (one out of the spiral`s wires) */
    private Unit unitSpiralSearchStep(int n, int m, int bound, int coefficient, boolean useN,
                                      int x, int y, int range) {
        for (int i = useN ? n : m; coefficient > 0 ? i < bound : i > bound; i += coefficient) {
            if (useN
                    ? !MathUtils.isInBounds(0, N_1, i) || !MathUtils.isInBounds(0, M_1, m)
                    : !MathUtils.isInBounds(0, M_1, i) || !MathUtils.isInBounds(0, N_1, n)) {
                continue;
            }
            Unit unit = selectClosest(x, y, range, mPartitions.get(useN ? i : n).get(!useN ? i : m));
            if (unit != null) {
                return unit;
            }
        }
        return null;
    }

    /** select closest to the given coordinates unit */
    private Unit selectClosest(int unitX, int unitY, int range, List<Unit> units) {
        int x, y, distX, distY;
        Unit unit, closestUnit = null;
        int distance = SizeConstants.GAME_FIELD_WIDTH;
        for (int i = 0; i < units.size(); i++) {
            unit = units.get(i);
            x = (int) unit.getX();
            y = (int) unit.getY();
            distX = Math.abs(unitX - x);
            distY = Math.abs(unitY - y);
            if (distX > range || distY > range) {
                continue;
            }
            x = distX >> 2 + distY >> 2; //x used to store the distance value
            if (x < distance) {
                distance = x;
                closestUnit = unit;
            }
        }
        return closestUnit;
    }

    // ===========================================================
    // Inner and Anonymous Classes
    // ===========================================================
}
