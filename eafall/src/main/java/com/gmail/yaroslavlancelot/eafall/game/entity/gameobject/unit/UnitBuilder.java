package com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.unit;

import com.gmail.yaroslavlancelot.eafall.game.audio.LimitedSoundWrapper;
import com.gmail.yaroslavlancelot.eafall.game.constant.SizeConstants;
import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.equipment.armor.Armor;
import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.equipment.damage.Damage;

import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.adt.spatial.bounds.util.FloatBoundsUtils;

/** unit Builder, for unit children faster creation */
public abstract class UnitBuilder {
    /** holds unit sprite texture region (for faster unit creation) */
    protected ITextureRegion mTextureRegion;
    protected VertexBufferObjectManager mObjectManager;
    /** unit health */
    protected int mHealth;
    /** unit armor */
    protected Armor mArmor;
    /** unit damage */
    protected Damage mDamage;
    protected int mAttackRadius;
    protected int mViewRadius;
    protected double mReloadTime;
    protected LimitedSoundWrapper mFireSound;
    protected float[] size = new float[2];

    public UnitBuilder(ITextureRegion textureRegion, VertexBufferObjectManager objectManager) {
        mTextureRegion = textureRegion;
        mObjectManager = objectManager;
        size[0] = textureRegion.getWidth();
        size[1] = textureRegion.getHeight();
        FloatBoundsUtils.proportionallyBound(size, SizeConstants.UNIT_SIZE);
    }

    public ITextureRegion getTextureRegion() {
        return mTextureRegion;
    }

    public VertexBufferObjectManager getObjectManager() {
        return mObjectManager;
    }

    public int getHealth() {
        return mHealth;
    }

    public UnitBuilder setHealth(int health) {
        mHealth = health;
        return this;
    }

    public Armor getArmor() {
        return mArmor;
    }

    public UnitBuilder setArmor(Armor armor) {
        mArmor = armor;
        return this;
    }

    public Damage getDamage() {
        return mDamage;
    }

    public UnitBuilder setDamage(Damage damage) {
        mDamage = damage;
        return this;
    }

    public int getAttackRadius() {
        return mAttackRadius;
    }

    public UnitBuilder setAttackRadius(int attackRadius) {
        mAttackRadius = attackRadius;
        return this;
    }

    public int getViewRadius() {
        return mViewRadius;
    }

    public UnitBuilder setViewRadius(int viewRadius) {
        mViewRadius = viewRadius;
        return this;
    }

    public double getReloadTime() {
        return mReloadTime;
    }

    public UnitBuilder setReloadTime(double reloadTime) {
        mReloadTime = reloadTime;
        return this;
    }

    public LimitedSoundWrapper getFireSound() {
        return mFireSound;
    }

    public UnitBuilder setFireSound(LimitedSoundWrapper fireSound) {
        mFireSound = fireSound;
        return this;
    }

    public float getWidth() {
        return size[0];
    }

    public float getHeight() {
        return size[1];
    }
}
