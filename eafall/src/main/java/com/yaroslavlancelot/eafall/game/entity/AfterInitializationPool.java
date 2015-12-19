package com.yaroslavlancelot.eafall.game.entity;

import org.andengine.util.adt.pool.GenericPool;

public abstract class AfterInitializationPool<T> {
    protected Pool mPool;

    protected AfterInitializationPool() {
    }

    public int getAvailableItems() {
        if (mPool == null) {
            return 0;
        }
        return mPool.getAvailableItemCount();
    }

    protected void initPool(int initialSize, int growth) {
        mPool = new Pool(initialSize, growth);
    }

    protected abstract T allocatePoolItem();

    public T obtainPoolItem() {
        return mPool.obtainPoolItem();
    }

    public void recycle(T item) {
        mPool.recyclePoolItem(item);
    }

    private class Pool extends GenericPool<T> {
        private Pool(int initialSize, int growth) {
            super(initialSize, growth);
        }

        @Override
        protected T onAllocatePoolItem() {
            return allocatePoolItem();
        }
    }
}
