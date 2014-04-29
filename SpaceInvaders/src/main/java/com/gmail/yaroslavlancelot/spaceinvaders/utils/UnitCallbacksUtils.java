package com.gmail.yaroslavlancelot.spaceinvaders.utils;

import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.objects.GameObject;
import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.objects.dynamicobjects.ISimpleUnitEnemiesUpdater;
import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.objects.dynamicobjects.Unit;
import com.gmail.yaroslavlancelot.spaceinvaders.teams.ITeam;

import java.util.ArrayList;
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
        public List<GameObject> getVisibleEnemiesForUnit(final Unit unit) {
            return TeamUtils.getVisibleEnemiesForUnit(unit, mEnemyTeam);
        }

        @Override
        public List<GameObject> getEnemiesObjects() {
            List<GameObject> list = new ArrayList<GameObject>(mEnemyTeam.getTeamObjects());
            return list;
        }

        @Override
        public GameObject getMainTarget() {
            return mEnemyTeam.getTeamPlanet();
        }
    }
}