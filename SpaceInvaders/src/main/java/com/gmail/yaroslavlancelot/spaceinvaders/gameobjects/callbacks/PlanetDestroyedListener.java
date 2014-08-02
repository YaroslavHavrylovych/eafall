package com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.callbacks;

import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.objects.RectangleWithBody;
import com.gmail.yaroslavlancelot.spaceinvaders.teams.ITeam;

/**
 * triggers when planet destroyed
 */
public class PlanetDestroyedListener extends ObjectDestroyedListener {
    public PlanetDestroyedListener(final ITeam team) {
        super(team);
    }

    @Override
    public void objectDestroyed(final RectangleWithBody gameObject) {
        mTeam.removeTeamPlanet();
        super.objectDestroyed(gameObject);
    }
}
