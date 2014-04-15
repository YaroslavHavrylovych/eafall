package com.gmail.yaroslavlancelot.spaceinvaders.races.imperials.units;

import android.content.Context;

import com.badlogic.gdx.physics.box2d.BodyDef;
import com.gmail.yaroslavlancelot.spaceinvaders.constants.GameStringsConstantsAndUtils;
import com.gmail.yaroslavlancelot.spaceinvaders.constants.SizeConstants;
import com.gmail.yaroslavlancelot.spaceinvaders.utils.interfaces.EntityOperations;
import com.gmail.yaroslavlancelot.spaceinvaders.utils.interfaces.SoundOperations;
import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.equipment.armor.Magnetic;
import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.equipment.weapons.Electric;
import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.objects.dynamicobjects.Bullet;
import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.objects.dynamicobjects.Unit;
import com.gmail.yaroslavlancelot.spaceinvaders.races.imperials.Imperials;
import com.gmail.yaroslavlancelot.spaceinvaders.utils.Area;
import com.gmail.yaroslavlancelot.spaceinvaders.utils.TextureRegionHolderUtils;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;

public class Scout extends Unit {
    public static final String KEY_IMPERIALS_SECOND_UNIT = GameStringsConstantsAndUtils.getPathToUnits(Imperials.RACE_NAME) + "scout.png";
    public static final String SCOUT_FIRE_SOUND_PATH = GameStringsConstantsAndUtils.getPathToSounds(Imperials.RACE_NAME) + "electrical_1s.ogg";

    public Scout(final EntityOperations entityOperations, final SoundOperations soundOperations) {
        super(TextureRegionHolderUtils.getInstance().getElement(KEY_IMPERIALS_SECOND_UNIT), soundOperations, entityOperations);
        setWidth(SizeConstants.UNIT_SIZE);
        setHeight(SizeConstants.UNIT_SIZE);
        initHealth(300);
        mObjectArmor = new Magnetic(2);
        mObjectDamage = new Electric(30);
        mAttackRadius = 90;
        mViewRadius = 190;
        setReloadTime(1.4);
        initSound(SCOUT_FIRE_SOUND_PATH);
    }

    @Override
    protected void attackGoal() {
        super.attackGoal();
        if (!isReloadFinished())
            return;
        playSound(mFireSound, mSoundOperations);
        Bullet bullet = new Bullet(getVertexBufferObjectManager(), mEntityOperations, getBackgroundColor(),
                getBody().getType().equals(BodyDef.BodyType.KinematicBody));
        bullet.fire(getX() + SizeConstants.UNIT_SIZE / 2, getY() - Bullet.BULLET_SIZE,
                mObjectToAttack.getCenterX(), mObjectToAttack.getCenterY(), mEnemiesUpdater, mObjectDamage);

        mEntityOperations.attachEntity(bullet);
    }

    public static void loadResources(final Context context, final BitmapTextureAtlas textureAtlas) {
        loadResource(KEY_IMPERIALS_SECOND_UNIT, context, textureAtlas, 16, 0);
    }

    @Override
    public void setBackgroundArea() {
        setBackgroundArea(new Area(2, 8, 12, 3));
    }
}
