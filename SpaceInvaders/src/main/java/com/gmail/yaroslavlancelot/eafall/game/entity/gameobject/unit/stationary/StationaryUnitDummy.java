package com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.unit.stationary;

import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.unit.UnitBuilder;
import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.unit.UnitDummy;
import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.unit.loader.UnitLoader;
import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.unit.pool.StationaryUnitsPool;

import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

/** unmovable/stationary unit */
public class StationaryUnitDummy extends UnitDummy {
    /** pool for faster creation */
    private StationaryUnitsPool mPool;

    public StationaryUnitDummy(UnitLoader unitLoader, String allianceName) {
        super(unitLoader, allianceName);
    }

    @Override
    public void initDummy(VertexBufferObjectManager objectManager) {
        /* for unit creation */
        mPool = new StationaryUnitsPool(
                (StationaryUnitBuilder) initBuilder(objectManager));
    }

    public StationaryUnit constructUnit() {
        return mPool.obtainPoolItem();
    }

    @Override
    protected UnitBuilder createUnitBuilder(ITextureRegion textureRegion,
                                            VertexBufferObjectManager objectManager) {
        return new StationaryUnitBuilder(textureRegion, objectManager);
    }
}
