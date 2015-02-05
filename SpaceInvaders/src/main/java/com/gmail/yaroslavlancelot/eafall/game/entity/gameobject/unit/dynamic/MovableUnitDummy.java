package com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.unit.dynamic;

import com.gmail.yaroslavlancelot.eafall.game.constant.StringsAndPath;
import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.unit.loader.UnitLoader;
import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.unit.UnitDummy;
import com.gmail.yaroslavlancelot.eafall.game.sound.SoundOperations;

import org.andengine.opengl.vbo.VertexBufferObjectManager;

/**
 * extends unit with speed for moving
 */
public class MovableUnitDummy extends UnitDummy {
    /** speed of the unit */
    private float mUnitSpeed;

    public MovableUnitDummy(UnitLoader unitLoader, String allianceName) {
        super(unitLoader, allianceName);
        mUnitSpeed = ((float) mUnitLoader.speed) / 100;
    }

    public MovableUnit constructUnit(VertexBufferObjectManager objectManager, SoundOperations soundOperations, String allianceName) {
        MovableUnitBuilder unitBuilder = new MovableUnitBuilder(mTextureRegion, soundOperations, objectManager);

        unitBuilder.setSpeed(getSpeed())
                .setHealth(getHealth())
                .setViewRadius(mUnitLoader.view_radius)
                .setAttackRadius(mUnitLoader.attack_radius)
                .setReloadTime(mUnitLoader.reload_time)
                .setSoundPath(StringsAndPath.getPathToSounds(allianceName) + mUnitLoader.sound)
                .setDamage(getDamage())
                .setWidth(getWidth())
                .setHeight(getHeight())
                .setArmor(getArmor());

        return new MovableUnit(unitBuilder);
    }

    public float getSpeed() {
        return mUnitSpeed;
    }
}
