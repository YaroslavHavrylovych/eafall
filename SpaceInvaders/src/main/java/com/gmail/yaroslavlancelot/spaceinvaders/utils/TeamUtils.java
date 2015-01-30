package com.gmail.yaroslavlancelot.spaceinvaders.utils;

import com.gmail.yaroslavlancelot.spaceinvaders.objects.objects.GameObject;
import com.gmail.yaroslavlancelot.spaceinvaders.objects.objects.units.Unit;
import com.gmail.yaroslavlancelot.spaceinvaders.teams.ITeam;

import java.util.ArrayList;
import java.util.List;

/** General operations with {@link com.gmail.yaroslavlancelot.spaceinvaders.teams.ITeam} interface */
public class TeamUtils {
    private TeamUtils() {
    }

    /** will invoke {@code TeamUtils#getEnemiesInRangeForUnit} with passed range as visible unit area */
    public static List<GameObject> getVisibleEnemiesForUnit(final Unit unit, ITeam enemyTeam) {
        return getEnemiesInRangeForUnit(unit, unit.getViewRadius(), enemyTeam);
    }

    /**
     * Create and return list of enemies for given unit which are in particular range about him
     *
     * @param unit      enemies of this will be returned
     * @param enemyTeam give unit enemy team
     * @return list of all unit enemies in the given area
     */
    public static List<GameObject> getEnemiesInRangeForUnit(final Unit unit, int range, ITeam enemyTeam) {
        List<GameObject> enemies = enemyTeam.getTeamObjects();
        List<GameObject> enemiesInView = new ArrayList<GameObject>(5);
        for (GameObject enemy : enemies) {
            if (UnitPathUtil.getDistanceBetweenPoints(enemy.getX(), enemy.getY(),
                    unit.getX(), unit.getY()) < range)
                enemiesInView.add(enemy);
        }
        return enemiesInView;
    }
}
