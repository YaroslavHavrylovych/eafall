package com.gmail.yaroslavlancelot.spaceinvaders.utils;

import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.Unit;
import com.gmail.yaroslavlancelot.spaceinvaders.teams.ITeam;

import java.util.ArrayList;
import java.util.List;

public class TeamUtils {
    public static List<Unit> getEnemiesForUnit(final Unit unit, ITeam enemyTeam) {
        List<Unit> enemies = enemyTeam.getTeamUnits();
        List<Unit> enemiesInView = new ArrayList<Unit>(5);
        for (Unit enemy : enemies) {
            if (UnitPathUtil.getDistanceBetweenPoints(enemy.getX(), enemy.getY(),
                    unit.getX(), unit.getY()) < unit.getViewRadius())
                enemiesInView.add(enemy);
        }
        return enemiesInView;
    }
}
