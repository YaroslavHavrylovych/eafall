package com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.objects.dynamicobjects;

import java.util.List;

/** callback for update unit visible enemies */
public interface ISimpleUnitEnemiesUpdater {
    public List<Unit> getEnemiesForUnit(Unit unit);
    public List<Unit> getEnemies();
}
