package com.gmail.yaroslavlancelot.spaceinvaders.utils;

import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.objects.dynamicobjects.ISimpleUnitEnemiesUpdater;
import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.objects.dynamicobjects.Unit;
import com.gmail.yaroslavlancelot.spaceinvaders.teams.ITeam;

import java.util.List;

/** for simple creating of all needed for unit listeners */
public class UnitCallbacksUtils {
    private UnitCallbacksUtils() {
    }

    public static ISimpleUnitEnemiesUpdater getSimpleUnitEnemiesUpdater(ITeam team) {
        return new SimpleUnitEnemiesUpdater(team);
    }

    /** updates unit visible enemies list */
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