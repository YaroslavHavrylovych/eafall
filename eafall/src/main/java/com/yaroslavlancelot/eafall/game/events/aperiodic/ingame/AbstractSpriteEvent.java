package com.yaroslavlancelot.eafall.game.events.aperiodic.ingame;

import com.yaroslavlancelot.eafall.game.entity.BatchedSprite;

/** hold entity for perform operations */
public abstract class AbstractSpriteEvent {
    protected BatchedSprite mBatchedSprite;

    /**
     * call {@code AbstractEntityEvent(entity, false)}
     *
     * @param batchedSprite entity for operations
     */
    public AbstractSpriteEvent(BatchedSprite batchedSprite) {
        mBatchedSprite = batchedSprite;
    }

    public BatchedSprite getSprite() {
        return mBatchedSprite;
    }
}
