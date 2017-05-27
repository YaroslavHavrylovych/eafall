package com.yaroslavlancelot.eafall.game.entity.gameobject;

import com.yaroslavlancelot.eafall.game.batching.BatchingKeys;
import com.yaroslavlancelot.eafall.game.batching.SpriteGroupHolder;
import com.yaroslavlancelot.eafall.game.engine.InstantRotationModifier;
import com.yaroslavlancelot.eafall.game.entity.gameobject.explosion.UnitExplosionAnimationListener;

import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

/**
 * EndPoint which used to mark where to move in FindPath missions.
 *
 * @author Yaroslav Havrylovych
 */
public class EndPointAnimatedSprite extends AnimatedSprite {
    // ===========================================================
    // Constants
    // ===========================================================

    // ===========================================================
    // Fields
    // ===========================================================

    // ===========================================================
    // Constructors
    // ===========================================================
    public EndPointAnimatedSprite(int x, int y,
                                  ITiledTextureRegion textureRegion,
                                  VertexBufferObjectManager vboManager) {
        super(x, y, textureRegion, vboManager);
        registerEntityModifier(new InstantRotationModifier(30));
        animate(75, true);
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

    // ===========================================================
    // Inner and Anonymous Classes
    // ===========================================================
}
