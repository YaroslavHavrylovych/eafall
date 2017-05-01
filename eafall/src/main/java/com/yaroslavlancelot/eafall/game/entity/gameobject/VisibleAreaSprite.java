package com.yaroslavlancelot.eafall.game.entity.gameobject;

import com.yaroslavlancelot.eafall.game.engine.InstantRotationModifier;

import org.andengine.entity.IEntity;
import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

/**
 * Visible area of a unit
 *
 * @author Yaroslav Havrylovych
 */
public class VisibleAreaSprite extends Sprite {
    // ===========================================================
    // Constants
    // ===========================================================

    // ===========================================================
    // Fields
    // ===========================================================
    private IEntity mUnit;

    // ===========================================================
    // Constructors
    // ===========================================================
    public VisibleAreaSprite(int x, int y,
                             ITextureRegion textureRegion,
                             IEntity unit,
                             VertexBufferObjectManager vboManager) {
        super(x, y, textureRegion, vboManager);
        registerEntityModifier(new InstantRotationModifier(15));
        mUnit = unit;
    }

    // ===========================================================
    // Getter & Setter
    // ===========================================================

    // ===========================================================
    // Methods for/from SuperClass/Interfaces
    // ===========================================================

    @Override
    protected void onManagedUpdate(float pSecondsElapsed) {
        super.onManagedUpdate(pSecondsElapsed);
        setPosition(mUnit.getX(), mUnit.getY());
    }


    // ===========================================================
    // Methods
    // ===========================================================

    // ===========================================================
    // Inner and Anonymous Classes
    // ===========================================================
}
