package com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.callbacks;

import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.objects.IGameObject;
import com.gmail.yaroslavlancelot.spaceinvaders.teams.ITeam;
import com.gmail.yaroslavlancelot.spaceinvaders.utils.interfaces.EntityOperations;

/**
 * triggers when planet destroyed
 */
public class PlanetDestroyedListener extends ObjectDestroyedListener {
    public PlanetDestroyedListener(final ITeam team, final EntityOperations entityOperations) {
        super(team, entityOperations);
    }

    @Override
    public void objectDestroyed(final IGameObject gameObject) {
        mTeam.removeTeamPlanet();
        super.objectDestroyed(gameObject);
    }
}
