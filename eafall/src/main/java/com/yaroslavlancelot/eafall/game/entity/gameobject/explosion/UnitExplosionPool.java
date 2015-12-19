package com.yaroslavlancelot.eafall.game.entity.gameobject.explosion;

import com.yaroslavlancelot.eafall.game.constant.StringConstants;
import com.yaroslavlancelot.eafall.game.entity.AfterInitializationPool;
import com.yaroslavlancelot.eafall.game.entity.TextureRegionHolder;

import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

/**
 * Pool for explosions (to prevent it from re-initiating each time).
 *
 * @author Yaroslav Havrylovych
 */
public class UnitExplosionPool extends AfterInitializationPool<UnitExplosionAnimation> {
    // ===========================================================
    // Constants
    // ===========================================================
    private volatile static UnitExplosionPool EXPLOSION_POOL;
    private VertexBufferObjectManager mObjectManager;

    // ===========================================================
    // Fields
    // ===========================================================

    // ===========================================================
    // Constructors
    // ===========================================================
    private UnitExplosionPool(final VertexBufferObjectManager objectManager) {
        mObjectManager = objectManager;
        initPool(5, 5);
    }


    // ===========================================================
    // Getter & Setter
    // ===========================================================
    public static UnitExplosionAnimation getExplosion() {
        return EXPLOSION_POOL.obtainPoolItem();
    }

    public static UnitExplosionPool getInstance() {
        return EXPLOSION_POOL;
    }

    // ===========================================================
    // Methods for/from SuperClass/Interfaces
    // ===========================================================
    @Override
    protected UnitExplosionAnimation allocatePoolItem() {
        return new UnitExplosionAnimation(
                (ITiledTextureRegion) TextureRegionHolder.getRegion(StringConstants.KEY_UNIT_EXPLOSION),
                mObjectManager);
    }

    // ===========================================================
    // Methods
    // ===========================================================
    public static void init(VertexBufferObjectManager objectManager) {
        EXPLOSION_POOL = new UnitExplosionPool(objectManager);
    }

    // ===========================================================
    // Inner and Anonymous Classes
    // ===========================================================
}
