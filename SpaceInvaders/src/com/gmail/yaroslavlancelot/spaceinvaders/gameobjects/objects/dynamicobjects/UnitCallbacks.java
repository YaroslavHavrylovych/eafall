package com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.objects.dynamicobjects;

import com.gmail.yaroslavlancelot.spaceinvaders.teams.ITeam;
import com.gmail.yaroslavlancelot.spaceinvaders.utils.TeamUtils;

import java.util.List;

public class UnitCallbacks {
    private UnitCallbacks() {
    }

    public static ISimpleUnitEnemiesUpdater getSimpleUnitEnemiesUpdater(ITeam team) {
        return new SimpleUnitEnemiesUpdater(team);
    }

    private static class SimpleUnitEnemiesUpdater implements ISimpleUnitEnemiesUpdater {
        private ITeam mEnemyTeam;

        private SimpleUnitEnemiesUpdater(ITeam enemyTeam) {
            mEnemyTeam = enemyTeam;
        }

        @Override
        public List<Unit> getEnemies(final Unit unit) {
            return TeamUtils.getEnemiesForUnit(unit, mEnemyTeam);
        }
    }
}