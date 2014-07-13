package com.gmail.yaroslavlancelot.spaceinvaders.eventbus.entities;

import org.andengine.entity.shape.IAreaShape;

/** hold entity for perform operations */
public abstract class AbstractEntityEvent {
    protected IAreaShape mEntity;
    /** contains true if operations should perform on hud */
    private boolean mHud;

    /**
     * @param entity entity for operations
     * @param hud    holds true if entity attached to hud
     */
    public AbstractEntityEvent(IAreaShape entity, boolean hud) {
        mEntity = entity;
        mHud = hud;
    }

    /**
     * call {@code AbstractEntityEvent(entity, false)}
     *
     * @param entity entity for operations
     */
    public AbstractEntityEvent(IAreaShape entity) {
        this(entity, false);
    }

    public IAreaShape getEntity() {
        return mEntity;
    }

    /** returns true if operations should perform on hud */
    public boolean hud() {
        return mHud;
    }
}
