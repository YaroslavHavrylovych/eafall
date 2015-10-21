package com.gmail.yaroslavlancelot.eafall.game.entity.bullets;

import com.gmail.yaroslavlancelot.eafall.game.batching.BatchingKeys;
import com.gmail.yaroslavlancelot.eafall.game.entity.BatchedSprite;
import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.GameObject;
import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.equipment.damage.Damage;
import com.gmail.yaroslavlancelot.eafall.game.events.aperiodic.ingame.AttachSpriteEvent;

import org.andengine.entity.modifier.MoveModifier;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.modifier.IModifier;

import de.greenrobot.event.EventBus;

/**
 * Bullet
 */
public class Bullet extends BatchedSprite implements IModifier.IModifierListener {
    /** bullet size in points */
    private static final float sDuration = 0.5f;
    /** bullet damage value */
    private Damage mDamage;
    /** bullet lifecycle handler */
    private MoveModifier mMoveModifier;
    private GameObject mTarget;
    private long mTargetId;
    private boolean mIsAttached;

    public Bullet(int width, int height, ITextureRegion textureRegion, VertexBufferObjectManager vertexBufferObjectManager) {
        super(-100, -100, width, height, textureRegion, vertexBufferObjectManager);
        setSpriteGroupName(BatchingKeys.BULLET_AND_HEALTH);
        /* the maximum distance bullet can damage (it's usual 2 times distance than it's enemy) */
        mMoveModifier = new MoveModifier(sDuration, 0, 0, 0, 0);
        mMoveModifier.setAutoUnregisterWhenFinished(false);
    }

    public Damage getDamage() {
        return mDamage;
    }

    @Override
    public void onModifierStarted(final IModifier pModifier, final Object pItem) {
    }

    @Override
    public void onModifierFinished(final IModifier pModifier, final Object pItem) {
        if (mTarget.getObjectUniqueId() == mTargetId && mTarget.isObjectAlive()) {
            mTarget.damageObject(mDamage);
        }
        setPosition(-100, -100);
        onBulletDestroyed();
    }

    public void fire(Damage damage, float x, float y, GameObject gameObject) {
        mDamage = damage;
        mTarget = gameObject;
        mTargetId = gameObject.getObjectUniqueId();
        mMoveModifier.reset(sDuration, x, y, gameObject.getX(), gameObject.getY());
        if (!mIsAttached) {
            mMoveModifier.addModifierListener(Bullet.this);
            EventBus.getDefault().post(new AttachSpriteEvent(this));
            mIsAttached = true;
            registerEntityModifier(mMoveModifier);
        }
    }

    protected void onBulletDestroyed() {
    }
}
