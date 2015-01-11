package com.gmail.yaroslavlancelot.spaceinvaders.objects.callbacks;

import com.gmail.yaroslavlancelot.spaceinvaders.eventbus.entities.DetachEntityEvent;
import com.gmail.yaroslavlancelot.spaceinvaders.objects.objects.RectangleWithBody;
import com.gmail.yaroslavlancelot.spaceinvaders.objects.objects.units.dynamic.MovableUnit;
import com.gmail.yaroslavlancelot.spaceinvaders.teams.ITeam;

import de.greenrobot.event.EventBus;

/** Callback after unit killing. Used method for MainOperationsBaseGameActivity class and should be placed in current class */
public class ObjectDestroyedListener implements IObjectDestroyedListener {
    /** team of listening object */
    protected ITeam mTeam;

    /**
     * used for perform operation after unit death
     *
     * @param team {@link com.gmail.yaroslavlancelot.spaceinvaders.teams.ITeam} of listening object
     */
    public ObjectDestroyedListener(ITeam team) {
        mTeam = team;
    }

    @Override
    public void objectDestroyed(final RectangleWithBody gameObject) {
        if (gameObject instanceof MovableUnit) {
            mTeam.removeObjectFromTeam((MovableUnit) gameObject);
        }
        EventBus.getDefault().post(new DetachEntityEvent(gameObject));
    }
}
