package com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.unit.filtering;

import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.unit.Unit;

import java.util.List;

/**
 * Units positions map interface. It's implementation gives you the access to particular unit/units
 * based on position and range.
 *
 * @author Yaroslav Havrylovych
 */
public interface IUnitMap {
    /** return closest to the give coordinates unit */
    Unit getClosestUnit(float x, float y, float range);

    /** get all units in given range */
    List<Unit> getInRange(float x, float y, float range);
}
