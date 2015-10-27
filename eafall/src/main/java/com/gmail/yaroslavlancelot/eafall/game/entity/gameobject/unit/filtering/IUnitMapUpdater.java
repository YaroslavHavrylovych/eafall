package com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.unit.filtering;

import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.unit.Unit;

import java.util.List;

/**
 * Used to separate consumer and holder unit map operations. Updater is used
 * by the unit map holder to update units positions.
 *
 * @author Yaroslav Havrylovych
 */
public interface IUnitMapUpdater {
    void updatePositions(List<Unit> units);
}
