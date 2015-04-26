package com.gmail.yaroslavlancelot.eafall.game.client.thick.single;

import com.gmail.yaroslavlancelot.eafall.game.client.thick.ThickClientGameActivity;
import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.building.BuildingId;
import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.staticobject.PlanetStaticObject;
import com.gmail.yaroslavlancelot.eafall.game.team.ITeam;

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
        PlanetStaticObject planetStaticObject = userTeam.getPlanet();
        if (planetStaticObject != null)
            userTeam.getPlanet().createBuilding(buildingId);
    }
}
