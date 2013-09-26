package com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.objects.dynamicobjects;

import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

public class HandsAttacker extends Unit {
    public HandsAttacker(final float x, final float y, final ITextureRegion textureRegion,
                         final VertexBufferObjectManager vertexBufferObjectManager) {
        super(x, y, textureRegion, vertexBufferObjectManager);
    }

    @Override
    public synchronized void hitUnit(final int hitPower) {
        mUnitHealth -= hitPower;
        if (mUnitHealth < 0) {
            if (mObjectDestroyedListener != null)
                mObjectDestroyedListener.unitDestroyed(this);
        }
    }

    @Override
    protected void attackGoal() {
        mUnitToAttack.hitUnit(mDamage);
    }
}
