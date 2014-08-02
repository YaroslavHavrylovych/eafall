package com.gmail.yaroslavlancelot.spaceinvaders.races.imperials.units;

import android.content.Context;

import com.gmail.yaroslavlancelot.spaceinvaders.constants.GameStringsConstantsAndUtils;
import com.gmail.yaroslavlancelot.spaceinvaders.constants.SizeConstants;
import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.equipment.armor.Magnetic;
import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.equipment.weapons.Higgs;
import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.objects.dynamicobjects.Unit;
import com.gmail.yaroslavlancelot.spaceinvaders.races.imperials.Imperials;
import com.gmail.yaroslavlancelot.spaceinvaders.utils.Area;
import com.gmail.yaroslavlancelot.spaceinvaders.utils.TextureRegionHolderUtils;
import com.gmail.yaroslavlancelot.spaceinvaders.utils.interfaces.SoundOperations;

import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

public class Demolisher extends Unit {
    public static final String KEY_IMPERIALS_SEVEN_UNIT = GameStringsConstantsAndUtils.getPathToUnits(Imperials.RACE_NAME) + "demolisher.png";
    public static final String DEMOLISHER_FIRE_SOUND_PATH = GameStringsConstantsAndUtils.getPathToSounds(Imperials.RACE_NAME) + "higgs_3s.ogg";

    public Demolisher(final VertexBufferObjectManager objectManager, final SoundOperations soundOperations) {
        super(TextureRegionHolderUtils.getInstance().getElement(KEY_IMPERIALS_SEVEN_UNIT), soundOperations, objectManager);
        setWidth(SizeConstants.UNIT_SIZE);
        setHeight(SizeConstants.UNIT_SIZE);
        initHealth(200);
        mObjectArmor = new Magnetic(1);
        mObjectDamage = new Higgs(50);
        mAttackRadius = 190;
        mViewRadius = 240;
        setReloadTime(3.5);
        initSound(DEMOLISHER_FIRE_SOUND_PATH);
    }

    public static void loadResources(final Context context, final BitmapTextureAtlas textureAtlas) {
        loadResource(KEY_IMPERIALS_SEVEN_UNIT, context, textureAtlas, 0, 32);
    }

    @Override
    public void setBackgroundArea() {
        setBackgroundArea(new Area(6, 6, 4, 4));
    }
}
