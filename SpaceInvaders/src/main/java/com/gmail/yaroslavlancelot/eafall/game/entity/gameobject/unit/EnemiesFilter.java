package com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.unit;

import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.GameObject;
import com.gmail.yaroslavlancelot.eafall.game.player.IPlayer;
import com.gmail.yaroslavlancelot.eafall.game.player.StaticHelper;

import java.util.ArrayList;
import java.util.List;

/** filter enemies by some criteria */
public class EnemiesFilter implements IEnemiesFilter {
    private IPlayer mEnemyPlayer;

    private EnemiesFilter() {
    }

    private EnemiesFilter(IPlayer enemyPlayer) {
        mEnemyPlayer = enemyPlayer;
    }

    public static IEnemiesFilter getSimpleUnitEnemiesUpdater(IPlayer player) {
        return new EnemiesFilter(player);
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
    public List<GameObject> getEnemiesObjects() {
        List<GameObject> list = new ArrayList<GameObject>(mEnemyPlayer.getPlayerObjects());
        return list;
    }

    @Override
    public GameObject getMainTarget() {
        return mEnemyPlayer.getPlanet();
    }
}