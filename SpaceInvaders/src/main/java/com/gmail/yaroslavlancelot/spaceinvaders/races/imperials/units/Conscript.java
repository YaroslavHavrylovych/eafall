package com.gmail.yaroslavlancelot.spaceinvaders.races.imperials.units;

import android.content.Context;
import com.gmail.yaroslavlancelot.spaceinvaders.constants.GameStringsConstantsAndUtils;
import com.gmail.yaroslavlancelot.spaceinvaders.constants.SizeConstants;
import com.gmail.yaroslavlancelot.spaceinvaders.game.interfaces.EntityOperations;
import com.gmail.yaroslavlancelot.spaceinvaders.game.interfaces.SoundOperations;
import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.equipment.armor.Physical;
import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.equipment.weapons.Annihilator;
import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.objects.dynamicobjects.Bullet;
import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.objects.dynamicobjects.Unit;
import com.gmail.yaroslavlancelot.spaceinvaders.races.imperials.Imperials;
import com.gmail.yaroslavlancelot.spaceinvaders.utils.Area;
import com.gmail.yaroslavlancelot.spaceinvaders.utils.TextureRegionHolderUtils;
import org.andengine.audio.sound.Sound;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

public class Conscript extends Unit {
    public static final String CONSCRIPT_IMAGE_PATH = GameStringsConstantsAndUtils.getPathToUnits(Imperials.RACE_NAME) + "conscript.png";
    public static final String CONSCRIPT_FIRE_SOUND_PATH = GameStringsConstantsAndUtils.getPathToSounds(Imperials.RACE_NAME) + "annihilation.ogg";
    private EntityOperations mEntityOperations;
    private Sound mFireSound;

    public Conscript(final VertexBufferObjectManager vertexBufferObjectManager, EntityOperations entityOperations, SoundOperations soundOperations) {
        super(TextureRegionHolderUtils.getInstance().getElement(CONSCRIPT_IMAGE_PATH), vertexBufferObjectManager);
        setWidth(SizeConstants.UNIT_SIZE);
        setHeight(SizeConstants.UNIT_SIZE);
        initHealth(450);
        mObjectArmor = new Physical(5);
        mObjectDamage = new Annihilator(20);
        mAttackRadius = 70;
        mViewRadius = 190;
        mFireSound = soundOperations.loadSound(CONSCRIPT_FIRE_SOUND_PATH);
        mEntityOperations = entityOperations;
    }

    public static void loadResources(final Context context, final BitmapTextureAtlas textureAtlas) {
        loadResource(CONSCRIPT_IMAGE_PATH, context, textureAtlas, 0, 0);
    }

    @Override
    protected void attackGoal() {
        if (mFireSound != null)
            mFireSound.play();
        Bullet bullet = new Bullet(getVertexBufferObjectManager(), mEntityOperations, getBackgroundColor());
        bullet.fire(getX() + SizeConstants.UNIT_SIZE / 2, getY() - Bullet.BULLET_SIZE,
                mObjectToAttack.getCenterX(), mObjectToAttack.getCenterY(), mEnemiesUpdater, mObjectDamage);

        mEntityOperations.attachEntity(bullet);
    }

    @Override
    public void setBackgroundArea() {
        setBackgroundArea(new Area(1, 11, 14, 3));
    }
}
