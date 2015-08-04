package com.gmail.yaroslavlancelot.eafall.general;

import java.util.ArrayList;
import java.util.List;

/**
 * Used to clear memory if you're not in the game. Method clear() called each time
 * in onResume() of any out of the NonInGame Activities
 */
public abstract class SelfCleanable {
    /** hold current class instance to simplify it's clearing */
    private static final List<SelfCleanable> sInstanceHolder = new ArrayList<>(10);

    protected SelfCleanable() {
        synchronized (sInstanceHolder) {
            sInstanceHolder.add(this);
        }
    }

    /** clear all static holders */
    public static void clearMemory() {
        synchronized (sInstanceHolder) {
            for (SelfCleanable holder : sInstanceHolder) {
                holder.clear();
            }
        }
    }

    /**
     * Will be called when user starting or leave the game.
     * Override it to remove static objects or not gc-able objects
     */
    public abstract void clear();
}
