package com.gmail.yaroslavlancelot.eafall.game.entity.bullets;

import com.gmail.yaroslavlancelot.eafall.game.engine.InstantRotationModifier;

import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.modifier.IModifier;

/**
 * @author Yaroslav Havrylovych
 */
public class QuakerBullet extends AbstractBullet {
    // ===========================================================
    // Constants
    // ===========================================================

    // ===========================================================
    // Fields
    // ===========================================================

    // ===========================================================
    // Constructors
    // ===========================================================
    public QuakerBullet(int width, int height, ITextureRegion textureRegion,
                        VertexBufferObjectManager vertexBufferObjectManager) {
        super(width, height, textureRegion, vertexBufferObjectManager);
        registerEntityModifier(new InstantRotationModifier(2));
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
            return 0.4f;
        } else if (distance < 200) {
            return 0.8f;
        } else if (distance < 300) {
            return 1.2f;
        } else if (distance < 400) {
            return 1.6f;
        } else if (distance < 500) {
            return 2f;
        } else if (distance < 600) {
            return 2.4f;
        }
        return 3f;
    }

    @Override
    public void onModifierFinished(final IModifier pModifier, final Object pItem) {
        if (mTarget.getObjectUniqueId() == mTargetId && mTarget.isObjectAlive()) {
            mTarget.damageObject(mDamage);
        }
        setPosition(-100, -100);
        onBulletDestroyed();
    }

    // ===========================================================
    // Methods
    // ===========================================================

    // ===========================================================
    // Inner and Anonymous Classes
    // ===========================================================
}
