package com.gmail.yaroslavlancelot.spaceinvaders.eventbus.entities;


import org.andengine.entity.shape.IAreaShape;

/** hold entity for attach */
public class AttachEntityEvent extends AbstractEntityEvent {
    /** unregister touch in all child elements */
    private boolean mRegisterChildrensTouch;

    public AttachEntityEvent(IAreaShape entity, boolean hud, boolean registerChildrensTouch) {
        super(entity, hud);
        mRegisterChildrensTouch = registerChildrensTouch;
    }

    public AttachEntityEvent(IAreaShape entity, boolean hud) { super(entity, hud); }

    public AttachEntityEvent(IAreaShape entity) { super(entity); }



    /** returns true if child elements touch should be unregistered  */
    public boolean isRegisterChildrenTouch() {
        return mRegisterChildrensTouch;
    }
}
