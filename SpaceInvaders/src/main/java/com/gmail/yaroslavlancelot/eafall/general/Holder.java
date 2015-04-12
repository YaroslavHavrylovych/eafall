package com.gmail.yaroslavlancelot.eafall.general;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/** {@link java.util.Map} wrapper. Simplified it's usage */
public abstract class Holder<T> extends SelfCleanable {
    private Map<String, T> holderMap = new HashMap<String, T>(15);

    @Override
    public void clear() {
        if (holderMap != null)
            holderMap.clear();
    }

    /** get object from internal map */
    public T getElement(String id) {
        if (holderMap == null) return null;
        return holderMap.get(id);
    }

    public Collection<T> getElements() {
        if (holderMap == null) return null;
        return holderMap.values();
    }

    /** put object into internal map */
    public void addElement(String id, T element) {
        holderMap.put(id, element);
    }

    /**
     * check is element has been already added
     *
     * @param id element id
     * @return true - if element holdered (already added) and false in other way
     */
    public boolean isElementExist(String id) {
        return holderMap.get(id) != null;
    }
}