package com.gmail.yaroslavlancelot.eafall.game.client.thick.single;

import com.gmail.yaroslavlancelot.eafall.game.client.thick.ThickClientGameActivity;
import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.building.BuildingId;
import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.staticobject.planet.PlanetStaticObject;
import com.gmail.yaroslavlancelot.eafall.game.player.IPlayer;

/**
 * Single player game
 */
public class SinglePlayerGameActivity extends ThickClientGameActivity {

    @Override
    public void onResourcesLoaded() {
        super.onResourcesLoaded();
        hideSplash();
    }

    @Override
    protected void userWantCreateBuilding(final IPlayer userPlayer, BuildingId buildingId) {
        PlanetStaticObject planetStaticObject = userPlayer.getPlanet();
        if (planetStaticObject != null)
            userPlayer.getPlanet().createBuilding(buildingId);
    }
}
