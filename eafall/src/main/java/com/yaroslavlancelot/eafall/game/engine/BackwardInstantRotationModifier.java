package com.yaroslavlancelot.eafall.game.engine;

import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.RotationModifier;

/**
 * Custom RotationModifier implementation which {@link RotationModifier#reset()} the
 * animation as soon as it completed.
 * <br/>
 * Always rotate from 360 till 0
 * <br/>
 * repeatable
 */
public class BackwardInstantRotationModifier extends RotationModifier {
    public BackwardInstantRotationModifier(float pDuration) {
        super(pDuration, 360, 0);
    }

    @Override
    protected void onModifierFinished(IEntity pItem) {
        super.onModifierFinished(pItem);
        reset();
    }
}
