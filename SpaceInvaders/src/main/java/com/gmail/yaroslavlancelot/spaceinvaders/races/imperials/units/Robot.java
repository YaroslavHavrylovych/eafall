package com.gmail.yaroslavlancelot.spaceinvaders.races.imperials.units;

import android.content.Context;
import com.gmail.yaroslavlancelot.spaceinvaders.constants.GameStringsConstantsAndUtils;
import com.gmail.yaroslavlancelot.spaceinvaders.constants.SizeConstants;
import com.gmail.yaroslavlancelot.spaceinvaders.utils.interfaces.EntityOperations;
import com.gmail.yaroslavlancelot.spaceinvaders.utils.interfaces.SoundOperations;
import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.equipment.armor.Electrical;
import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.equipment.weapons.Annihilator;
import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.objects.dynamicobjects.Bullet;
import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.objects.dynamicobjects.Unit;
import com.gmail.yaroslavlancelot.spaceinvaders.races.imperials.Imperials;
import com.gmail.yaroslavlancelot.spaceinvaders.utils.Area;
import com.gmail.yaroslavlancelot.spaceinvaders.utils.TextureRegionHolderUtils;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;

public class Robot extends Unit {
    public static final String KEY_IMPERIALS_SIX_UNIT = GameStringsConstantsAndUtils.getPathToUnits(Imperials.RACE_NAME) + "robot.png";
    public static final String ROBOT_FIRE_SOUND_PATH = GameStringsConstantsAndUtils.getPathToSounds(Imperials.RACE_NAME) + "annihilation_1s.ogg";

    public Robot(final EntityOperations entityOperations, final SoundOperations soundOperations) {
        super(TextureRegionHolderUtils.getInstance().getElement(KEY_IMPERIALS_SIX_UNIT), soundOperations, entityOperations);
        setWidth(SizeConstants.UNIT_SIZE);
        setHeight(SizeConstants.UNIT_SIZE);
        initHealth(500);
        mObjectArmor = new Electrical(10);
        mObjectDamage = new Annihilator(40);
        mAttackRadius = 110;
        mViewRadius = 240;
        setReloadTime(1);
        initSound(ROBOT_FIRE_SOUND_PATH);
    }

    public static void loadResources(final Context context, final BitmapTextureAtlas textureAtlas) {
        loadResource(KEY_IMPERIALS_SIX_UNIT, context, textureAtlas, 32, 16);
    }

    @Override
    protected void attackGoal() {
        if (!isReloadFinished())
            return;
        playSound(mFireSound, mSoundOperations);
        Bullet bullet = new Bullet(getVertexBufferObjectManager(), mEntityOperations, getBackgroundColor());
        bullet.fire(getX() + SizeConstants.UNIT_SIZE / 2, getY() - Bullet.BULLET_SIZE,
                mObjectToAttack.getCenterX(), mObjectToAttack.getCenterY(), mEnemiesUpdater, mObjectDamage);

        mEntityOperations.attachEntity(bullet);
    }

    @Override
    public void setBackgroundArea() {
        setBackgroundArea(new Area(7, 3, 2, 8));
    }
}
