package com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.staticobject;

import com.gmail.yaroslavlancelot.eafall.game.entity.BodiedSprite;
import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.listeners.DestroyListener;
import com.gmail.yaroslavlancelot.eafall.game.player.IPlayer;

/**
 * triggers when planet destroyed
 */
public class PlanetDestroyListener extends DestroyListener {
    public PlanetDestroyListener(final IPlayer player) {
        super(player);
    }

    @Override
    public void objectDestroyed(final BodiedSprite gameObject) {
        mPlayer.removePlanet();
        super.objectDestroyed(gameObject);
    }
}
