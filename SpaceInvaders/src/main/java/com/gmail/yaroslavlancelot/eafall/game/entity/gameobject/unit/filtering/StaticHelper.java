package com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.unit.filtering;

import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.GameObject;
import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.unit.Unit;
import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.unit.dynamic.path.PathHelper;
import com.gmail.yaroslavlancelot.eafall.game.player.IPlayer;

import java.util.ArrayList;
import java.util.List;

/** General operations with {@link IPlayer} interface */
public class StaticHelper {
    private StaticHelper() {
    }

    /**
     * will invoke {@code PlayerUtils#getEnemiesInRangeForUnit} with passed range as visible unit area
     * <br/>
     * WARNING heavy operation
     */
    static List<GameObject> getVisibleEnemiesForUnit(final Unit unit, IPlayer enemyPlayer) {
        return getEnemiesInRangeForUnit(unit, unit.getViewRadius(), enemyPlayer);
    }

    /**
     * Create and return list of enemies for given unit which are in particular range about him
     * <br/>
     * WARNING heavy operation
     *
     * @param unit        enemies of this will be returned
     * @param enemyPlayer give unit enemy player
     * @return list of all unit enemies in the given area
     */
    static List<GameObject> getEnemiesInRangeForUnit(final Unit unit, int range, IPlayer enemyPlayer) {
        List<GameObject> enemies = enemyPlayer.getPlayerObjects();
        //TODO prevent new array creation. Use (or pass) existing as if using this method in
        //update cycle can cause arrays overhead
        List<GameObject> enemiesInView = new ArrayList<GameObject>(5);
        for (GameObject enemy : enemies) {
            if (PathHelper.getDistanceBetweenPoints(enemy.getX(), enemy.getY(),
                    unit.getX(), unit.getY()) < range)
                enemiesInView.add(enemy);
        }
        return enemiesInView;
    }

    /**
     * Return first founded enemy unit in range
     *
     * @param unit        enemies of this will be returned
     * @param enemyPlayer give unit enemy player
     * @return enemy unit in range or null if there is no such unit
     */
    static GameObject getEnemy(final Unit unit, int range, IPlayer enemyPlayer) {
        for (GameObject enemy : enemyPlayer.getPlayerObjects()) {
            if (checkRange(range, enemy.getX(), enemy.getY(),
                    unit.getX(), unit.getY())) {
                return enemy;
            }
        }
        return null;
    }

    /**
     * Return closest founded enemy unit in range
     *
     * @param unit        enemies of this will be returned
     * @param enemyPlayer give unit enemy player
     * @return enemy unit in range or null if there is no such unit
     */
    static GameObject getClosestEnemy(final Unit unit, int range, IPlayer enemyPlayer) {
        GameObject closestEnemy = null;
        float smallestDistance = 0f;
        for (GameObject enemy : enemyPlayer.getPlayerObjects()) {
            float distance =
                    PathHelper.getDistanceBetweenPoints(
                            enemy.getX(), enemy.getY(), unit.getX(), unit.getY());
            if (distance < smallestDistance) {
                smallestDistance = distance;
                closestEnemy = enemy;
            }
        }
        if (smallestDistance > range) {
            closestEnemy = null;
        }
        return closestEnemy;
    }


    /**
     * checks that distance between points less than given range
     *
     * @param range maximum distance between points
     * @param x1    first point abscissa
     * @param y1    first point ordinate
     * @param x2    second point abscissa
     * @param y2    second point ordinate
     * @return true if distance less than range and false in other way
     */
    static boolean checkRange(float range, float x1, float y1, float x2, float y2) {
        return PathHelper.getDistanceBetweenPoints(x1, y1, x2, y2) < range;
    }


    /**
     * check range as most simplified (of course not so accurate) way
     * <br/>
     * simplification is just removing of sqrt (as long operation) with checking x and y in separate
     *
     * @param range check that x1,y1 is in range with x2,y2
     * @param x1    first point abscissa
     * @param y1    first point ordinate
     * @param x2    second point abscissa
     * @param y2    second point ordinate
     * @return true if both (x1,x2) and (y1,y2) is in range and false if it's not
     */
    static boolean checkSimplifiedRange(float range, float x1, float y1, float x2, float y2) {
        return Math.abs(x1 - x2) < range && Math.abs(y1 - y2) < range;
    }
}
