package com.gmail.yaroslavlancelot.eafall.game.visual.buttons;

import org.andengine.entity.sprite.ButtonSprite;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

/**
 * Used to extend arrow buttons clickable areas.
 *
 * @author Yaroslav Havrylovych
 */
public class ArrowButtonSprite extends ButtonSprite {
    // ===========================================================
    // Constants
    // ===========================================================

    // ===========================================================
    // Fields
    // ===========================================================

    // ===========================================================
    // Constructors
    // ===========================================================
    public ArrowButtonSprite(final float pX, final float pY, final ITiledTextureRegion pTiledTextureRegion, final VertexBufferObjectManager pVertexBufferObjectManager) {
        super(pX, pY, pTiledTextureRegion, pVertexBufferObjectManager);
    }

    // ===========================================================
    // Getter & Setter
    // ===========================================================

    // ===========================================================
    // Methods for/from SuperClass/Interfaces
    // ===========================================================
    @Override
    public boolean contains(final float pX, final float pY) {
        float[] coordinates = convertLocalCoordinatesToSceneCoordinates(mWidth / 2, mHeight / 2);
        return this.isVisible() &&
                pX > coordinates[0] - mWidth - mWidth && pX < coordinates[0] + mWidth + mWidth &&
                pY > coordinates[1] - mHeight && pY < coordinates[1] + mHeight;
    }

    // ===========================================================
    // Methods
    // ===========================================================

    // ===========================================================
    // Inner and Anonymous Classes
    // ===========================================================
}
