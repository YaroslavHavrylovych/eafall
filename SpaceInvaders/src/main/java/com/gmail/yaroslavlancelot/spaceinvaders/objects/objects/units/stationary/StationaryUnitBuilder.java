package com.gmail.yaroslavlancelot.spaceinvaders.objects.objects.units.stationary;

import com.gmail.yaroslavlancelot.spaceinvaders.objects.objects.units.UnitBuilder;
import com.gmail.yaroslavlancelot.spaceinvaders.utils.interfaces.SoundOperations;

import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

/** stationary/unmovable unit builder */
public class StationaryUnitBuilder extends UnitBuilder {
    public StationaryUnitBuilder(ITextureRegion textureRegion, SoundOperations soundOperations, VertexBufferObjectManager objectManager) {
        super(textureRegion, soundOperations, objectManager);
    }
}
