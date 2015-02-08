package com.gmail.yaroslavlancelot.eafall.game.eventbus;


import org.andengine.entity.shape.IAreaShape;

/** hold entity for attach */
public class AttachEntityEvent extends AbstractEntityEvent {
    /** unregister touch in all child elements */
    private boolean mRegisterChildrenTouch;

    public AttachEntityEvent(IAreaShape entity, boolean hud, boolean registerChildrenTouch) {
        super(entity, hud);
        mRegisterChildrenTouch = registerChildrenTouch;
    }

    public AttachEntityEvent(IAreaShape entity, boolean hud) {
        super(entity, hud);
    }

    public AttachEntityEvent(IAreaShape entity) {
        super(entity);
    }


    /** returns true if child elements touch should be unregistered */
    public boolean isRegisterChildrenTouch() {
        return mRegisterChildrenTouch;
    }
}
