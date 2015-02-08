package com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.unit.stationary;

import com.gmail.yaroslavlancelot.eafall.game.constant.StringsAndPath;
import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.unit.UnitDummy;
import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.unit.loader.UnitLoader;
import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.unit.pool.StationaryUnitsPool;
import com.gmail.yaroslavlancelot.eafall.game.sound.SoundOperations;

import org.andengine.opengl.vbo.VertexBufferObjectManager;

/** unmovable/stationary unit */
public class StationaryUnitDummy extends UnitDummy {
    /** pool for faster creation */
    private StationaryUnitsPool mPool;

    public StationaryUnitDummy(UnitLoader unitLoader, String allianceName) {
        super(unitLoader, allianceName);
    }

    @Override
    public void initDummy(VertexBufferObjectManager objectManager, SoundOperations soundOperations, String allianceName) {
        /* for unit creation */
        mPool = new StationaryUnitsPool(initBuilder(objectManager, soundOperations, allianceName));
    }

    private StationaryUnitBuilder initBuilder(VertexBufferObjectManager objectManager, SoundOperations soundOperations, String allianceName) {
        StationaryUnitBuilder unitBuilder = new StationaryUnitBuilder(mTextureRegion, soundOperations, objectManager);

        unitBuilder.setHealth(getHealth())
                .setViewRadius(mUnitLoader.view_radius)
                .setAttackRadius(mUnitLoader.attack_radius)
                .setReloadTime(mUnitLoader.reload_time)
                .setSoundPath(StringsAndPath.getPathToSounds(allianceName.toLowerCase()) + mUnitLoader.sound)
                .setDamage(getDamage())
                .setWidth(getWidth())
                .setHeight(getHeight())
                .setArmor(getArmor());

        return unitBuilder;
    }

    public StationaryUnit constructUnit() {
        return mPool.obtainPoolItem();
    }
}
