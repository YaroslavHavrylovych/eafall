package com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.unit;

import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.GameObject;
import com.gmail.yaroslavlancelot.eafall.game.team.ITeam;
import com.gmail.yaroslavlancelot.eafall.game.team.StaticHelper;

import java.util.ArrayList;
import java.util.List;

/** filter enemies by some criteria */
public class EnemiesFilter implements IEnemiesFilter {
    private ITeam mEnemyTeam;

    private EnemiesFilter() {
    }

    private EnemiesFilter(ITeam enemyTeam) {
        mEnemyTeam = enemyTeam;
    }

    public static IEnemiesFilter getSimpleUnitEnemiesUpdater(ITeam team) {
        return new EnemiesFilter(team);
    }

    @Override
    public List<GameObject> getVisibleEnemiesForUnit(final Unit unit) {
        return StaticHelper.getVisibleEnemiesForUnit(unit, mEnemyTeam);
    }

    @Override
    public List<GameObject> getEnemiesInRangeForUnit(Unit unit, int range) {
        return StaticHelper.getEnemiesInRangeForUnit(unit, range, mEnemyTeam);
    }

    @Override
    public List<GameObject> getEnemiesObjects() {
        List<GameObject> list = new ArrayList<GameObject>(mEnemyTeam.getTeamObjects());
        return list;
    }

    @Override
    public GameObject getMainTarget() {
        return mEnemyTeam.getPlanet();
    }
}