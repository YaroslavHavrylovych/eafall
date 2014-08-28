package com.gmail.yaroslavlancelot.spaceinvaders.races;

import com.gmail.yaroslavlancelot.spaceinvaders.utils.HolderUtils;

/** you can get race instance from here */
public class RaceHolder extends HolderUtils<IRace> {
    /** current class instance (singleton realization) */
    private final static RaceHolder sInstance = new RaceHolder();

    private RaceHolder() {
    }

    public static RaceHolder getInstance() {
        return sInstance;
    }
}
