package com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.unit.stationary;

import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.unit.UnitBuilder;

import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

/** stationary/unmovable unit builder */
public class StationaryUnitBuilder extends UnitBuilder {
    public StationaryUnitBuilder(ITextureRegion textureRegion, VertexBufferObjectManager objectManager) {
        super(textureRegion, objectManager);
    }
}
