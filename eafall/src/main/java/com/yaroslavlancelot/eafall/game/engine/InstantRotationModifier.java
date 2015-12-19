package com.yaroslavlancelot.eafall.game.engine;

import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.RotationModifier;

/**
 * Custom RotationModifier implementation which {@link RotationModifier#reset()} the
 * animation as soon as it completed.
 * <br/>
 * Always rotate from 0 till 360
 * <br/>
 * repeatable
 */
public class InstantRotationModifier extends RotationModifier {
    public InstantRotationModifier(float pDuration) {
        super(pDuration, 0, 360);
    }

    @Override
    protected void onModifierFinished(IEntity pItem) {
        super.onModifierFinished(pItem);
        reset();
    }
}
