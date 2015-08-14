package com.gmail.yaroslavlancelot.eafall.android.activities;

import android.app.Activity;

import com.gmail.yaroslavlancelot.eafall.general.Holder;

/** base/parent (general actions) for all non in-game activities */
public abstract class BaseNonGameActivity extends Activity {
    @Override
    protected void onResume() {
        super.onResume();
        Holder.clearMemory();
    }
}
