package com.gmail.yaroslavlancelot.spaceinvaders.objects.objects.units.stationary;

import com.gmail.yaroslavlancelot.spaceinvaders.constants.StringsAndPathUtils;
import com.gmail.yaroslavlancelot.spaceinvaders.objects.objects.loading.units.UnitLoader;
import com.gmail.yaroslavlancelot.spaceinvaders.objects.objects.units.UnitDummy;
import com.gmail.yaroslavlancelot.spaceinvaders.utils.interfaces.SoundOperations;

import org.andengine.opengl.vbo.VertexBufferObjectManager;

/** unmovable/stationary unit */
public class StationaryUnitDummy extends UnitDummy {

    public StationaryUnitDummy(UnitLoader unitLoader, String allianceName) {
        super(unitLoader, allianceName);
    }

    public StationaryUnit constructUnit(VertexBufferObjectManager objectManager, SoundOperations soundOperations, String allianceName) {
        StationaryUnitBuilder unitBuilder = new StationaryUnitBuilder(mTextureRegion, soundOperations, objectManager);

        unitBuilder.setHealth(getHealth())
                .setViewRadius(mUnitLoader.view_radius)
                .setAttackRadius(mUnitLoader.attack_radius)
                .setReloadTime(mUnitLoader.reload_time)
                .setSoundPath(StringsAndPathUtils.getPathToSounds(allianceName) + mUnitLoader.sound)
                .setDamage(getDamage())
                .setWidth(getWidth())
                .setHeight(getHeight())
                .setArmor(getArmor());

        return new StationaryUnit(unitBuilder);
    }
}
