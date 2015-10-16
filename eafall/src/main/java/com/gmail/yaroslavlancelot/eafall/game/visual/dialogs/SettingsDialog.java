package com.gmail.yaroslavlancelot.eafall.game.visual.dialogs;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.gmail.yaroslavlancelot.eafall.R;
import com.gmail.yaroslavlancelot.eafall.general.settings.SettingsFragment;

/**
 * Fragment which contains settings fragment. Used to be shown as a dialog
 * in the game.
 *
 * @author Yaroslav Havrylovych
 */
public class SettingsDialog extends DialogFragment {
    // ===========================================================
    // Constants
    // ===========================================================
    public static final String KEY = "settings_dialog";

    // ===========================================================
    // Fields
    // ===========================================================

    // ===========================================================
    // Constructors
    // ===========================================================

    // ===========================================================
    // Getter & Setter
    // ===========================================================

    // ===========================================================
    // Methods for/from SuperClass/Interfaces
    // ===========================================================

    // ===========================================================
    // Methods
    // ===========================================================
    @Override
    public Dialog onCreateDialog(final Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.settings_layout, container, true);
        initSettingsFragment();
        initBackButton(view.findViewById(R.id.back));
        return view;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    private void initSettingsFragment() {
        getChildFragmentManager().beginTransaction()
                .replace(R.id.content, new SettingsFragment())
                .commit();
    }

    private void initBackButton(View backButton) {
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SettingsDialog.this.dismiss();
            }
        });
    }

    // ===========================================================
    // Inner and Anonymous Classes
    // ===========================================================
}
