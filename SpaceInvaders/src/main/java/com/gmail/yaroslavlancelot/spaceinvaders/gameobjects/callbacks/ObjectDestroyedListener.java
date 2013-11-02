package com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.callbacks;

import com.gmail.yaroslavlancelot.spaceinvaders.game.interfaces.EntityOperations;
import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.objects.GameObject;
import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.objects.dynamicobjects.Unit;
import com.gmail.yaroslavlancelot.spaceinvaders.teams.ITeam;

/** Callback after unit killing. Used method for GameActivity class and should be placed in current class */
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
    public void objectDestroyed(final GameObject gameObject) {
        if (gameObject instanceof Unit) {
            mTeam.removeObjectFromTeam(gameObject);
        }
        mEntityOperations.detachPhysicsBody(gameObject);
        mEntityOperations.detachEntity(gameObject);
    }
}
