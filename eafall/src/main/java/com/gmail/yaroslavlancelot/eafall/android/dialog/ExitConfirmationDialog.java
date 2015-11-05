package com.gmail.yaroslavlancelot.eafall.android.dialog;

import android.app.Dialog;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.gmail.yaroslavlancelot.eafall.R;
import com.gmail.yaroslavlancelot.eafall.game.EaFallActivity;

/**
 * @author Yaroslav Havrylovych
 */
public class ExitConfirmationDialog extends Dialog {
    // ===========================================================
    // Constants
    // ===========================================================

    // ===========================================================
    // Fields
    // ===========================================================
    private final EaFallActivity mEaFallActivity;

    // ===========================================================
    // Constructors
    // ===========================================================
    public ExitConfirmationDialog(final EaFallActivity activity) {
        super(activity);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        mEaFallActivity = activity;
        setContentView(R.layout.exit_alert_dialog_layout);
        Button button = (Button) findViewById(R.id.yes);
        initYesButton(button);
        button = (Button) findViewById(R.id.no);
        initNoButton(button);
        setCancelable(true);
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
    private void initYesButton(Button button) {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                dismiss();
                mEaFallActivity.finish();
            }
        });
    }

    private void initNoButton(final Button button) {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                dismiss();
            }
        });
    }

    // ===========================================================
    // Inner and Anonymous Classes
    // ===========================================================
}
