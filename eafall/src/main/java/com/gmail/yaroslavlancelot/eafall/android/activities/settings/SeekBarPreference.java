package com.gmail.yaroslavlancelot.eafall.android.activities.settings;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.gmail.yaroslavlancelot.eafall.R;

public class SeekBarPreference extends Preference implements SeekBar.OnSeekBarChangeListener{

    private static final String ANDROIDNS = "http://schemas.android.com/apk/res/android";

    private TextView mValue;
    private int mCurrentValue;
    private int mMax;

    public SeekBarPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        mMax = attrs.getAttributeIntValue(ANDROIDNS, "max", 99);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        mCurrentValue = preferences.getInt(getKey(), attrs.getAttributeIntValue(ANDROIDNS, "defaultValue", 0));
    }

    @Override
    protected View onCreateView(ViewGroup parent) {
        super.onCreateView(parent);

        LayoutInflater layoutInflater = LayoutInflater.from(getContext());
        RelativeLayout layout = (RelativeLayout) layoutInflater.inflate(R.layout.seek_bar_preference_layout, null);

        ((TextView) layout.findViewById(R.id.title)).setText(getTitle());

        SeekBar bar = (SeekBar) layout.findViewById(R.id.seekBar);
        bar.setMax(mMax);
        bar.setProgress(mCurrentValue);
        bar.setOnSeekBarChangeListener(this);

        mValue = (TextView) layout.findViewById(R.id.value);
        mValue.setText(mCurrentValue+"");

        return layout;
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        mValue.setText(progress+"");
        mValue.invalidate();
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        mCurrentValue = seekBar.getProgress();
        updatePreference(mCurrentValue);
        notifyChanged();
    }

    private void updatePreference(int newValue) {
        SharedPreferences.Editor editor = getEditor();
        editor.putInt(getKey(), newValue);
        editor.commit();
    }
}
