package com.yaroslavlancelot.eafall.game.entity.gameobject.explosion;

import com.yaroslavlancelot.eafall.game.batching.BatchingKeys;
import com.yaroslavlancelot.eafall.game.batching.SpriteGroupHolder;
import com.yaroslavlancelot.eafall.game.entity.gameobject.GameObject;

import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

/**
 * Unit explosion animation.
 *
 * @author Yaroslav Havrylovych
 */
public class UnitExplosionAnimation extends AnimatedSprite {
    // ===========================================================
    // Constants
    // ===========================================================

    // ===========================================================
    // Fields
    // ===========================================================
    /** actions while animation */
    private UnitExplosionAnimationListener mExplosionAnimationListener;

    // ===========================================================
    // Constructors
    // ===========================================================
    public UnitExplosionAnimation(ITiledTextureRegion textureRegion, VertexBufferObjectManager vboManager) {
        super(-100, -100, textureRegion, vboManager);
        mExplosionAnimationListener = new UnitExplosionAnimationListener(this);
        setIgnoreUpdate(true);
        setVisible(false);
        SpriteGroupHolder.getGroup(BatchingKeys.EXPLOSIONS).attachChild(this);
    }

    // ===========================================================
    // Getter & Setter
    // ===========================================================

    // ===========================================================
    // Methods for/from SuperClass/Interfaces
    // ===========================================================

    // ===========================================================
    // Methods
    // ===========================================================
    public void init(GameObject gameObject) {
        mExplosionAnimationListener.init(gameObject);
        setIgnoreUpdate(false);
        setVisible(true);
        animate(50, false, mExplosionAnimationListener);
    }

    // ===========================================================
    // Inner and Anonymous Classes
    // ===========================================================
}
