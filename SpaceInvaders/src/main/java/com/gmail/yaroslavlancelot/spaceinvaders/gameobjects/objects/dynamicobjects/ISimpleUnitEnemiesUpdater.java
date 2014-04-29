package com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.objects.dynamicobjects;

import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.objects.GameObject;

import java.util.List;

/** callback for update unit visible enemies */
public interface ISimpleUnitEnemiesUpdater {
    public List<GameObject> getVisibleEnemiesForUnit(Unit unit);

    public List<GameObject> getEnemiesObjects();

    public GameObject getMainTarget();
}
