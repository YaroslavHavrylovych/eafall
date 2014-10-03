package com.gmail.yaroslavlancelot.spaceinvaders.activities;

import android.app.Activity;

import com.gmail.yaroslavlancelot.spaceinvaders.utils.HolderUtils;

/** general actions for all non in-game activities */
public class BaseNonGameActivity extends Activity {
    @Override
    protected void onResume() {
        super.onResume();
        HolderUtils.clearMemory();
    }
}
