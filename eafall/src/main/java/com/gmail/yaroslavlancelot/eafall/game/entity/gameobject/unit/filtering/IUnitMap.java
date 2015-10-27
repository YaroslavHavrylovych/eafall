package com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.unit.filtering;

import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.unit.Unit;

/**
 * Units positions map interface. It's implementation gives you the access to particular unit/units
 * based on position and range.
 *
 * @author Yaroslav Havrylovych
 */
public interface IUnitMap {
    Unit getClosestUnit(float x, float y, float range);
}
