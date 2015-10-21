package com.gmail.yaroslavlancelot.eafall.game.entity.bullets;

import com.gmail.yaroslavlancelot.eafall.android.LoggerHelper;
import com.gmail.yaroslavlancelot.eafall.game.constant.StringConstants;
import com.gmail.yaroslavlancelot.eafall.game.engine.InstantRotationModifier;
import com.gmail.yaroslavlancelot.eafall.game.entity.AfterInitializationPool;
import com.gmail.yaroslavlancelot.eafall.game.entity.TextureRegionHolder;
import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.equipment.damage.Damage;

import org.andengine.entity.modifier.IEntityModifier;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import java.util.HashMap;
import java.util.Map;

/** Bullets pool. Used to increase fps with a lot of new bullets creation */
public class BulletsPool {
    public static final String TAG = BulletsPool.class.getCanonicalName();
    private volatile static BulletsPool BULLETS_POOL;
    private VertexBufferObjectManager mObjectManager;
    private Map<Damage.DamageType, Pool> mPools = new HashMap<>(Damage.DamageType.values().length);

    private BulletsPool(VertexBufferObjectManager objectManager) {
        mObjectManager = objectManager;
        for (Damage.DamageType damageType : Damage.DamageType.values()) {
            mPools.put(damageType, new Pool(damageType));
        }
    }

    public static AfterInitializationPool<Bullet> getInstance(Damage.DamageType damageType) {
        return BULLETS_POOL.mPools.get(damageType);
    }

    public static void init(VertexBufferObjectManager objectManager) {
        BULLETS_POOL = new BulletsPool(objectManager);
    }

    private class Pool extends AfterInitializationPool<Bullet> {
        private final String mBulletFile;
        private final int mWidth;
        private final int mHeight;
        private final IEntityModifier mAdditionalModifier;

        private Pool(Damage.DamageType damageType) {
            switch (damageType) {
                case ANNIHILATOR:
                    mBulletFile = StringConstants.FILE_ANNIHILATOR_BULLET;
                    mHeight = mWidth = 3;
                    mAdditionalModifier = null;
                    break;
                case HIGGSON:
                    mBulletFile = StringConstants.FILE_HIGGSON_BULLET;
                    mHeight = mWidth = 3;
                    mAdditionalModifier = null;
                    break;
                case LASER:
                    mBulletFile = StringConstants.FILE_LASER_BULLET;
                    mWidth = 2;
                    mHeight = 4;
                    mAdditionalModifier = null;
                    break;
                case NEUTRINO:
                    mBulletFile = StringConstants.FILE_NEUTRINO_BULLET;
                    mWidth = 2;
                    mHeight = 4;
                    mAdditionalModifier = null;
                    break;
                case QUAKER:
                    mBulletFile = StringConstants.FILE_QUAKER_BULLET;
                    mWidth = 4;
                    mHeight = 4;
                    mAdditionalModifier = new InstantRotationModifier(2);
                    break;
                case RAILGUN:
                    mHeight = mWidth = 3;
                    mBulletFile = StringConstants.FILE_RAILGUN_BULLET;
                    mAdditionalModifier = null;
                    break;
                default:
                    throw new IllegalArgumentException("unknown bullet type");
            }
            initPool(5, 5);
        }

        @Override
        protected Bullet allocatePoolItem() {
            LoggerHelper.printVerboseMessage(TAG, "New bullet allocated. Available items count=" + getAvailableItems());
            Bullet bullet = new Bullet(mWidth, mHeight, TextureRegionHolder.getRegion(mBulletFile), mObjectManager) {
                @Override
                protected void onBulletDestroyed() {
                    super.onBulletDestroyed();
                    LoggerHelper.printVerboseMessage(TAG, "Bullet recycled. Available items count=" + getAvailableItems());
                    recycle(this);
                }
            };
            if (mAdditionalModifier != null) {
                bullet.registerEntityModifier(mAdditionalModifier);
            }
            return bullet;
        }
    }
}
