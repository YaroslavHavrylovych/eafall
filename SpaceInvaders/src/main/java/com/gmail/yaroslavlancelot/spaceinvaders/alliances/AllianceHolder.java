package com.gmail.yaroslavlancelot.spaceinvaders.alliances;

import com.gmail.yaroslavlancelot.spaceinvaders.utils.HolderUtils;

/** you can get race instance from here */
public class AllianceHolder extends HolderUtils<IAlliance> {
    /** current class instance (singleton implementation) */
    private final static AllianceHolder sInstance = new AllianceHolder();

    private AllianceHolder() {
    }

    public static IAlliance getRace(String raceName) {
        return getInstance().getElement(raceName);
    }

    public static AllianceHolder getInstance() {
        return sInstance;
    }
}
