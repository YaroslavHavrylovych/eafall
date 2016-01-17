package com.yaroslavlancelot.eafall.android.dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.yaroslavlancelot.eafall.R;
import com.yaroslavlancelot.eafall.android.fragment.SettingsFragment;

/**
 * Dialog which contains settings fragment. Used to be shown as a dialog
 * in the game.
 *
 * @author Yaroslav Havrylovych
 */
public class SettingsDialog extends DialogFragment {
    // ===========================================================
    // Constants
    // ===========================================================
    /** Dialog key */
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
    @NonNull
    public Dialog onCreateDialog(final Bundle savedInstanceState) {
        Dialog dialog = new Dialog(getActivity(), R.style.Theme_NoTitleBar_Fullscreen_BlackBackground);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.holder_layout, container, true);
        initSettingsFragment();
        return view;
    }

    /**
     * Used in {@link SettingsDialog#onCreateView(LayoutInflater, ViewGroup, Bundle)}
     * to initialize the content.
     */
    private void initSettingsFragment() {
        SettingsFragment fragment = new SettingsFragment();
        fragment.addBackButtonOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                SettingsDialog.this.dismiss();
            }
        });
        getChildFragmentManager().beginTransaction()
                .replace(R.id.content, fragment)
                .commit();
    }

    // ===========================================================
    // Inner and Anonymous Classes
    // ===========================================================
}
