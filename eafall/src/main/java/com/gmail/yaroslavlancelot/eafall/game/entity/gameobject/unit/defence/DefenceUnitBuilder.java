package com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.unit.defence;

import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.unit.UnitBuilder;

import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

/** stationary/unmovable unit builder */
public class DefenceUnitBuilder extends UnitBuilder {
    public DefenceUnitBuilder(ITextureRegion textureRegion, VertexBufferObjectManager objectManager) {
        super(textureRegion, objectManager);
    }
}
