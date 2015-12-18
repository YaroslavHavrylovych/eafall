package com.yaroslavlancelot.eafall.game.entity.gameobject.unit.defence;

import com.yaroslavlancelot.eafall.game.entity.gameobject.unit.UnitBuilder;
import com.yaroslavlancelot.eafall.game.entity.gameobject.unit.UnitDummy;
import com.yaroslavlancelot.eafall.game.entity.gameobject.unit.loader.UnitLoader;

import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

/** unmovable/stationary unit */
public class DefenceUnitDummy extends UnitDummy {
    public DefenceUnitDummy(UnitLoader unitLoader, String allianceName) {
        super(unitLoader, allianceName);
    }

    @Override
    protected UnitBuilder createUnitBuilder(ITextureRegion textureRegion,
                                            VertexBufferObjectManager objectManager) {
        return new DefenceUnitBuilder(textureRegion, objectManager);
    }
}
