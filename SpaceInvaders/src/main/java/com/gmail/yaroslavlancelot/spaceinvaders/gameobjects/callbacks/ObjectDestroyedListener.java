package com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.callbacks;

import com.gmail.yaroslavlancelot.spaceinvaders.eventbus.entities.DetachEntityEvent;
import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.objects.RectangleWithBody;
import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.objects.dynamicobjects.Unit;
import com.gmail.yaroslavlancelot.spaceinvaders.teams.ITeam;
import com.gmail.yaroslavlancelot.spaceinvaders.utils.interfaces.EntityOperations;

import de.greenrobot.event.EventBus;

/** Callback after unit killing. Used method for MainOperationsBaseGameActivity class and should be placed in current class */
public class ObjectDestroyedListener implements IObjectDestroyedListener {
    /** team of listening object */
    protected ITeam mTeam;
    /** for detaching sprites */
    protected EntityOperations mEntityOperations;

    /**
     * used for perform operation after unit death
     *
     * @param team {@link com.gmail.yaroslavlancelot.spaceinvaders.teams.ITeam} of listening object
     */
    public ObjectDestroyedListener(ITeam team, EntityOperations entityOperations) {
        mTeam = team;
        mEntityOperations = entityOperations;
    }

    @Override
    public void objectDestroyed(final RectangleWithBody gameObject) {
        if (gameObject instanceof Unit) {
            mTeam.removeObjectFromTeam((Unit) gameObject);
        }
        EventBus.getDefault().post(new DetachEntityEvent(gameObject));
    }
}
