package com.gmail.yaroslavlancelot.spaceinvaders.utils;

import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.objects.GameObject;
import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.objects.dynamicobjects.Unit;
import com.gmail.yaroslavlancelot.spaceinvaders.teams.ITeam;

import java.util.ArrayList;
import java.util.List;

/** General operations with {@link com.gmail.yaroslavlancelot.spaceinvaders.teams.ITeam} interface */
public class TeamUtils {
    private TeamUtils() {
    }

    /**
     * Create and return list of enemies from enemy team in visible area for unit
     *
     * @param unit      unit enemies of whom should to be returned in the list
     * @param enemyTeam unfriendly team for unit
     * @return list of all unit enemies in the visible rect (from enemy team which pass like parameter)
     */
    public static List<GameObject> getVisibleEnemiesForUnit(final Unit unit, ITeam enemyTeam) {
        List<GameObject> enemies = enemyTeam.getTeamObjects();
        List<GameObject> enemiesInView = new ArrayList<GameObject>(5);
        for (GameObject enemy : enemies) {
            if (UnitPathUtil.getDistanceBetweenPoints(enemy.getX(), enemy.getY(),
                    unit.getX(), unit.getY()) < unit.getViewRadius())
                enemiesInView.add(enemy);
        }
        return enemiesInView;
    }
}
