package com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.unit.stationary;

import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.unit.UnitBuilder;
import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.unit.UnitDummy;
import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.unit.loader.UnitLoader;

import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

/** unmovable/stationary unit */
public class StationaryUnitDummy extends UnitDummy {
    public StationaryUnitDummy(UnitLoader unitLoader, String allianceName) {
        super(unitLoader, allianceName);
    }

    @Override
    protected UnitBuilder createUnitBuilder(ITextureRegion textureRegion,
                                            VertexBufferObjectManager objectManager) {
        return new StationaryUnitBuilder(textureRegion, objectManager);
    }
}
