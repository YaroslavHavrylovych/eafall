package com.gmail.yaroslavlancelot.eafall.android.fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.gmail.yaroslavlancelot.eafall.EaFallApplication;
import com.gmail.yaroslavlancelot.eafall.R;
import com.gmail.yaroslavlancelot.eafall.android.activities.StartupActivity;
import com.gmail.yaroslavlancelot.eafall.android.dialog.HealthBarDialog;
import com.gmail.yaroslavlancelot.eafall.android.view.SettingsVolume;
import com.gmail.yaroslavlancelot.eafall.game.configuration.game.ApplicationSettings;

/** Full-screen application settings */
public class SettingsFragment extends Fragment {
    /*Constants (or it has to be constants)*/
    private String KEY_DEV_MODE;
    private String KEY_HEALTH_BAR;
    private String KEY_MUSIC;
    private String KEY_MUSIC_VOLUME;
    private String KEY_SOUND;
    private String KEY_SOUND_VOLUME;
    /*Fields*/
    /** shared preferences instance */
    private SharedPreferences mSharedPreferences;
    /** background music volume */
    private SettingsVolume mMusicVolume;
    /** game sounds volume */
    private SettingsVolume mSoundVolume;
    /** developers mode checkbox */
    private CheckBox mDevelopersModeCheckBox;
    /** unit health bar behaviour picket */
    private Button mHealthBarBehaviour;
    /** settings on screen back button */
    private Button mBackButton;
    /** back button on click listener */
    private View.OnClickListener mBackButtonOnClickListener;

    public SettingsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.settings_full_screen_layout, container, false);
        mSharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(EaFallApplication.getContext());
        initKeys();
        //header
        TextView textView = (TextView) view.findViewById(R.id.title_text);
        textView.getPaint().setShader(StartupActivity.getTextGradient(
                (int) getResources().getDimension(R.dimen.settings_title_text_size)));
        //background music volume
        mMusicVolume = (SettingsVolume) view.findViewById(R.id.music_volume);
        mMusicVolume.setTitle(R.string.music_volume);
        mMusicVolume.initSettingsVolume(KEY_MUSIC, KEY_MUSIC_VOLUME,
                R.string.music_on, R.string.music_off);
        //game sound volume
        mSoundVolume = (SettingsVolume) view.findViewById(R.id.sound_volume);
        mSoundVolume.setTitle(R.string.game_sounds_volume);
        mSoundVolume.initSettingsVolume(KEY_SOUND, KEY_SOUND_VOLUME,
                R.string.game_sounds_on, R.string.game_sounds_off);
        //dev mode
        mDevelopersModeCheckBox = (CheckBox) view.findViewById(R.id.developers_mode_checkbox);
        initDeveloperModeSettings(mDevelopersModeCheckBox,
                (TextView) view.findViewById(R.id.developers_mode_state_text));
        //health bar
        mHealthBarBehaviour = (Button) view.findViewById(R.id.health_bar_behaviour);
        initHealthBar(mHealthBarBehaviour);
        //back button
        mBackButton = (Button) view.findViewById(R.id.back_button);
        mBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                if (mBackButtonOnClickListener != null) {
                    mBackButtonOnClickListener.onClick(v);
                }
            }
        });
        return view;
    }

    /**
     * settings back button click listener (used to close the settings)
     *
     * @param onClickListener back button new click listener
     */
    public void addBackButtonOnClickListener(View.OnClickListener onClickListener) {
        mBackButtonOnClickListener = onClickListener;
    }

    /** initialize health bar button */
    private void initHealthBar(final Button button) {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                String val = mSharedPreferences.getString(KEY_HEALTH_BAR,
                        ApplicationSettings.UnitHealthBarBehavior.DEFAULT.name());
                ApplicationSettings.UnitHealthBarBehavior behavior =
                        ApplicationSettings.UnitHealthBarBehavior.valueOf(val);
                HealthBarDialog dialogFragment = new HealthBarDialog();
                dialogFragment.init(new HealthBarDialog.HealthBarTypeSet() {
                    @Override
                    public void onTypeSet(final ApplicationSettings.UnitHealthBarBehavior healthBarBehavior) {
                        mSharedPreferences.edit().putString(KEY_HEALTH_BAR,
                                healthBarBehavior.name()).apply();
                    }
                }, behavior);
                dialogFragment.show(getFragmentManager(), HealthBarDialog.KEY);
            }
        });
    }

    /** developers mode check box initialization */
    private void initDeveloperModeSettings(CheckBox checkBox, final TextView description) {
        boolean isChecked = mSharedPreferences.getBoolean(KEY_DEV_MODE, false);
        checkBox.setChecked(isChecked);
        updateTextView(isChecked, R.string.developers_mode_on,
                R.string.developers_mode_off, description);
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(final CompoundButton buttonView, final boolean isChecked) {
                updateTextView(isChecked, R.string.developers_mode_on,
                        R.string.developers_mode_off, description);
                mSharedPreferences.edit().putBoolean(KEY_DEV_MODE, isChecked).apply();
            }
        });
    }

    private void initKeys() {
        ApplicationSettings settings = EaFallApplication.getConfig().getSettings();
        //just a syntax sugar
        KEY_DEV_MODE = settings.KEY_PREF_DEVELOPERS_MODE;
        KEY_HEALTH_BAR = settings.KEY_PREF_UNIT_HEALTH_BAR_BEHAVIOR;
        KEY_MUSIC = settings.KEY_PREF_MUSIC;
        KEY_MUSIC_VOLUME = settings.KEY_PREF_MUSIC_VOLUME;
        KEY_SOUND = settings.KEY_PREF_SOUNDS;
        KEY_SOUND_VOLUME = settings.KEY_PREF_SOUNDS_VOLUME;
    }

    /**
     * update text view based on isTrueVal flag
     *
     * @param isTrueVal flag
     * @param trueVal   text view text in case of positive flag
     * @param falseVal  text view text in case of negative flag
     * @param textView  changeable text view
     */
    private void updateTextView(boolean isTrueVal, int trueVal, int falseVal, TextView textView) {
        if (isTrueVal) {
            textView.setText(trueVal);
        } else {
            textView.setText(falseVal);
        }
        textView.invalidate();
    }
}
