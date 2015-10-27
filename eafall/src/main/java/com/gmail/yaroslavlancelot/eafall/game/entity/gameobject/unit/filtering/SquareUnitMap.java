package com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.unit.filtering;

import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.unit.Unit;

import java.util.ArrayList;
import java.util.List;

/**
 * Units map (later just grid) which split screen area on partitions (size of each 120*120).
 * Each grid partition hold units list which are in this screen partition.
 * It helps to exclude from search the positions which are too far for the unit
 * (Unit can't see more than closest 2 without counting the current unit partition).
 *
 * @author Yaroslav Havrylovych
 */
public class SquareUnitMap implements IUnitMap, IUnitMapUpdater {
    // ===========================================================
    // Constants
    // ===========================================================
    private final int N = 16;
    private final int M = 9;
    private final int PARTITION_SIZE = 120;
    private final int PARTIOTIONAL_VIEW = 2;

    // ===========================================================
    // Fields
    // ===========================================================
    private List<List<List<Unit>>> mPartitions;

    // ===========================================================
    // Constructors
    // ===========================================================
    public SquareUnitMap() {
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
        int m = iy / PARTITION_SIZE;
        Unit unit;
        //
        unit = getClosest(ix, iy, iRange, mPartitions.get(n).get(m));

        return unit;
    }

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
            n = (int) unit.getX() / PARTITION_SIZE;
            m = (int) unit.getY() / PARTITION_SIZE;
            mPartitions.get(n).get(m).add(unit);
        }
    }

    private Unit getClosest(int unitX, int unitY, int range, List<Unit> units) {
        int x, y, distX, distY;
        Unit unit, closestUnit = null;
        int distance = 1920;
        for (int i = 0; i < units.size(); i++) {
            unit = units.get(i);
            x = (int) unit.getX();
            y = (int) unit.getY();
            distX = Math.abs(unitX - x);
            distY = Math.abs(unitY - y);
            if (distX > range || distY > range) {
                continue;
            }
            x = distX >> 2 + distY >> 2; //x used as distance
            if (x < distance) {
                distance = x;
                closestUnit = unit;
            }
        }
        return closestUnit;
    }

    // ===========================================================
    // Methods
    // ===========================================================

    // ===========================================================
    // Inner and Anonymous Classes
    // ===========================================================
}
