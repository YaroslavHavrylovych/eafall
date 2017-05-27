package com.yaroslavlancelot.eafall.game.campaign.plot;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.yaroslavlancelot.eafall.R;

/**
 * Used to present plot message
 *
 * @author Yaroslav Havrylovych
 */
class PlotTellerDialog extends Dialog {
    // ===========================================================
    // Constants
    // ===========================================================

    // ===========================================================
    // Fields
    // ===========================================================
    private final Button mButton;
    private final TextView mTextView;

    // ===========================================================
    // Constructors
    // ===========================================================
    PlotTellerDialog(Activity activity) {
        super(activity);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.plot_teller_dialog);
        mButton = (Button) findViewById(R.id.confirm);
        mTextView = (TextView) findViewById(R.id.plot_text);
        setCancelable(true);
    }

    // ===========================================================
    // Getter & Setter
    // ===========================================================

    // ===========================================================
    // Methods for/from SuperClass/Interfaces
    // ===========================================================

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window window = getWindow();
        if (window != null) {
            window.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        }
    }

    // ===========================================================
    // Methods
    // ===========================================================
    void setOnNextCallback(View.OnClickListener onClickListener) {
        mButton.setOnClickListener(onClickListener);
    }

    void setNextButtonText(String text) {
        mButton.setText(text);
    }

    public void setText(String text) {
        mTextView.setText(text);
    }

    // ===========================================================
    // Inner and Anonymous Classes
    // ===========================================================
}
