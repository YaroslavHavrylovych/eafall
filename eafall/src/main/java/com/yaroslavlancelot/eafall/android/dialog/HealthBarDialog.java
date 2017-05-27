package com.yaroslavlancelot.eafall.android.dialog;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.yaroslavlancelot.eafall.R;
import com.yaroslavlancelot.eafall.android.fragment.DialogHealthBarFragment;
import com.yaroslavlancelot.eafall.game.configuration.game.ApplicationSettings;

/**
 * Health bar behaviour pick dialog fragment
 *
 * @author Yaroslav Havrylovych
 */
public class HealthBarDialog extends DialogFragment {
    // ===========================================================
    // Constants
    // ===========================================================
    /** Dialog key */
    public static final String KEY = "health_bar_dialog";

    // ===========================================================
    // Fields
    // ===========================================================
    /** Health bar unit behaviour from the settings */
    private ApplicationSettings.UnitHealthBarBehavior mCurrentBehavior;
    /** Triggered after health bar behaviour was picked */
    private HealthBarTypeSet mCallback;

    // ===========================================================
    // Constructors
    // ===========================================================

    // ===========================================================
    // Getter & Setter
    // ===========================================================

    // ===========================================================
    // Methods for/from SuperClass/Interfaces
    // ===========================================================
    @Override
    @NonNull
    public Dialog onCreateDialog(final Bundle savedInstanceState) {
        Dialog dialog = new Dialog(getActivity());
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));
        return dialog;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.holder_layout, container, true);
        initSettingsFragment();
        return view;
    }

    // ===========================================================
    // Methods
    // ===========================================================
    public void init(HealthBarTypeSet callback, ApplicationSettings.UnitHealthBarBehavior behavior) {
        mCallback = callback;
        mCurrentBehavior = behavior;
    }

    /**
     * Used in {@link HealthBarDialog#onCreateView(LayoutInflater, ViewGroup, Bundle)}
     * to initialize callbacks and content.
     */
    private void initSettingsFragment() {
        @SuppressLint("ValidFragment")
        DialogHealthBarFragment dialogFragment = new DialogHealthBarFragment() {
            @Override
            public void onClose() {
                HealthBarDialog.this.dismiss();
            }
        };
        dialogFragment.setSelectedIdByType(mCurrentBehavior);
        dialogFragment.setCallback(mCallback);
        getChildFragmentManager().beginTransaction()
                .add(R.id.content, dialogFragment)
                .commit();
    }

    // ===========================================================
    // Inner and Anonymous Classes
    // ===========================================================

    /** trigger for health bar behaviour set */
    public interface HealthBarTypeSet {
        /** triggers when user pick health bar behaviour (can be the same as the old one) */
        void onTypeSet(ApplicationSettings.UnitHealthBarBehavior healthBarBehavior);
    }

    /** dialog close c */
    public interface OnClose {
        /** triggers when user want to close the dialog */
        void onClose();
    }
}
