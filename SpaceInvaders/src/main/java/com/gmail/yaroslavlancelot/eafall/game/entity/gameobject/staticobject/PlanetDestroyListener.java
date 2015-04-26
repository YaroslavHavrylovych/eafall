package com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.staticobject;

import com.gmail.yaroslavlancelot.eafall.game.entity.BodiedSprite;
import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.listeners.DestroyListener;
import com.gmail.yaroslavlancelot.eafall.game.team.ITeam;

/**
 * triggers when planet destroyed
 */
public class PlanetDestroyListener extends DestroyListener {
    public PlanetDestroyListener(final ITeam team) {
        super(team);
    }

    @Override
    public void objectDestroyed(final BodiedSprite gameObject) {
        mTeam.removePlanet();
        super.objectDestroyed(gameObject);
    }
}
