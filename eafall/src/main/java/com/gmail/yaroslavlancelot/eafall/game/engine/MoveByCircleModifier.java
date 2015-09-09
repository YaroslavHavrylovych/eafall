package com.gmail.yaroslavlancelot.eafall.game.engine;

import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.RotationModifier;
import org.andengine.util.math.MathUtils;

/**
 * Constantly moves entity by circle with given radius and movement duration.
 *
 * @author Yaroslav Havrylovych
 */
public class MoveByCircleModifier extends RotationModifier {
    // ===========================================================
    // Constants
    // ===========================================================

    // ===========================================================
    // Fields
    // ===========================================================
    private float mX;
    private float mY;
    private float mRadius;

    // ===========================================================
    // Constructors
    // ===========================================================
    public MoveByCircleModifier(float duration, float radius, int x, int y) {
        super(duration, 0, 360);
        mX = x;
        mY = y;
        mRadius = radius;
        setAutoUnregisterWhenFinished(false);
    }

    // ===========================================================
    // Getter & Setter
    // ===========================================================

    // ===========================================================
    // Methods for/from SuperClass/Interfaces
    // ===========================================================
    @Override
    protected void onSetInitialValue(final IEntity entity, final float rotation) {
        entity.setPosition(mX, mY);
    }

    @Override
    protected void onSetValue(final IEntity entity, final float percentageDone, final float rotation) {
        float x = mRadius * (float) Math.cos(MathUtils.degToRad(rotation)) + mX;
        float y = mRadius * (float) Math.sin(MathUtils.degToRad(rotation)) + mY;
        entity.setPosition(x, y);
    }

    @Override
    protected void onModifierFinished(final IEntity pItem) {
        super.onModifierFinished(pItem);
        reset();
    }

    // ===========================================================
    // Methods
    // ===========================================================

    // ===========================================================
    // Inner and Anonymous Classes
    // ===========================================================
}
