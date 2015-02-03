package com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.unit.dynamic;

import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.unit.UnitBuilder;
import com.gmail.yaroslavlancelot.eafall.game.sound.SoundOperations;

import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

/** movable unit builder */
public class MovableUnitBuilder extends UnitBuilder {
    private float mSpeed;

    public MovableUnitBuilder(ITextureRegion textureRegion, SoundOperations soundOperations, VertexBufferObjectManager objectManager) {
        super(textureRegion, soundOperations, objectManager);
    }

    public float getSpeed() {
        return mSpeed;
    }

    public MovableUnitBuilder setSpeed(float speed) {
        mSpeed = speed;
        return this;
    }
}
