package com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.unit.stationary;

import com.gmail.yaroslavlancelot.eafall.game.constant.StringsAndPath;
import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.unit.loader.UnitLoader;
import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.unit.UnitDummy;
import com.gmail.yaroslavlancelot.eafall.game.sound.SoundOperations;

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
                .setSoundPath(StringsAndPath.getPathToSounds(allianceName) + mUnitLoader.sound)
                .setDamage(getDamage())
                .setWidth(getWidth())
                .setHeight(getHeight())
                .setArmor(getArmor());

        return new StationaryUnit(unitBuilder);
    }
}
