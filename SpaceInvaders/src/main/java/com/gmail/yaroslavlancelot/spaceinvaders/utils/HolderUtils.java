package com.gmail.yaroslavlancelot.spaceinvaders.utils;

import java.util.HashMap;
import java.util.Map;

/** {@link java.util.Map} wrapper. Simplified it's usage */
public abstract class HolderUtils<T> {
    private Map<String, T> textureRegionMap = new HashMap<String, T>(15);

    protected HolderUtils() {
    }

    /** get object from internal map */
    public T getElement(String id) {
        return textureRegionMap.get(id);
    }

    /** put object into internal map */
    public void addElement(String id, T element) {
        textureRegionMap.put(id, element);
    }
}
