package com.gmail.yaroslavlancelot.eafall.game.events.aperiodic.ingame;


import com.gmail.yaroslavlancelot.eafall.game.entity.BatchedSprite;

/** hold entity for attach */
public class AttachSpriteEvent extends AbstractSpriteEvent {
    public AttachSpriteEvent(BatchedSprite batchedSprite) {
        super(batchedSprite);
    }
}
