package com.gmail.yaroslavlancelot.eafall.game.eventbus;

import org.andengine.entity.shape.Shape;

/** hold entity for perform operations */
public abstract class AbstractEntityEvent {
    protected Shape mEntity;
    /** contains true if operations should perform on hud */
    private boolean mHud;

    /**
     * @param entity entity for operations
     * @param hud    holds true if entity attached to hud
     */
    public AbstractEntityEvent(Shape entity, boolean hud) {
        mEntity = entity;
        mHud = hud;
    }

    /**
     * call {@code AbstractEntityEvent(entity, false)}
     *
     * @param entity entity for operations
     */
    public AbstractEntityEvent(Shape entity) {
        this(entity, false);
    }

    public Shape getEntity() {
        return mEntity;
    }

    /** returns true if operations should perform on hud */
    public boolean hud() {
        return mHud;
    }
}
