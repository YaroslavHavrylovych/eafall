package com.yaroslavlancelot.eafall.game.entity.bullets;

import com.yaroslavlancelot.eafall.game.batching.BatchingKeys;
import com.yaroslavlancelot.eafall.game.engine.CleanableSpriteGroup;
import com.yaroslavlancelot.eafall.game.entity.BatchedSprite;
import com.yaroslavlancelot.eafall.game.entity.gameobject.GameObject;
import com.yaroslavlancelot.eafall.game.entity.gameobject.equipment.damage.Damage;
import com.yaroslavlancelot.eafall.game.events.aperiodic.ingame.AttachSpriteEvent;

import org.andengine.entity.modifier.MoveModifier;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.modifier.IModifier;

import de.greenrobot.event.EventBus;

/**
 * Bullet
 */
public abstract class AbstractBullet extends BatchedSprite implements IModifier.IModifierListener {
    /** bullet damage value */
    protected Damage mDamage;
    protected GameObject mTarget;
    protected long mTargetId;
    /** bullet lifecycle handler */
    private MoveModifier mMoveModifier;
    private boolean mIsAttached;

    public AbstractBullet(int width, int height, ITextureRegion textureRegion, VertexBufferObjectManager vertexBufferObjectManager) {
        super(-100, -100, width, height, textureRegion, vertexBufferObjectManager);
        setSpriteGroupName(BatchingKeys.BULLET_AND_HEALTH);
        /* the maximum distance bullet can damage (it's usual 2 times distance than it's enemy) */
        mMoveModifier = new MoveModifier(0, 0, 0, 0, 0);
        mMoveModifier.setAutoUnregisterWhenFinished(false);
    }

    public Damage getDamage() {
        return mDamage;
    }

    @Override
    public void onModifierStarted(final IModifier pModifier, final Object pItem) {
    }

    protected abstract float duration(float distance);

    public void fire(Damage damage, float x, float y, GameObject gameObject) {
        setTag(0);
        mDamage = damage;
        mTarget = gameObject;
        mTargetId = gameObject.getObjectUniqueId();
        float distance = Math.max(Math.abs(x - gameObject.getX()), Math.abs(y - gameObject.getY()));
        mMoveModifier.reset(duration(distance), x, y, gameObject.getX(), gameObject.getY());
        if (!mIsAttached) {
            mMoveModifier.addModifierListener(AbstractBullet.this);
            EventBus.getDefault().post(new AttachSpriteEvent(this));
            mIsAttached = true;
            registerEntityModifier(mMoveModifier);
        }
    }

    protected void destroy() {
        setPosition(-100, -100);
        setTag(CleanableSpriteGroup.RECYCLE);
        onBulletDestroyed();
    }

    protected void onBulletDestroyed() {
    }
}
