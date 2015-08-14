package com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.unit.dynamic;

import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.unit.UnitBuilder;

import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

/** movable unit builder */
public class MovableUnitBuilder extends UnitBuilder {
    private float mSpeed;

    public MovableUnitBuilder(ITextureRegion textureRegion, VertexBufferObjectManager objectManager) {
        super(textureRegion, objectManager);
    }

    public float getSpeed() {
        return mSpeed;
    }

    public MovableUnitBuilder setSpeed(float speed) {
        mSpeed = speed;
        return this;
    }
}
