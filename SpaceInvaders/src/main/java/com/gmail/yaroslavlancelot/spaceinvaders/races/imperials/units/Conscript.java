package com.gmail.yaroslavlancelot.spaceinvaders.races.imperials.units;

import android.content.Context;

import com.gmail.yaroslavlancelot.spaceinvaders.constants.GameStringsConstantsAndUtils;
import com.gmail.yaroslavlancelot.spaceinvaders.constants.SizeConstants;
import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.equipment.armor.Physical;
import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.equipment.weapons.Annihilator;
import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.objects.GameObject;
import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.objects.UpdatableGameObject;
import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.objects.dynamicobjects.Unit;
import com.gmail.yaroslavlancelot.spaceinvaders.races.imperials.Imperials;
import com.gmail.yaroslavlancelot.spaceinvaders.utils.Area;
import com.gmail.yaroslavlancelot.spaceinvaders.utils.TextureRegionHolderUtils;
import com.gmail.yaroslavlancelot.spaceinvaders.utils.interfaces.EntityOperations;
import com.gmail.yaroslavlancelot.spaceinvaders.utils.interfaces.SoundOperations;

import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;

public class Conscript extends Unit implements UpdatableGameObject<Conscript> {
    public static final String CONSCRIPT_IMAGE_PATH = GameStringsConstantsAndUtils.getPathToUnits(Imperials.RACE_NAME) + "conscript.png";
    public static final String CONSCRIPT_FIRE_SOUND_PATH = GameStringsConstantsAndUtils.getPathToSounds(Imperials.RACE_NAME) + "annihilation_1s.ogg";

    public Conscript(final EntityOperations entityOperations, final SoundOperations soundOperations) {
        super(TextureRegionHolderUtils.getInstance().getElement(CONSCRIPT_IMAGE_PATH), soundOperations, entityOperations);
        setWidth(SizeConstants.UNIT_SIZE);
        setHeight(SizeConstants.UNIT_SIZE);
        initHealth(450);
        mObjectArmor = new Physical(5);
        mObjectDamage = new Annihilator(20);
        mAttackRadius = 70;
        mViewRadius = 190;
        setReloadTime(1.7);
        initSound(CONSCRIPT_FIRE_SOUND_PATH);
    }

    public static void loadResources(final Context context, final BitmapTextureAtlas textureAtlas) {
        loadResource(CONSCRIPT_IMAGE_PATH, context, textureAtlas, 0, 0);
    }

    @Override
    public void setBackgroundArea() {
        setBackgroundArea(new Area(1, 11, 14, 3));
    }

    @Override
    public Conscript getUpdatedGameObject() {
        //TODO this method should return an instance of the upper level of current object
        return null;
    }
}