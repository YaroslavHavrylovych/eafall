package com.yaroslavlancelot.eafall.android;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

/**
 * Common methods for my intents
 *
 * @author Yaroslav Havrylovych
 */
public class StartableIntent extends Intent {
    // ===========================================================
    // Constants
    // ===========================================================

    // ===========================================================
    // Fields
    // ===========================================================

    // ===========================================================
    // Constructors
    // ===========================================================
    public StartableIntent() {
    }

    public StartableIntent(final Intent o) {
        super(o);
    }

    public StartableIntent(final String action) {
        super(action);
    }

    public StartableIntent(final String action, final Uri uri) {
        super(action, uri);
    }

    public StartableIntent(final Context packageContext, final Class<?> cls) {
        super(packageContext, cls);
    }

    public StartableIntent(final String action, final Uri uri, final Context packageContext, final Class<?> cls) {
        super(action, uri, packageContext, cls);
    }


    // ===========================================================
    // Getter & Setter
    // ===========================================================

    // ===========================================================
    // Methods for/from SuperClass/Interfaces
    // ===========================================================

    // ===========================================================
    // Methods
    // ===========================================================
    public void start(Activity activity) {
        activity.startActivity(this);
    }

    // ===========================================================
    // Inner and Anonymous Classes
    // ===========================================================
}
