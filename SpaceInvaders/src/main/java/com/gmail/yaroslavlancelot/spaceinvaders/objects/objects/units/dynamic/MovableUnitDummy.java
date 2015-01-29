package com.gmail.yaroslavlancelot.spaceinvaders.objects.objects.units.dynamic;

import com.gmail.yaroslavlancelot.spaceinvaders.constants.StringsAndPathUtils;
import com.gmail.yaroslavlancelot.spaceinvaders.objects.objects.loading.units.UnitLoader;
import com.gmail.yaroslavlancelot.spaceinvaders.objects.objects.units.UnitDummy;
import com.gmail.yaroslavlancelot.spaceinvaders.utils.interfaces.SoundOperations;

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
                .setSoundPath(StringsAndPathUtils.getPathToSounds(allianceName) + mUnitLoader.sound)
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
