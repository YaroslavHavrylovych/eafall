package com.gmail.yaroslavlancelot.spaceinvaders.objects.objects.dynamicobjects;

import com.gmail.yaroslavlancelot.spaceinvaders.objects.objects.GameObject;
import com.gmail.yaroslavlancelot.spaceinvaders.objects.objects.units.Unit;

import java.util.List;

/** callback for update unit visible enemies */
public interface ISimpleUnitEnemiesUpdater {
    public List<GameObject> getVisibleEnemiesForUnit(Unit unit);

    public List<GameObject> getEnemiesInRangeForUnit(Unit unit, int range);

    public List<GameObject> getEnemiesObjects();

    public GameObject getMainTarget();
}
