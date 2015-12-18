package com.yaroslavlancelot.eafall.android.activities;

import android.support.v4.app.FragmentActivity;

import com.yaroslavlancelot.eafall.general.Holder;

/** base/parent (general actions) for all non in-game activities */
public abstract class BaseNonGameActivity extends FragmentActivity {
    @Override
    protected void onResume() {
        super.onResume();
        Holder.clearMemory();
    }
}
