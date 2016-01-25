package com.yaroslavlancelot.eafall.android.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.ViewGroup;

import com.yaroslavlancelot.eafall.EaFallApplication;
import com.yaroslavlancelot.eafall.R;
import com.yaroslavlancelot.eafall.game.configuration.IConfig;
import com.yaroslavlancelot.eafall.general.Holder;

/** base/parent (general actions) for all non in-game activities */
public abstract class BaseNonGameActivity extends FragmentActivity {

    /**
     * All activities are cut to 16x9 aspect ratio. To implement this we've
     * created we customized {@link BaseNonGameActivity#onCreate(Bundle)} and now you
     * must use {@link BaseNonGameActivity#onCustomCreate(Bundle)} instead.
     */
    @Override
    @Deprecated
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        onCustomCreate(savedInstanceState);
        truncateView(findViewById(R.id.background_layout));
    }

    @Override
    protected void onResume() {
        super.onResume();
        Holder.clearMemory();
    }

    /**
     * Used instead of {@link BaseNonGameActivity#onCreate(Bundle)}.
     * <p/>
     * YOU MUST set content view in this method and this view have to has
     * id {@link R.id#background_layout} as an background element.
     * It will be used to set aspect ration for the activity.
     *
     * @param savedInstanceState came from onCreate
     */
    protected abstract void onCustomCreate(Bundle savedInstanceState);

    /**
     * Set's width of the passed view to screen width and set's height proportionally to width:
     * width/height=16/9.
     * <p/>
     * Screen width - the longest side of the screen.
     *
     * @param view passed view
     */
    public static void truncateView(View view) {
        IConfig config = EaFallApplication.getConfig();
        int baseViewWidth = config.getDisplayWidth();
        int baseViewHeight = baseViewWidth / 16 * 9;
        ViewGroup.LayoutParams params = view.getLayoutParams();
        params.height = baseViewHeight;
        params.width = baseViewWidth;
        view.setLayoutParams(params);
    }
}
