package com.gmail.yaroslavlancelot.spaceinvaders.activities;

import android.app.Activity;

import com.gmail.yaroslavlancelot.spaceinvaders.utils.HolderUtils;

/** base/parent (general actions) for all non in-game activities */
public abstract class BaseNonGameActivity extends Activity {
    @Override
    protected void onResume() {
        super.onResume();
        HolderUtils.clearMemory();
    }
}
