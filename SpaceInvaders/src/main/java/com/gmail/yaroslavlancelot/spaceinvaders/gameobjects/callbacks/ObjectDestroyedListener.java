package com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.callbacks;

import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.objects.dynamicobjects.Unit;
import com.gmail.yaroslavlancelot.spaceinvaders.teams.ITeam;
import com.gmail.yaroslavlancelot.spaceinvaders.game.interfaces.EntityOperations;
import org.andengine.entity.shape.IAreaShape;

/** Callback after unit killing. Used method for GameActivity class and should be placed in current class */
public class ObjectDestroyedListener implements IObjectDestroyedListener {
    /** team of listening object */
    private ITeam mTeam;
    /** for detaching sprites */
    private EntityOperations mEntityOperations;

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
    public void unitDestroyed(final IAreaShape sprite) {
        if (sprite instanceof Unit)
            mTeam.removeObjectFromTeam((Unit) sprite);
        mEntityOperations.detachEntity(sprite);
    }
}
