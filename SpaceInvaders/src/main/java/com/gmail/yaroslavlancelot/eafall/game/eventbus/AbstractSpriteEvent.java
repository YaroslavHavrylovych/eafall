package com.gmail.yaroslavlancelot.eafall.game.eventbus;

import com.gmail.yaroslavlancelot.eafall.game.entity.BatchedSprite;

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
