package com.gmail.yaroslavlancelot.spaceinvaders.races.imperials.units;

import android.content.Context;

import com.badlogic.gdx.physics.box2d.BodyDef;
import com.gmail.yaroslavlancelot.spaceinvaders.constants.GameStringsConstantsAndUtils;
import com.gmail.yaroslavlancelot.spaceinvaders.constants.SizeConstants;
import com.gmail.yaroslavlancelot.spaceinvaders.utils.interfaces.EntityOperations;
import com.gmail.yaroslavlancelot.spaceinvaders.utils.interfaces.SoundOperations;
import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.equipment.armor.HeavyWater;
import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.equipment.weapons.Neutrino;
import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.objects.dynamicobjects.Bullet;
import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.objects.dynamicobjects.Unit;
import com.gmail.yaroslavlancelot.spaceinvaders.races.imperials.Imperials;
import com.gmail.yaroslavlancelot.spaceinvaders.utils.Area;
import com.gmail.yaroslavlancelot.spaceinvaders.utils.TextureRegionHolderUtils;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;

public class Sniper extends Unit {
    public static final String KEY_IMPERIALS_FOURTH_UNIT = GameStringsConstantsAndUtils.getPathToUnits(Imperials.RACE_NAME) + "sniper.png";
    public static final String SNIPER_FIRE_SOUND_PATH = GameStringsConstantsAndUtils.getPathToSounds(Imperials.RACE_NAME) + "neutrino_3s.ogg";

    public Sniper(final EntityOperations entityOperations, final SoundOperations soundOperations) {
        super(TextureRegionHolderUtils.getInstance().getElement(KEY_IMPERIALS_FOURTH_UNIT), soundOperations, entityOperations);
        setWidth(SizeConstants.UNIT_SIZE);
        setHeight(SizeConstants.UNIT_SIZE);
        initHealth(220);
        mObjectArmor = new HeavyWater(3);
        mObjectDamage = new Neutrino(50);
        mAttackRadius = 240;
        mViewRadius = 340;
        setReloadTime(3.1);
        initSound(SNIPER_FIRE_SOUND_PATH);
    }

    public static void loadResources(final Context context, final BitmapTextureAtlas textureAtlas) {
        loadResource(KEY_IMPERIALS_FOURTH_UNIT, context, textureAtlas, 0, 16);
    }

    @Override
    public void setBackgroundArea() {
        setBackgroundArea(new Area(6, 3, 4, 10));
    }
}
