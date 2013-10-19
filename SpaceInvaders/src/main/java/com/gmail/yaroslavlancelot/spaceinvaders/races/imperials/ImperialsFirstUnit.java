package com.gmail.yaroslavlancelot.spaceinvaders.races.imperials;

import com.gmail.yaroslavlancelot.spaceinvaders.constants.SizeConstants;
import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.objects.dynamicobjects.Unit;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

/** unit which can attack without bullets */
public class ImperialsFirstUnit extends Unit {
    public ImperialsFirstUnit(final float x, final float y, final ITextureRegion textureRegion,
                              final VertexBufferObjectManager vertexBufferObjectManager) {
        super(x, y, textureRegion, vertexBufferObjectManager);
        setWidth(SizeConstants.UNIT_SIZE);
        setHeight(SizeConstants.UNIT_SIZE);
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
