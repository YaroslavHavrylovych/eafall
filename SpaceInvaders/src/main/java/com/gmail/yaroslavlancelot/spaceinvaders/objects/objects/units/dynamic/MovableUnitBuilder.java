package com.gmail.yaroslavlancelot.spaceinvaders.objects.objects.units.dynamic;

import com.gmail.yaroslavlancelot.spaceinvaders.objects.objects.units.UnitBuilder;
import com.gmail.yaroslavlancelot.spaceinvaders.utils.interfaces.SoundOperations;

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
