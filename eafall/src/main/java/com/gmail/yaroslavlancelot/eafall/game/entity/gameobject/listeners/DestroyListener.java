package com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.listeners;

import com.gmail.yaroslavlancelot.eafall.game.entity.BodiedSprite;
import com.gmail.yaroslavlancelot.eafall.game.events.aperiodic.ingame.DetachSpriteEvent;
import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.unit.Unit;
import com.gmail.yaroslavlancelot.eafall.game.player.IPlayer;

import de.greenrobot.event.EventBus;

/** Callback after unit killing. Used method for ClientGameActivity class and should be placed in current class */
public class DestroyListener implements IDestroyListener {
    /** player of listening object */
    protected IPlayer mPlayer;

    /**
     * used for perform operation after unit death
     *
     * @param player {@link IPlayer} of listening object
     */
    public DestroyListener(IPlayer player) {
        mPlayer = player;
    }

    @Override
    public void objectDestroyed(final BodiedSprite gameObject) {
        if (gameObject instanceof Unit) {
            mPlayer.removeObjectFromPlayer((Unit) gameObject);
        }
        EventBus.getDefault().post(new DetachSpriteEvent(gameObject));
    }
}
