package com.gmail.yaroslavlancelot.eafall.game.entity.bullets;

import com.gmail.yaroslavlancelot.eafall.android.LoggerHelper;
import com.gmail.yaroslavlancelot.eafall.game.constant.StringConstants;
import com.gmail.yaroslavlancelot.eafall.game.entity.AfterInitializationPool;
import com.gmail.yaroslavlancelot.eafall.game.entity.TextureRegionHolder;

import org.andengine.opengl.vbo.VertexBufferObjectManager;

/** Bullets pool. Used to increase fps with a lot of new bullets creation */
public class BulletPool extends AfterInitializationPool<Bullet> {
    public static final String TAG = BulletPool.class.getCanonicalName();
    private volatile static BulletPool sBulletPool;
    private VertexBufferObjectManager mObjectManager;

    private BulletPool(VertexBufferObjectManager objectManager) {
        mObjectManager = objectManager;
        initPool(10, 10);
    }

    public static void init(VertexBufferObjectManager objectManager) {
        sBulletPool = new BulletPool(objectManager);
    }

    public static BulletPool getInstance() {
        return sBulletPool;
    }

    @Override
    protected Bullet allocatePoolItem() {
        LoggerHelper.printVerboseMessage(TAG, "New bullet allocated. Available items count=" + getAvailableItems());
        return new Bullet(TextureRegionHolder.getRegion(StringConstants.FILE_BULLET),
                mObjectManager) {
            @Override
            protected void onBulletDestroyed() {
                super.onBulletDestroyed();
                LoggerHelper.printVerboseMessage(TAG, "Bullet recycled. Available items count=" + getAvailableItems());
                recycle(this);
            }
        };
    }
}
