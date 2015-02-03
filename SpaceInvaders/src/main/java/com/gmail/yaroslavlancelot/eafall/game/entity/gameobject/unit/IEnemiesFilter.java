package com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.unit;

import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.GameObject;

import java.util.List;

/** callback for update unit visible enemies */
public interface IEnemiesFilter {
    public List<GameObject> getVisibleEnemiesForUnit(Unit unit);

    public List<GameObject> getEnemiesInRangeForUnit(Unit unit, int range);

    public List<GameObject> getEnemiesObjects();

    public GameObject getMainTarget();
}
