package com.gmail.yaroslavlancelot.eafall.game.player;

import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.GameObject;
import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.unit.Unit;

import java.util.ArrayList;
import java.util.List;

/** General operations with {@link IPlayer} interface */
public class StaticHelper {
    private StaticHelper() {
    }

    /** will invoke {@code PlayerUtils#getEnemiesInRangeForUnit} with passed range as visible unit area */
    public static List<GameObject> getVisibleEnemiesForUnit(final Unit unit, IPlayer enemyPlayer) {
        return getEnemiesInRangeForUnit(unit, unit.getViewRadius(), enemyPlayer);
    }

    /**
     * Create and return list of enemies for given unit which are in particular range about him
     *
     * @param unit      enemies of this will be returned
     * @param enemyPlayer give unit enemy player
     * @return list of all unit enemies in the given area
     */
    public static List<GameObject> getEnemiesInRangeForUnit(final Unit unit, int range, IPlayer enemyPlayer) {
        List<GameObject> enemies = enemyPlayer.getPlayerObjects();
        List<GameObject> enemiesInView = new ArrayList<GameObject>(5);
        for (GameObject enemy : enemies) {
            if (com.gmail.yaroslavlancelot.eafall.game
                    .entity.gameobject.unit.dynamic.path
                    .StaticHelper.getDistanceBetweenPoints(enemy.getX(), enemy.getY(),
                            unit.getX(), unit.getY()) < range)
                enemiesInView.add(enemy);
        }
        return enemiesInView;
    }
}
