package com.yaroslavlancelot.eafall.android.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.yaroslavlancelot.eafall.EaFallApplication;
import com.yaroslavlancelot.eafall.R;
import com.yaroslavlancelot.eafall.android.analytics.AnalyticsFactory;

public class UserInfoSettingsFragment extends Fragment {
    /*Fields*/
    /*Constants (or it has to be constants)*/
    /*Fields*/

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.user_info_settings_layout, container, false);
        view.findViewById(R.id.privacy_policy).setOnClickListener(this::openPrivacyPolicy);
        initStatsGatheringState(view.findViewById(R.id.send_data_checkbox),
                view.findViewById(R.id.data_send_description));
        return view;
    }

    /** developers mode check box initialization */
    private void initStatsGatheringState(CheckBox checkBox, final TextView description) {
        boolean isChecked = AnalyticsFactory.getInstance().isEnabled();
        checkBox.setChecked(isChecked);
        updateInfoTextView(isChecked,
                description);
        checkBox.setOnCheckedChangeListener((buttonView, analyticsEnabled) -> {
            updateInfoTextView(analyticsEnabled,
                    description);
            AnalyticsFactory.getInstance().setStatsState(analyticsEnabled);
        });
    }

    private void openPrivacyPolicy(View view) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW,
                Uri.parse(EaFallApplication.getContext().getString(R.string.user_agreement_url)));
        startActivity(browserIntent);
    }

    /**
     * update text view based on isTrueVal flag
     *
     * @param isTrueVal flag
     * @param textView  changeable text view
     */
    private void updateInfoTextView(boolean isTrueVal, TextView textView) {
        if (isTrueVal) {
            textView.setText(R.string.stats_gathering_enabled);
        } else {
            textView.setText(R.string.stats_gathering_disabled);
        }
        textView.invalidate();
    }
}
