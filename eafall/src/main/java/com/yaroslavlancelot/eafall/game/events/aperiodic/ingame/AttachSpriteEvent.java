package com.yaroslavlancelot.eafall.game.events.aperiodic.ingame;


import com.yaroslavlancelot.eafall.game.entity.BatchedSprite;

/** hold entity for attach */
public class AttachSpriteEvent extends AbstractSpriteEvent {
    public AttachSpriteEvent(BatchedSprite batchedSprite) {
        super(batchedSprite);
    }
}
