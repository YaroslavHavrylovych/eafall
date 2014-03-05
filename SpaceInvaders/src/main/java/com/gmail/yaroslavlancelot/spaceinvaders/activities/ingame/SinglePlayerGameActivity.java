package com.gmail.yaroslavlancelot.spaceinvaders.activities.ingame;

import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.objects.staticobjects.PlanetStaticObject;
import com.gmail.yaroslavlancelot.spaceinvaders.teams.ITeam;

/**
 * Single player game
 */
public class SinglePlayerGameActivity extends PhysicWorldGameActivity {
    @Override
    protected void userWantCreateBuilding(final ITeam userTeam, final int buildingId) {
        PlanetStaticObject planetStaticObject = userTeam.getTeamPlanet();
        if (planetStaticObject != null)
            userTeam.getTeamPlanet().createBuildingById(buildingId);
    }
}
