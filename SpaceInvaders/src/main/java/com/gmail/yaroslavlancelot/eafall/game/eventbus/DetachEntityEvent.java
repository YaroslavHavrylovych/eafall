package com.gmail.yaroslavlancelot.eafall.game.eventbus;

import org.andengine.entity.shape.IAreaShape;

/** hold entity for detach */
public class DetachEntityEvent extends AbstractEntityEvent {
    /** if entity needs to be detached with physic body */
    private boolean mWithBody = true;
    /** unregister touch in all child elements */
    private boolean mUnregisterChildrenTouch;

    public DetachEntityEvent(IAreaShape entity, boolean hud, boolean unregisterChildrenTouch) {
        super(entity, hud);
        mUnregisterChildrenTouch = unregisterChildrenTouch;
    }

    public DetachEntityEvent(IAreaShape entity, boolean hud) {
        super(entity, hud);
    }

    public DetachEntityEvent(IAreaShape entity) {
        super(entity);
    }

    /** set to false if bode needs to be detached without body and true in other way (default value : true) */
    public void setWithBody(boolean withBody) {
        mWithBody = withBody;
    }

    /** returns true if body needs to be detached with body */
    public boolean withBody() {
        return mWithBody;
    }

    /** returns true if child elements touch should be unregistered  */
    public boolean isUnregisterChildrenTouch() {
        return mUnregisterChildrenTouch;
    }
}
