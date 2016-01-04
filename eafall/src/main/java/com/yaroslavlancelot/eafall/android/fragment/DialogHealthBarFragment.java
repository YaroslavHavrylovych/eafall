package com.yaroslavlancelot.eafall.android.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.yaroslavlancelot.eafall.R;
import com.yaroslavlancelot.eafall.android.activities.StartupActivity;
import com.yaroslavlancelot.eafall.android.dialog.HealthBarDialog;
import com.yaroslavlancelot.eafall.game.configuration.game.ApplicationSettings;

/** Health bar picker fragment */
@SuppressLint("ValidFragment")
public abstract class DialogHealthBarFragment extends Fragment implements HealthBarDialog.OnClose {
    private HealthBarDialog.HealthBarTypeSet mCallback;
    private int mSelectedId = R.id.health_bar_default;

    public void setCallback(HealthBarDialog.HealthBarTypeSet callback) {
        mCallback = callback;
    }

    public void setSelectedIdByType(ApplicationSettings.UnitHealthBarBehavior type) {
        switch (type) {
            case ALWAYS_HIDDEN: {
                mSelectedId = R.id.health_bar_invisible;
                break;
            }
            case ALWAYS_VISIBLE: {
                mSelectedId = R.id.health_bar_visible;
                break;
            }
            default: {
                mSelectedId = R.id.health_bar_default;
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_health_bar, container, false);
        //title
        TextView textView = (TextView) view.findViewById(R.id.title_text);
        textView.getPaint().setShader(StartupActivity.getTextGradient(textView.getLineHeight()));
        //select one
        RadioButton radioButton = (RadioButton) view.findViewById(mSelectedId);
        radioButton.setChecked(true);
        //variants
        RadioGroup radioGroup = (RadioGroup) view.findViewById(R.id.health_bar_behaviour);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(final RadioGroup group, final int checkedId) {
                mSelectedId = checkedId;
            }
        });
        //done button
        Button doneButton = (Button) view.findViewById(R.id.done_button);
        doneButton.getPaint().setShader(StartupActivity.getTextGradient(doneButton.getLineHeight()));
        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                switch (mSelectedId) {
                    case R.id.health_bar_invisible: {
                        mCallback.onTypeSet(ApplicationSettings.UnitHealthBarBehavior.ALWAYS_HIDDEN);
                        break;
                    }
                    case R.id.health_bar_visible: {
                        mCallback.onTypeSet(ApplicationSettings.UnitHealthBarBehavior.ALWAYS_VISIBLE);
                        break;
                    }
                    default: {
                        mCallback.onTypeSet(ApplicationSettings.UnitHealthBarBehavior.DEFAULT);
                    }
                }
                onClose();
            }
        });
        return view;
    }
}
