package com.gmail.yaroslavlancelot.eafall.game.batching;

import com.gmail.yaroslavlancelot.eafall.general.Holder;

import org.andengine.entity.sprite.batch.SpriteGroup;

/** Holds {@link SpriteGroup}'s */
public class SpriteGroupHolder extends Holder<SpriteGroup> {
    /** current class instance (singleton implementation) */
    private final static SpriteGroupHolder sInstance = new SpriteGroupHolder();

    private SpriteGroupHolder() {
    }

    /**
     * Search and return {@link SpriteGroup}
     *
     * @param key {@link SpriteGroup} key in the collection
     * @return stored {@link SpriteGroup} or null if no such group stored
     */
    public static SpriteGroup getGroup(String key) {
        return getsInstance().getElement(key);
    }

    public static SpriteGroupHolder getsInstance() {
        return sInstance;
    }

    public static SpriteGroup addGroup(String key, SpriteGroup group) {
        return getsInstance().addElement(key, group);
    }
}
