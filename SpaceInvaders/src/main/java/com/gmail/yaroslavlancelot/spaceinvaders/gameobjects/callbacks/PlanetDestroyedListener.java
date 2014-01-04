package com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.callbacks;

import com.gmail.yaroslavlancelot.spaceinvaders.utils.interfaces.EntityOperations;
import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.objects.GameObject;
import com.gmail.yaroslavlancelot.spaceinvaders.teams.ITeam;

public class PlanetDestroyedListener extends ObjectDestroyedListener {
    public PlanetDestroyedListener(final ITeam team, final EntityOperations entityOperations) {
        super(team, entityOperations);
    }

    @Override
    public void objectDestroyed(final GameObject gameObject) {
        mTeam.removeTeamPlanet();
        super.objectDestroyed(gameObject);
    }
}
