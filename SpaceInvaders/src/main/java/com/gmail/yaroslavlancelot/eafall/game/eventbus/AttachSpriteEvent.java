package com.gmail.yaroslavlancelot.eafall.game.eventbus;


import com.gmail.yaroslavlancelot.eafall.game.entity.BatchedSprite;

/** hold entity for attach */
public class AttachSpriteEvent extends AbstractSpriteEvent {
    public AttachSpriteEvent(BatchedSprite batchedSprite) {
        super(batchedSprite);
    }
}
