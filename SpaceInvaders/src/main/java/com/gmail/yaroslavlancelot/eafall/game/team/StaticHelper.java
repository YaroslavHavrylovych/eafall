package com.gmail.yaroslavlancelot.eafall.game.team;

import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.GameObject;
import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.unit.Unit;

import java.util.ArrayList;
import java.util.List;

/** General operations with {@link com.gmail.yaroslavlancelot.eafall.game.team.ITeam} interface */
public class StaticHelper {
    private StaticHelper() {
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
            if (com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.unit.dynamic.path.StaticHelper.getDistanceBetweenPoints(enemy.getX(), enemy.getY(),
                    unit.getX(), unit.getY()) < range)
                enemiesInView.add(enemy);
        }
        return enemiesInView;
    }
}
