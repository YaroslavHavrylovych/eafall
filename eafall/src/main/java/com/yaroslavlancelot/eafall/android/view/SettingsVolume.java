package com.yaroslavlancelot.eafall.android.view;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.preference.PreferenceManager;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yaroslavlancelot.eafall.EaFallApplication;
import com.yaroslavlancelot.eafall.R;
import com.yaroslavlancelot.eafall.android.analytics.AnalyticsFactory;

/**
 * Settings float element which have own seek-bar and a checkbox. The element can be disabled
 * with the checkbox or can set own value [0..1] (if enabled). Use default {@link SharedPreferences}
 * to store values.
 *
 * @author Yaroslav Havrylovych
 */
public class SettingsVolume extends LinearLayout {
    // ===========================================================
    // Constants
    // ===========================================================

    // ===========================================================
    // Fields
    // ===========================================================
    /** increase seek-bar value preferences */
    private SoundButton mIncreaseButton;
    /** decrease seek-bar value preferences */
    private SoundButton mDecreaseButton;
    /** seek-bar cover (used to show progress) */
    private PercentLight mLight;
    /** volume switcher (can disable this settings totally) */
    private CheckBox mVolumeCheckbox;
    /** current settings title */
    private TextView mTitle;
    /** used to display current setting status */
    private TextView mStatusDescription;
    /** share preferences instance */
    private SharedPreferences mSharedPreferences;
    /** {@link SharedPreferences} key used to store {@link SettingsVolume#mVolumeCheckbox} status */
    private String mVolumeEnabledPreferencesKey;
    /** status description text (used for enabled status) */
    private int mResEnabled;
    /** status description text (used for disabled status) */
    private int mResDisabled;
    /** volume background (middle part) */
    private View mVolumeBackground;
    /** area with inc/dec buttons and progress bar */
    private ColorFilter mDisableColorFilter =
            new PorterDuffColorFilter(Color.GRAY, PorterDuff.Mode.MULTIPLY);


    // ===========================================================
    // Constructors
    // ===========================================================
    public SettingsVolume(final Context context, final AttributeSet attrs) {
        super(context, attrs);
        _init_();
    }

    // ===========================================================
    // Getter & Setter
    // ===========================================================

    /** set volume title */
    public void setTitle(int id) {
        mTitle.setText(id);
    }

    /** set current settings status */
    private void setStatusDescription(int id) {
        mStatusDescription.setText(id);
    }

    // ===========================================================
    // Methods for/from SuperClass/Interfaces
    // ===========================================================

    // ===========================================================
    // Methods
    // ===========================================================

    /**
     * init current settings
     *
     * @param keyEnabled   {@link SharedPreferences} switcher key (used to disable volume)
     * @param keyValue     {@link SharedPreferences} key used to store setting value
     * @param resEnabled   if keyEnabled then this resource will be used as status description
     * @param resDisabled  if not keyEnabled then this resource will be used as status description
     * @param defaultValue value if there is no previously set
     */
    public void initSettingsVolume(String keyEnabled, String keyValue,
                                   int resEnabled, int resDisabled, float defaultValue) {
        mSharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(EaFallApplication.getContext());
        mVolumeEnabledPreferencesKey = keyEnabled;
        mLight.init(keyValue, defaultValue);
        mResEnabled = resEnabled;
        mResDisabled = resDisabled;
        initVolumeCheckbox();
        updateVolumeSettings(mVolumeCheckbox.isChecked());
    }


    /** volume checkbox initialization */
    private void initVolumeCheckbox() {
        boolean currentStatus
                = mSharedPreferences.getBoolean(mVolumeEnabledPreferencesKey, false);
        mVolumeCheckbox.setChecked(currentStatus);
        mVolumeCheckbox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            updateVolumeSettings(isChecked);
            mSharedPreferences.edit().putBoolean(mVolumeEnabledPreferencesKey, isChecked).apply();
        });
    }

    /** updates status description and seek-bar cover ({@link SettingsVolume#mLight}) */
    private void updateVolumeSettings(boolean status) {
        if (status) {
            setStatusDescription(mResEnabled);
            mLight.getBackground().setColorFilter(null);
        } else {
            setStatusDescription(mResDisabled);
            mLight.getBackground().setColorFilter(mDisableColorFilter);
        }
        mIncreaseButton.setEnabled(status);
        mIncreaseButton.setClickable(status);
        mIncreaseButton.setPressed(!status);
        mDecreaseButton.setEnabled(status);
        mDecreaseButton.setClickable(status);
        mDecreaseButton.setPressed(!status);
        mLight.setEnabled(status);
    }

    /** used in constructor to initialize the view */
    private void _init_() {
        View view = inflate(getContext(), R.layout.settings_volume, this);
        mLight = view.findViewById(R.id.light);
        mIncreaseButton = view.findViewById(R.id.increase);
        initIncButton(mIncreaseButton);
        mDecreaseButton = view.findViewById(R.id.decrease);
        initDecButton(mDecreaseButton);
        mVolumeCheckbox = view.findViewById(R.id.volume_checkbox);
        mTitle = view.findViewById(R.id.volume_text);
        mStatusDescription = view.findViewById(R.id.volume_description);
        mVolumeBackground = view.findViewById(R.id.middle);
    }

    /** increase volume button initialization */
    private void initIncButton(Button button) {
        button.setOnClickListener(v -> mLight.increase());
    }

    /** decrease volume button initialization */
    private void initDecButton(Button button) {
        button.setOnClickListener(v -> mLight.decrease());
    }

    // ===========================================================
    // Inner and Anonymous Classes
    // ===========================================================
}
