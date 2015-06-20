package com.gmail.yaroslavlancelot.eafall.game.entity.bullets;

import com.gmail.yaroslavlancelot.eafall.game.batching.BatchingKeys;
import com.gmail.yaroslavlancelot.eafall.game.entity.BatchedSprite;
import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.GameObject;
import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.equipment.damage.Damage;
import com.gmail.yaroslavlancelot.eafall.game.eventbus.AttachSpriteEvent;

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
    public static final int BULLET_SIZE = 3;
    private static final float sDuration = 0.5f;
    /** bullet damage value */
    private Damage mDamage;
    /** bullet lifecycle handler */
    private MoveModifier moveModifier;
    private GameObject mTarget;
    private long mTargetId;
    private boolean mIsAttached;

    public Bullet(ITextureRegion textureRegion, VertexBufferObjectManager vertexBufferObjectManager) {
        this(BULLET_SIZE, BULLET_SIZE, textureRegion, vertexBufferObjectManager);
        /* the maximum distance bullet can damage (it's usual 2 times distance than it's enemy) */
        moveModifier = new MoveModifier(sDuration, 0, 0, 0, 0);
        moveModifier.setAutoUnregisterWhenFinished(false);
    }

    public Bullet(int width, int height, ITextureRegion textureRegion, VertexBufferObjectManager vertexBufferObjectManager) {
        super(-100, -100, width, height, textureRegion, vertexBufferObjectManager);
        setSpriteGroupName(BatchingKeys.BULLET_AND_HEALTH);
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
        moveModifier.reset(sDuration, x, y, gameObject.getX(), gameObject.getY());
        if (!mIsAttached) {
            moveModifier.addModifierListener(Bullet.this);
            EventBus.getDefault().post(new AttachSpriteEvent(this));
            mIsAttached = true;
            registerEntityModifier(moveModifier);
        }
    }

    protected void onBulletDestroyed() {
    }
}
