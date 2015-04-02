package com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.unit.dynamic;

import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.unit.UnitBuilder;
import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.unit.UnitDummy;
import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.unit.loader.UnitLoader;
import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.unit.pool.MovableUnitsPool;
import com.gmail.yaroslavlancelot.eafall.game.sound.SoundOperations;

import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

/**
 * extends unit with speed for moving
 */
public class MovableUnitDummy extends UnitDummy {
    /** speed of the unit */
    private final float mUnitSpeed;
    /** pool for faster creation */
    private MovableUnitsPool mPool;

    public MovableUnitDummy(UnitLoader unitLoader, String allianceName) {
        super(unitLoader, allianceName);
        mUnitSpeed = ((float) mUnitLoader.speed) / 100;
    }

    @Override
    public void initDummy(VertexBufferObjectManager objectManager, SoundOperations soundOperations, String allianceName) {
        /* for unit creation */
        mPool = new MovableUnitsPool(
                (MovableUnitBuilder) initBuilder(objectManager, soundOperations, allianceName));
    }

    /** create and return movable unit builder */
    protected UnitBuilder initBuilder(VertexBufferObjectManager objectManager, SoundOperations soundOperations, String allianceName) {
        return ((MovableUnitBuilder) super.initBuilder(objectManager, soundOperations, allianceName))
                .setSpeed(getSpeed());
    }

    @Override
    protected UnitBuilder createUnitBuilder(ITextureRegion textureRegion,
                                            SoundOperations soundOperations,
                                            VertexBufferObjectManager objectManager) {
        return new MovableUnitBuilder(textureRegion, soundOperations, objectManager);
    }

    public float getSpeed() {
        return mUnitSpeed;
    }

    public MovableUnit constructUnit() {
        return mPool.obtainPoolItem();
    }
}
