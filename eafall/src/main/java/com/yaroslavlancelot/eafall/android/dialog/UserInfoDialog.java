package com.yaroslavlancelot.eafall.android.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.yaroslavlancelot.eafall.EaFallApplication;
import com.yaroslavlancelot.eafall.R;
import com.yaroslavlancelot.eafall.android.analytics.AnalyticsFactory;
import com.yaroslavlancelot.eafall.android.analytics.UserConsent;

/** Dialog which appear when the app starts to inform the user about Privacy Policy */
public class UserInfoDialog extends Dialog {

    // ===========================================================
    // Constants
    // ===========================================================

    // ===========================================================
    // Fields
    // ===========================================================

    // ===========================================================
    // Constructors
    // ===========================================================
    public UserInfoDialog(final Context context) {
        super(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setCanceledOnTouchOutside(false);
        setContentView(R.layout.dialog_user_agreement);
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
        getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));
        TextView textView = findViewById(R.id.consent_text);
        textView.setOnClickListener((view) -> {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW,
                    Uri.parse(EaFallApplication.getContext().getString(R.string.user_agreement_url)));
            getContext().startActivity(browserIntent);
        });
        Button doneButton = findViewById(R.id.done_button);
        doneButton.setOnClickListener(v -> {
            AnalyticsFactory.getInstance().setStatsState(true);
            UserConsent.getInstance().userConsentAgreed();
            dismiss();
        });
    }

    // ===========================================================
    // Methods
    // ===========================================================

    // ===========================================================
    // Inner and Anonymous Classes
    // ===========================================================
}
