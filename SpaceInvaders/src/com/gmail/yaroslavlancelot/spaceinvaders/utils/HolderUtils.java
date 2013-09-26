package com.gmail.yaroslavlancelot.spaceinvaders.utils;

import java.util.HashMap;
import java.util.Map;

public abstract class HolderUtils<T> {
    private Map<String, T> textureRegionMap = new HashMap<String, T>(15);

    protected HolderUtils() {
    }

    public T getElement(String id) {
        return textureRegionMap.get(id);
    }

    public void addElement(String id, T element) {
        textureRegionMap.put(id, element);
    }
}
