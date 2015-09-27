package com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.unit.offence;

import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.unit.UnitBuilder;

import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

/** offence unit builder */
public class OffenceUnitBuilder extends UnitBuilder {
    private float mSpeed;

    public OffenceUnitBuilder(ITextureRegion textureRegion, VertexBufferObjectManager objectManager) {
        super(textureRegion, objectManager);
    }

    public float getSpeed() {
        return mSpeed;
    }

    public OffenceUnitBuilder setSpeed(float speed) {
        mSpeed = speed;
        return this;
    }
}
