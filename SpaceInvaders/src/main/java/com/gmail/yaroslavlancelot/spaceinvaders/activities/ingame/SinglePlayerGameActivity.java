package com.gmail.yaroslavlancelot.spaceinvaders.activities.ingame;

import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.objects.buildings.BuildingId;
import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.objects.staticobjects.PlanetStaticObject;
import com.gmail.yaroslavlancelot.spaceinvaders.teams.ITeam;

/**
 * Single player game
 */
public class SinglePlayerGameActivity extends ThickClientGameActivity {

    @Override
    public void afterGameLoaded() {
        replaceSplashSceneWithGameScene();
    }

    @Override
    protected void userWantCreateBuilding(final ITeam userTeam, BuildingId buildingId) {
        PlanetStaticObject planetStaticObject = userTeam.getTeamPlanet();
        if (planetStaticObject != null)
            userTeam.getTeamPlanet().createBuilding(buildingId);
    }
}
