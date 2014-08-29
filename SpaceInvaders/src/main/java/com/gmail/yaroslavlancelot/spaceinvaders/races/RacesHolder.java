package com.gmail.yaroslavlancelot.spaceinvaders.races;

import com.gmail.yaroslavlancelot.spaceinvaders.utils.HolderUtils;

/** you can get race instance from here */
public class RacesHolder extends HolderUtils<IRace> {
    /** current class instance (singleton realization) */
    private final static RacesHolder sInstance = new RacesHolder();

    private RacesHolder() {
    }

    public static RacesHolder getInstance() {
        return sInstance;
    }
}
