package com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.listeners;

import com.gmail.yaroslavlancelot.eafall.game.eventbus.DetachEntityEvent;
import com.gmail.yaroslavlancelot.eafall.game.entity.RectangleWithBody;
import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.unit.Unit;
import com.gmail.yaroslavlancelot.eafall.game.team.ITeam;

import de.greenrobot.event.EventBus;

/** Callback after unit killing. Used method for MainOperationsBaseGameActivity class and should be placed in current class */
public class DestroyListener implements IDestroyListener {
    /** team of listening object */
    protected ITeam mTeam;

    /**
     * used for perform operation after unit death
     *
     * @param team {@link com.gmail.yaroslavlancelot.eafall.game.team.ITeam} of listening object
     */
    public DestroyListener(ITeam team) {
        mTeam = team;
    }

    @Override
    public void objectDestroyed(final RectangleWithBody gameObject) {
        if (gameObject instanceof Unit) {
            mTeam.removeObjectFromTeam((Unit) gameObject);
        }
        EventBus.getDefault().post(new DetachEntityEvent(gameObject));
    }
}
