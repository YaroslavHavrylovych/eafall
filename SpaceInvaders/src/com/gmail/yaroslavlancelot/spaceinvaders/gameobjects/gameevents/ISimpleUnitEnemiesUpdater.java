package com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.gameevents;

import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.SimpleUnit;

import java.util.List;

public interface ISimpleUnitEnemiesUpdater {
    public List<SimpleUnit> getEnemies(SimpleUnit simpleUnit);
}
