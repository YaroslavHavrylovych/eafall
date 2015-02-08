package com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.unit.dynamic;

import com.gmail.yaroslavlancelot.eafall.game.constant.StringsAndPath;
import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.unit.UnitDummy;
import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.unit.loader.UnitLoader;
import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.unit.pool.MovableUnitsPool;
import com.gmail.yaroslavlancelot.eafall.game.sound.SoundOperations;

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
        mPool = new MovableUnitsPool(initBuilder(objectManager, soundOperations, allianceName));
    }

    /** create and return movable unit builder */
    private MovableUnitBuilder initBuilder(VertexBufferObjectManager objectManager, SoundOperations soundOperations, String allianceName) {
        MovableUnitBuilder unitBuilder = new MovableUnitBuilder(mTextureRegion, soundOperations, objectManager);

        unitBuilder.setSpeed(getSpeed())
                .setHealth(getHealth())
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

    public float getSpeed() {
        return mUnitSpeed;
    }

    public MovableUnit constructUnit() {
        return mPool.obtainPoolItem();
    }
}
