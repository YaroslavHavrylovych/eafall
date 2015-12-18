package com.yaroslavlancelot.eafall.game.engine;

import org.andengine.entity.modifier.RotationModifier;
import org.andengine.util.modifier.ease.IEaseFunction;

/**
 * @author Yaroslav Havrylovych
 */
public class ManualFinishRotationModifier extends RotationModifier {
    // ===========================================================
    // Constants
    // ===========================================================

    // ===========================================================
    // Fields
    // ===========================================================

    // ===========================================================
    // Constructors
    // ===========================================================
    public ManualFinishRotationModifier(final float pDuration, final float pFromRotation, final float pToRotation) {
        super(pDuration, pFromRotation, pToRotation);
    }

    public ManualFinishRotationModifier(final float pDuration, final float pFromRotation, final float pToRotation, final IEaseFunction pEaseFunction) {
        super(pDuration, pFromRotation, pToRotation, pEaseFunction);
    }

    public ManualFinishRotationModifier(final float pDuration, final float pFromRotation, final float pToRotation, final IEntityModifierListener pEntityModifierListener) {
        super(pDuration, pFromRotation, pToRotation, pEntityModifierListener);
    }

    public ManualFinishRotationModifier(final float pDuration, final float pFromRotation, final float pToRotation, final IEntityModifierListener pEntityModifierListener, final IEaseFunction pEaseFunction) {
        super(pDuration, pFromRotation, pToRotation, pEntityModifierListener, pEaseFunction);
    }

    protected ManualFinishRotationModifier(final RotationModifier pRotationModifier) {
        super(pRotationModifier);
    }

    // ===========================================================
    // Getter & Setter
    // ===========================================================
    public void setFinished(boolean finished) {
        mFinished = finished;
    }

    // ===========================================================
    // Methods for/from SuperClass/Interfaces
    // ===========================================================

    // ===========================================================
    // Methods
    // ===========================================================

    // ===========================================================
    // Inner and Anonymous Classes
    // ===========================================================
}
