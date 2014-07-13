package com.gmail.yaroslavlancelot.spaceinvaders.eventbus.entities;


import org.andengine.entity.shape.IAreaShape;

/** hold entity for attach */
public class AttachEntityEvent extends AbstractEntityEvent {
    public AttachEntityEvent(IAreaShape entity, boolean hud) { super(entity, hud); }

    public AttachEntityEvent(IAreaShape entity) { super(entity); }
}
