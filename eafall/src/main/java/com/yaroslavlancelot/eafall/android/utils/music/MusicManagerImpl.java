package com.yaroslavlancelot.eafall.android.utils.music;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Handler;
import android.support.annotation.NonNull;

import com.yaroslavlancelot.eafall.EaFallApplication;
import com.yaroslavlancelot.eafall.R;
import com.yaroslavlancelot.eafall.game.configuration.game.ApplicationSettings;

import java.util.HashMap;
import java.util.Map;

import timber.log.Timber;

/**
 * Used to play music in background across all activities.
 */
class MusicManagerImpl implements Music {
    private Context mContext;
    private Map<MusicType, MediaPlayer> mPlayers = new HashMap<>(MusicType.values().length);
    private Map<MusicType, StopRunnable> mMusicStopCallbacks
            = new HashMap<>(MusicType.values().length);
    private Map<MusicType, Handler> mMusicStopHandler = new HashMap<>(MusicType.values().length);
    private MusicType mCurrentlyPlaying = MusicType.NONE;
    private int mDelayToStopMusicMillis = 1000;
    private boolean mMusicEnabled;
    private float mMaxVolume;

    MusicManagerImpl(Context context) {
        mContext = context;
        initSettingsCallbacks();
    }

    @Override
    public void startPlaying(@NonNull MusicType music) {
        if (!mMusicEnabled) {
            Timber.v("music is disabled");
            mCurrentlyPlaying = music;
            return;
        }

        Handler stopHandler = mMusicStopHandler.get(music);
        if (stopHandler != null) {
            StopRunnable stopRunnable = mMusicStopCallbacks.get(music);
            if (stopRunnable != null) {
                stopHandler.removeCallbacks(mMusicStopCallbacks.get(music));
            }
        }
        if (mCurrentlyPlaying != music) {
            Timber.v("start playing %s", music.toString());
            MediaPlayer mediaPlayer = initPlayer(music);
            mediaPlayer.setLooping(true);
            mediaPlayer.setVolume(mMaxVolume, mMaxVolume);
            mediaPlayer.start();
        }
    }

    @Override
    public void stopPlaying() {
        for (MusicType musicType : mPlayers.keySet()) {
            Timber.v("stop playing");
            StopRunnable stopRunnable = mMusicStopCallbacks.get(musicType);
            if (stopRunnable == null) {
                stopRunnable = new StopRunnable(musicType);
                mMusicStopCallbacks.put(musicType, stopRunnable);
            }
            Handler handler = mMusicStopHandler.get(musicType);
            if (handler == null) {
                handler = new Handler();
                mMusicStopHandler.put(musicType, handler);
            }
            handler.removeCallbacks(stopRunnable);
            handler.postDelayed(stopRunnable, mDelayToStopMusicMillis);
        }
    }

    private void initSettingsCallbacks() {
        final ApplicationSettings settings
                = EaFallApplication.getConfig().getSettings();
        mMusicEnabled = settings.isMusicEnabled();
        mMaxVolume = settings.getMusicVolumeMax();
        settings.setOnConfigChangedListener(settings.KEY_PREF_MUSIC,
                new ApplicationSettings.ISettingsChangedListener() {
                    @Override
                    public void configChanged(final Object value) {
                        mMusicEnabled = (Boolean) value;
                        if (mMusicEnabled) {
                            startPlaying(mCurrentlyPlaying);
                        } else {
                            stopPlaying();
                        }
                    }
                });
        settings.setOnConfigChangedListener(settings.KEY_PREF_MUSIC_VOLUME,
                new ApplicationSettings.ISettingsChangedListener() {
                    @Override
                    public void configChanged(final Object value) {
                        mMaxVolume = (Float) value;
                        for (MediaPlayer mediaPlayer : mPlayers.values()) {
                            mediaPlayer.setVolume(mMaxVolume, mMaxVolume);
                        }
                    }
                });
    }

    private MediaPlayer initPlayer(@NonNull MusicType musicType) {
        mCurrentlyPlaying = musicType;
        MediaPlayer mediaPlayer = mPlayers.get(musicType);
        if (mediaPlayer == null) {
            switch (musicType) {
                case NONE:
                case START_MENU: {
                    mediaPlayer = MediaPlayer.create(mContext, R.raw.main_menu);
                    break;
                }
                case CAMPAIGN: {
                    mediaPlayer = MediaPlayer.create(mContext, R.raw.campaign);
                    break;
                }
                case IN_GAME: {
                    mediaPlayer = MediaPlayer.create(mContext, R.raw.in_game);
                    break;
                }
            }
            mPlayers.put(musicType, mediaPlayer);
        }
        return mediaPlayer;
    }

    private class StopRunnable implements Runnable {
        private Music.MusicType mMusicType;

        StopRunnable(Music.MusicType musicType) {
            mMusicType = musicType;
        }

        @Override
        public void run() {
            Timber.v("stop playing %s", mMusicType.toString());
            MediaPlayer player = mPlayers.get(mMusicType);
            if (player == null) {
                return;
            }
            if (player.isPlaying()) {
                player.pause();
            }
            player.stop();
            player.release();
            mPlayers.remove(mMusicType);
            if (mPlayers.isEmpty()) {
                mCurrentlyPlaying = MusicType.NONE;
            }
        }
    }
}
