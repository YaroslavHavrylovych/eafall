package com.gmail.yaroslavlancelot.eafall.game.entity.bullets;

import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.modifier.IModifier;

/**
 * @author Yaroslav Havrylovych
 */
public class SingleBullet extends AbstractBullet {
    // ===========================================================
    // Constants
    // ===========================================================

    // ===========================================================
    // Fields
    // ===========================================================

    // ===========================================================
    // Constructors
    // ===========================================================
    public SingleBullet(int width, int height, ITextureRegion textureRegion,
                        VertexBufferObjectManager vertexBufferObjectManager) {
        super(width, height, textureRegion, vertexBufferObjectManager);
    }


    // ===========================================================
    // Getter & Setter
    // ===========================================================

    // ===========================================================
    // Methods for/from SuperClass/Interfaces
    // ===========================================================
    @Override
    protected float duration(final float distance) {
        if (distance < 100) {
            return 0.15f;
        } else if (distance < 200) {
            return 0.3f;
        } else if (distance < 300) {
            return 0.45f;
        } else if (distance < 400) {
            return 0.6f;
        } else if (distance < 500) {
            return 0.75f;
        } else if (distance < 600) {
            return 0.9f;
        }
        return 1.1f;
    }

    @Override
    public void onModifierFinished(final IModifier pModifier, final Object pItem) {
        if (mTarget.getObjectUniqueId() == mTargetId && mTarget.isObjectAlive()) {
            mTarget.damageObject(mDamage);
        }
        destroy();
    }

    // ===========================================================
    // Methods
    // ===========================================================

    // ===========================================================
    // Inner and Anonymous Classes
    // ===========================================================
}
