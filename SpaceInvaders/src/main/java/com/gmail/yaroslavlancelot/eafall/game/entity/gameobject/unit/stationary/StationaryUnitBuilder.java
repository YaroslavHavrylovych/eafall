package com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.unit.stationary;

import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.unit.UnitBuilder;
import com.gmail.yaroslavlancelot.eafall.game.sound.SoundOperations;

import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

/** stationary/unmovable unit builder */
public class StationaryUnitBuilder extends UnitBuilder {
    public StationaryUnitBuilder(ITextureRegion textureRegion, SoundOperations soundOperations, VertexBufferObjectManager objectManager) {
        super(textureRegion, soundOperations, objectManager);
    }
}
