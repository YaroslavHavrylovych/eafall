package com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.unit.filtering;

import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.GameObject;
import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.unit.Unit;
import com.gmail.yaroslavlancelot.eafall.game.player.IPlayer;

import java.util.ArrayList;
import java.util.List;

/**
 * Simple enemies filter. A lot of hard operations so can cause problems.
 *
 * @author Yaroslav Havrylovych
 */
public class EnemiesFilter implements IEnemiesFilter {
    // ===========================================================
    // Constants
    // ===========================================================

    // ===========================================================
    // Fields
    // ===========================================================
    private IPlayer mEnemyPlayer;

    // ===========================================================
    // Constructors
    // ===========================================================
    EnemiesFilter(IPlayer enemyPlayer) {
        mEnemyPlayer = enemyPlayer;
    }

    // ===========================================================
    // Getter & Setter
    // ===========================================================

    // ===========================================================
    // Methods for/from SuperClass/Interfaces
    // ===========================================================

    @Override
    public List<GameObject> getEnemiesObjects() {
        return new ArrayList<GameObject>(mEnemyPlayer.getPlayerObjects());
    }

    @Override
    public GameObject getMainTarget() {
        return mEnemyPlayer.getPlanet();
    }

    @Override
    public List<GameObject> getVisibleEnemiesForUnit(final Unit unit) {
        return StaticHelper.getVisibleEnemiesForUnit(unit, mEnemyPlayer);
    }

    @Override
    public List<GameObject> getEnemiesInRangeForUnit(Unit unit, int range) {
        return StaticHelper.getEnemiesInRangeForUnit(unit, range, mEnemyPlayer);
    }

    @Override
    public GameObject getFirstEnemyInRange(final Unit unit, final int range) {
        return StaticHelper.getEnemy(unit, range, mEnemyPlayer);
    }

    // ===========================================================
    // Methods
    // ===========================================================

    // ===========================================================
    // Inner and Anonymous Classes
    // ===========================================================
}
