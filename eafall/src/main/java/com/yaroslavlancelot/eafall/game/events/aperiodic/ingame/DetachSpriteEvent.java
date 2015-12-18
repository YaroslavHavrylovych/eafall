package com.yaroslavlancelot.eafall.game.events.aperiodic.ingame;

import com.yaroslavlancelot.eafall.game.entity.BatchedSprite;
import com.yaroslavlancelot.eafall.game.entity.BodiedSprite;

/** hold entity for detach */
public class DetachSpriteEvent extends AbstractSpriteEvent {
    private boolean mBodied;

    public DetachSpriteEvent(BodiedSprite batchedSprite) {
        super(batchedSprite);
        mBodied = true;
    }

    public DetachSpriteEvent(BatchedSprite batchedSprite) {
        super(batchedSprite);
    }

    public boolean isBodied() {
        return mBodied;
    }
}
