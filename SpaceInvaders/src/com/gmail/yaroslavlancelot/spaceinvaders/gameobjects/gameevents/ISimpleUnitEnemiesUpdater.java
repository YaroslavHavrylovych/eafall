package com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.gameevents;

import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.units.Unit;

import java.util.List;

public interface ISimpleUnitEnemiesUpdater {
    public List<Unit> getEnemies(Unit unit);
}
