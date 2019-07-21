package com.yaroslavlancelot.eafall.android.fragment;

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
import android.widget.Toast;

import com.yaroslavlancelot.eafall.EaFallApplication;
import com.yaroslavlancelot.eafall.R;
import com.yaroslavlancelot.eafall.android.view.SettingsVolume;
import com.yaroslavlancelot.eafall.game.configuration.game.ApplicationSettings;

/** Full-screen application settings */
public class GameSettingsFragment extends Fragment {
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

    public GameSettingsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.game_settings_layout, container, false);
        mSharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(EaFallApplication.getContext());
        initKeys();
        ApplicationSettings applicationSettings = EaFallApplication.getConfig().getSettings();
        //background music volume
        mMusicVolume = view.findViewById(R.id.music_volume);
        mMusicVolume.setTitle(R.string.music);
        mMusicVolume.initSettingsVolume(KEY_MUSIC, KEY_MUSIC_VOLUME,
                R.string.music_on, R.string.music_off, applicationSettings.getMusicVolumeMax());
        //game sound volume
        mSoundVolume = view.findViewById(R.id.sound_volume);
        mSoundVolume.setTitle(R.string.game_sounds);
        mSoundVolume.initSettingsVolume(KEY_SOUND, KEY_SOUND_VOLUME,
                R.string.game_sounds_on, R.string.game_sounds_off,
                applicationSettings.getSoundVolumeMax());
        //dev mode
        mDevelopersModeCheckBox = view.findViewById(R.id.developers_mode_checkbox);
        initDeveloperModeSettings(mDevelopersModeCheckBox,
                view.findViewById(R.id.developers_mode_state_text));
        //health bar
        mHealthBarBehaviour = view.findViewById(R.id.health_bar_behaviour);
        initHealthBar(mHealthBarBehaviour);
        return view;
    }

    /** initialize health bar button */
    private void initHealthBar(final Button button) {
        button.setOnClickListener(v -> {
            Toast.makeText(getContext(), R.string.not_implemented, Toast.LENGTH_SHORT).show();
            //                String val = mSharedPreferences.getString(KEY_HEALTH_BAR,
            //                        ApplicationSettings.UnitHealthBarBehavior.DEFAULT.name());
            //                ApplicationSettings.UnitHealthBarBehavior behavior =
            //                        ApplicationSettings.UnitHealthBarBehavior.valueOf(val);
            //                HealthBarDialog dialogFragment = new HealthBarDialog();
            //                dialogFragment.init(new HealthBarDialog.HealthBarTypeSet() {
            //                    @Override
            //                    public void onTypeSet(final ApplicationSettings.UnitHealthBarBehavior healthBarBehavior) {
            //                        mSharedPreferences.edit().putString(KEY_HEALTH_BAR,
            //                                healthBarBehavior.name()).apply();
            //                    }
            //                }, behavior);
            //                dialogFragment.show(getFragmentManager(), HealthBarDialog.KEY);
        });
    }

    /** developers mode check box initialization */
    private void initDeveloperModeSettings(CheckBox checkBox, final TextView description) {
        boolean isChecked = mSharedPreferences.getBoolean(KEY_DEV_MODE, false);
        checkBox.setChecked(isChecked);
        updateTextView(isChecked, R.string.developers_mode_on,
                R.string.developers_mode_off, description);
        checkBox.setOnCheckedChangeListener((buttonView, isChecked1) -> {
            updateTextView(isChecked1, R.string.developers_mode_on,
                    R.string.developers_mode_off, description);
            mSharedPreferences.edit().putBoolean(KEY_DEV_MODE, isChecked1).apply();
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
