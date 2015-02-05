package com.gmail.yaroslavlancelot.eafall.game.eventbus.bullet;


import com.gmail.yaroslavlancelot.eafall.game.eventbus.AbstractEntityEvent;

import org.andengine.entity.shape.IAreaShape;

/** hold entity for attach */
public class AttachBulletEvent extends AbstractEntityEvent {
    /** unregister touch in all child elements */
    private boolean mRegisterChildrenTouch;

    public AttachBulletEvent(IAreaShape entity, boolean hud, boolean registerChildrenTouch) {
        super(entity, hud);
        mRegisterChildrenTouch = registerChildrenTouch;
    }

    public AttachBulletEvent(IAreaShape entity, boolean hud) { super(entity, hud); }

    public AttachBulletEvent(IAreaShape entity) { super(entity); }



    /** returns true if child elements touch should be unregistered  */
    public boolean isRegisterChildrenTouch() {
        return mRegisterChildrenTouch;
    }
}
