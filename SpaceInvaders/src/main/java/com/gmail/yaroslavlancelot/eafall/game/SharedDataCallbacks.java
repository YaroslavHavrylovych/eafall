package com.gmail.yaroslavlancelot.eafall.game;

import java.util.ArrayList;
import java.util.List;

//TODO maybe use it only for player data (units, oxygen etc) and rename and move it
/**
 * Contains callback (which added and removed manually) which triggered by key from
 * {@link SharedDataCallbacks#valueChanged(String, Object)}. So if you subscribe to
 * get notification about some data changes (by given key) then each call of
 * {@link SharedDataCallbacks#valueChanged(String, Object)} method with your key
 * will trigger the callback.
 *
 * @author Yaroslav Havrylovych
 */
public class SharedDataCallbacks {
    private static final SharedDataCallbacks sInstance = new SharedDataCallbacks();
    private final List<DataChangedCallback> mCallbacks = new ArrayList<DataChangedCallback>(5);

    public static boolean addCallback(DataChangedCallback callback) {
        return getInstance().mCallbacks.add(callback);
    }

    public static SharedDataCallbacks getInstance() {
        return sInstance;
    }

    public synchronized static void valueChanged(String key, Object value) {
        SharedDataCallbacks sharedDataCallbacks = getInstance();
        synchronized (sharedDataCallbacks.mCallbacks) {
            for (DataChangedCallback callback : sharedDataCallbacks.mCallbacks) {
                if (callback.mKey.equals(key)) {
                    callback.callback(key, value);
                }
            }
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
