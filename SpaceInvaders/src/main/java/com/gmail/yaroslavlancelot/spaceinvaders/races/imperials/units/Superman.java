package com.gmail.yaroslavlancelot.spaceinvaders.races.imperials.units;

import android.content.Context;

import com.badlogic.gdx.physics.box2d.BodyDef;
import com.gmail.yaroslavlancelot.spaceinvaders.constants.GameStringsConstantsAndUtils;
import com.gmail.yaroslavlancelot.spaceinvaders.constants.SizeConstants;
import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.equipment.armor.Higgs;
import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.equipment.weapons.Annihilator;
import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.objects.dynamicobjects.Bullet;
import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.objects.dynamicobjects.Unit;
import com.gmail.yaroslavlancelot.spaceinvaders.races.imperials.Imperials;
import com.gmail.yaroslavlancelot.spaceinvaders.utils.Area;
import com.gmail.yaroslavlancelot.spaceinvaders.utils.TextureRegionHolderUtils;
import com.gmail.yaroslavlancelot.spaceinvaders.utils.interfaces.EntityOperations;
import com.gmail.yaroslavlancelot.spaceinvaders.utils.interfaces.SoundOperations;

import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;

public class Superman extends Unit {
    public static final String KEY_IMPERIALS_EIGHT_UNIT = GameStringsConstantsAndUtils.getPathToUnits(Imperials.RACE_NAME) + "superman.png";
    public static final String SUPERMAN_FIRE_SOUND_PATH = GameStringsConstantsAndUtils.getPathToSounds(Imperials.RACE_NAME) + "higgs_3s.ogg";

    public Superman(final EntityOperations entityOperations, final SoundOperations soundOperations) {
        super(TextureRegionHolderUtils.getInstance().getElement(KEY_IMPERIALS_EIGHT_UNIT), soundOperations, entityOperations);
        setWidth(SizeConstants.UNIT_SIZE);
        setHeight(SizeConstants.UNIT_SIZE);
        initHealth(1000);
        mObjectArmor = new Higgs(10);
        mObjectDamage = new Annihilator(60);
        mAttackRadius = 90;
        mViewRadius = 190;
        setReloadTime(3);
        initSound(SUPERMAN_FIRE_SOUND_PATH);
    }

    public static void loadResources(final Context context, final BitmapTextureAtlas textureAtlas) {
        loadResource(KEY_IMPERIALS_EIGHT_UNIT, context, textureAtlas, 16, 32);
    }

    @Override
    public void setBackgroundArea() {
        setBackgroundArea(new Area(6, 1, 4, 4));
    }
}
