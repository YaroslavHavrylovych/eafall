package com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.unit;

import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.equipment.armor.Armor;
import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.equipment.damage.Damage;
import com.gmail.yaroslavlancelot.eafall.game.sound.SoundOperations;

import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

/** unit Builder, for unit children faster creation */
public abstract class UnitBuilder {
    /** holds unit image texture region (for faster unit creation) */
    protected ITextureRegion mTextureRegion;
    protected SoundOperations mSoundOperations;
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
    protected String mSoundPath;
    protected int mWidth;
    protected int mHeight;

    public UnitBuilder(ITextureRegion textureRegion, SoundOperations soundOperations, VertexBufferObjectManager objectManager) {
        mTextureRegion = textureRegion;
        mSoundOperations = soundOperations;
        mObjectManager = objectManager;
    }

    public ITextureRegion getTextureRegion() {
        return mTextureRegion;
    }

    public SoundOperations getSoundOperations() {
        return mSoundOperations;
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

    public String getSoundPath() {
        return mSoundPath;
    }

    public UnitBuilder setSoundPath(String soundPath) {
        mSoundPath = soundPath;
        return this;
    }

    public int getWidth() {
        return mWidth;
    }

    public UnitBuilder setWidth(int width) {
        mWidth = width;
        return this;
    }

    public int getHeight() {
        return mHeight;
    }

    public UnitBuilder setHeight(int height) {
        mHeight = height;
        return this;
    }
}
