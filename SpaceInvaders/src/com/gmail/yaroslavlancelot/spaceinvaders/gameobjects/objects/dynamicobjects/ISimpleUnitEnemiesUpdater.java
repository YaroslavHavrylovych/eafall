package com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.objects.dynamicobjects;

import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.objects.dynamicobjects.Unit;

import java.util.List;

public interface ISimpleUnitEnemiesUpdater {
    public List<Unit> getEnemies(Unit unit);
}
