package com.gmail.yaroslavlancelot.eafall.game.events;

import com.gmail.yaroslavlancelot.eafall.general.SelfCleanable;

import java.util.ArrayList;
import java.util.List;

//TODO maybe use it only for player data (units, oxygen etc) and rename and move it

/**
 * Contains callback (which added and removed manually) which triggered by key from
 * {@link SharedEvents#valueChanged(String, Object)}. So if you subscribe to
 * get notification about some data changes (by given key) then each call of
 * {@link SharedEvents#valueChanged(String, Object)} method with your key
 * will trigger the callback.
 *
 * @author Yaroslav Havrylovych
 */
public class SharedEvents extends SelfCleanable {
    private static final SharedEvents SHARED_DATA_CALLBACKS = new SharedEvents();
    private final List<DataChangedCallback> mCallbacks = new ArrayList<DataChangedCallback>(5);

    public static SharedEvents getInstance() {
        return SHARED_DATA_CALLBACKS;
    }

    @Override
    public void clear() {
        removeCallbacks();
    }

    public static boolean addCallback(DataChangedCallback callback) {
        return getInstance().mCallbacks.add(callback);
    }

    public synchronized static void valueChanged(String key, Object value) {
        SharedEvents sharedEvents = getInstance();
        synchronized (sharedEvents.mCallbacks) {
            for (DataChangedCallback callback : sharedEvents.mCallbacks) {
                if (callback.mKey.equals(key)) {
                    callback.callback(key, value);
                }
            }
        }
    }

    public synchronized static void removeCallbacks() {
        synchronized (getInstance().mCallbacks) {
            getInstance().mCallbacks.clear();
        }
    }

    public synchronized static boolean removeCallback(DataChangedCallback callback) {
        return getInstance().mCallbacks.remove(callback);
    }

    public abstract static class DataChangedCallback {
        private final String mKey;

        public DataChangedCallback(String key) {
            mKey = key;
        }

        public abstract void callback(String key, Object value);
    }
}
