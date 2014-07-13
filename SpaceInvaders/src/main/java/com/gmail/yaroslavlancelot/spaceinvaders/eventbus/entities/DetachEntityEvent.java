package com.gmail.yaroslavlancelot.spaceinvaders.eventbus.entities;

import org.andengine.entity.shape.IAreaShape;

/** hold entity for detach */
public class DetachEntityEvent extends AbstractEntityEvent {
    /** if entity needs to be detached with physic body */
    private boolean mWithBody = true;

    public DetachEntityEvent(IAreaShape entity, boolean hud) { super(entity, hud); }

    public DetachEntityEvent(IAreaShape entity) { super(entity); }

    /** set to false if bode needs to be detached without body and true in other way (default value : true) */
    public void setWithBody(boolean withBody) {
        mWithBody = withBody;
    }

    /** returns true if body needs to be detached with body */
    public boolean withBody() {
        return mWithBody;
    }
}
