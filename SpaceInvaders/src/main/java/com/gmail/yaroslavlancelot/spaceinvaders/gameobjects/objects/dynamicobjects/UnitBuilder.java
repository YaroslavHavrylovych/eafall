package com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.objects.dynamicobjects;

import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.equipment.armor.Armor;
import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.equipment.weapons.Damage;
import com.gmail.yaroslavlancelot.spaceinvaders.utils.interfaces.SoundOperations;

import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

/** unit Builder */
public class UnitBuilder {
    private ITextureRegion mTextureRegion;
    private SoundOperations mSoundOperations;
    private VertexBufferObjectManager mObjectManager;
    /** unit health */
    private int mHealth;
    /** unit armor */
    private Armor mArmor;
    /** unit damage */
    private Damage mDamage;
    private int mAttackRadius;
    private int mViewRadius;
    private double mReloadTime;
    private String mSoundPath;
    private int mWidth;
    private int mHeight;

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

    public UnitBuilder setWidth(int width) {
        mWidth = width;
        return this;
    }

    public UnitBuilder setHeight(int height) {
        mHeight = height;
        return this;
    }

    public int getWidth() {
        return mWidth;
    }

    public int getHeight() {
        return mHeight;
    }
}
