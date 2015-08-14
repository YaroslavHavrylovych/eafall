package com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.unit.filtering;

import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.GameObject;
import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.unit.Unit;

import java.util.List;

/**
 * get enemies by given criteria
 *
 * @author Yaroslav Havrylovych
 */
public interface IEnemiesFilter {
    List<GameObject> getEnemiesObjects();

    GameObject getMainTarget();

    List<GameObject> getVisibleEnemiesForUnit(Unit unit);

    List<GameObject> getEnemiesInRangeForUnit(Unit unit, int range);

    /** return first founded enemy based on the unit range */
    GameObject getFirstEnemyInRange(Unit unit, int range);
}
