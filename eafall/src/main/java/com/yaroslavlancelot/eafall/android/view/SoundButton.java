package com.yaroslavlancelot.eafall.android.view;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;

import com.yaroslavlancelot.eafall.EaFallApplication;
import com.yaroslavlancelot.eafall.R;

/** The simplest way to have custom click sound on a button */
public class SoundButton extends Button implements Button.OnClickListener {
    private Button.OnClickListener mOnClickListener;

    public SoundButton(Context context) {
        super(context);
        init();
    }

    public SoundButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SoundButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public SoundButton(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        super.setOnClickListener(this);
    }

    @Override
    public void setOnClickListener(OnClickListener l) {
        mOnClickListener = l;
    }

    @Override
    public void onClick(View v) {
        if (mOnClickListener != null) {
            mOnClickListener.onClick(v);
        }
        if (!EaFallApplication.getConfig().getSettings().isSoundsEnabled()) {
            return;
        }
        Context context = getContext();
        MediaPlayer mp;
        mp = MediaPlayer.create(context, R.raw.click_button);
        mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mp.reset();
                mp.release();
            }

        });
        float volume = EaFallApplication.getConfig().getSettings().getSoundVolumeMax();
        mp.setVolume(volume, volume);
        mp.start();
    }
}
