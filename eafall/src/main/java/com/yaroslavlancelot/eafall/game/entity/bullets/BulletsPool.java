package com.yaroslavlancelot.eafall.game.entity.bullets;

import com.yaroslavlancelot.eafall.android.LoggerHelper;
import com.yaroslavlancelot.eafall.game.constant.StringConstants;
import com.yaroslavlancelot.eafall.game.entity.AfterInitializationPool;
import com.yaroslavlancelot.eafall.game.entity.TextureRegionHolder;
import com.yaroslavlancelot.eafall.game.entity.gameobject.equipment.damage.Damage;

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

    /**
     * create (or use existing one) bullet
     *
     * @param damageType bullet damage type
     * @param playerName player which want to throw the bullet name
     * @return bullet which you can use
     */
    public static AbstractBullet getBullet(Damage.DamageType damageType, String playerName) {
        Pool pool = BULLETS_POOL.mPools.get(damageType);
        AbstractBullet bullet = pool.obtainPoolItem();
        if (damageType.equals(Damage.DamageType.QUAKER)) {
            ((QuakerBullet) bullet).setPlayerName(playerName);
        }
        return bullet;
    }

    public static void init(VertexBufferObjectManager objectManager) {
        BULLETS_POOL = new BulletsPool(objectManager);
    }

    private class Pool extends AfterInitializationPool<AbstractBullet> {
        private final String mBulletFile;
        private final int mWidth;
        private final int mHeight;

        private Pool(Damage.DamageType damageType) {
            switch (damageType) {
                case ANNIHILATOR:
                    mBulletFile = StringConstants.FILE_ANNIHILATOR_BULLET;
                    mHeight = mWidth = 3;
                    break;
                case HIGGSON:
                    mBulletFile = StringConstants.FILE_HIGGSON_BULLET;
                    mHeight = mWidth = 3;
                    break;
                case LASER:
                    mBulletFile = StringConstants.FILE_LASER_BULLET;
                    mWidth = 2;
                    mHeight = 4;
                    break;
                case NEUTRINO:
                    mBulletFile = StringConstants.FILE_NEUTRINO_BULLET;
                    mWidth = 2;
                    mHeight = 4;
                    break;
                case QUAKER:
                    mBulletFile = StringConstants.FILE_QUAKER_BULLET;
                    mWidth = 4;
                    mHeight = 4;
                    break;
                case RAILGUN:
                    mHeight = mWidth = 3;
                    mBulletFile = StringConstants.FILE_RAILGUN_BULLET;
                    break;
                default:
                    throw new IllegalArgumentException("unknown bullet type");
            }
            initPool(5, 5);
        }

        @Override
        protected AbstractBullet allocatePoolItem() {
            LoggerHelper.printVerboseMessage(TAG, "New bullet allocated. Available items count=" + getAvailableItems());
            AbstractBullet bullet;
            if (mBulletFile.equals(StringConstants.FILE_QUAKER_BULLET)) {
                bullet = new QuakerBullet(mWidth, mHeight, TextureRegionHolder.getRegion(mBulletFile), mObjectManager) {
                    @Override
                    protected void onBulletDestroyed() {
                        super.onBulletDestroyed();
                        LoggerHelper.printVerboseMessage(TAG, "Bullet recycled. Available items count=" + getAvailableItems());
                        recycle(this);
                    }
                };
            } else {
                bullet = new SingleBullet(mWidth, mHeight, TextureRegionHolder.getRegion(mBulletFile), mObjectManager) {
                    @Override
                    protected void onBulletDestroyed() {
                        super.onBulletDestroyed();
                        LoggerHelper.printVerboseMessage(TAG, "Bullet recycled. Available items count=" + getAvailableItems());
                        recycle(this);
                    }
                };
            }
            return bullet;
        }
    }
}
