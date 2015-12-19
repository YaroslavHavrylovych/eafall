package com.yaroslavlancelot.eafall.android.view;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.preference.PreferenceManager;
import android.util.AttributeSet;
import android.view.View;

import com.yaroslavlancelot.eafall.EaFallApplication;
import com.yaroslavlancelot.eafall.R;

import org.andengine.util.adt.map.SparseFloatArray;
import org.andengine.util.math.MathUtils;

/**
 * Shows progress in by drawing part or full (with progressing increase of visible view area)
 * of the view.
 *
 * @author Yaroslav Havrylovych
 */
public class PercentLight extends View {
    /** drawable to draw view partly, not to draw or draw fully */
    private PartlyDrawable mPartlyDrawable;
    /** {@link SharedPreferences} to save the progress (this key used to store float value) */
    private String mValueKey;
    /** used to store the progress */
    private SharedPreferences mSharedPreferences;
    /**
     * View changes value gradually from 0 till some int max value or vice versa.
     * Each int value which changes gradually has it's corresponding float value
     * for the progress.
     */
    private SparseFloatArray mValueToPercentage = new SparseFloatArray(11) {{
        put(0, 0f);
        put(1, .16f);
        put(2, .21f);
        put(3, .26f);
        put(4, .312f);
        put(5, .36f);
        put(6, .41f);
        put(7, .459f);
        put(8, .509f);
        put(9, .558f);
        put(10, .608f);
        put(11, .657f);
        put(12, .708f);
        put(13, .757f);
        put(14, .807f);
        put(15, .99f);
    }};

    public PercentLight(final Context context, final AttributeSet attrs) {
        super(context, attrs);
        _init_();
    }

    public void init(String settingsValueKey) {
        mSharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(EaFallApplication.getContext());
        mValueKey = settingsValueKey;
        updateIdByValue(mSharedPreferences.getFloat(mValueKey, .5f));
    }

    /** increment progress */
    public void increase() {
        mPartlyDrawable.setId(mPartlyDrawable.mCurrentValueId + 1);
    }

    /** decrement progress */
    public void decrease() {
        mPartlyDrawable.setId(mPartlyDrawable.mCurrentValueId - 1);
    }

    /** search the id with the closest corresponding value and set it as the current value */
    private void updateIdByValue(float value) {
        int currentValueId = mValueToPercentage.indexOfValue(value);
        if (currentValueId == -1) {
            currentValueId = 0;
            float distance = Math.abs(value - mValueToPercentage.indexOfValue(currentValueId));
            for (int i = 1; i < mValueToPercentage.size(); i++) {
                float loopValue = mValueToPercentage.get(i);
                if (Math.abs(value - loopValue) < distance) {
                    currentValueId = i;
                    distance = Math.abs(value - loopValue);
                }
            }
        }
        mPartlyDrawable.setId(currentValueId);
    }


    /** class initialization */
    private void _init_() {
        //TODO logger was here
        setWillNotDraw(false);
        mPartlyDrawable = new PartlyDrawable();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            setBackground(mPartlyDrawable);
        } else {
            setBackgroundDrawable(mPartlyDrawable);
        }
    }

    /** draw only part of the view which you can set with percentage */
    private class PartlyDrawable extends BitmapDrawable {
        public final int MAX_ID = 15;
        private Rect mDrawableRect = new Rect(0, 0, 0, 0);
        private int mCurrentValueId = 0;

        private PartlyDrawable() {
            super(getResources(), getResources().openRawResource(R.raw.volume_light));
        }

        public void setId(int id) {
            if (MathUtils.isInBounds(0, MAX_ID, id)) {
                mCurrentValueId = id;
                mSharedPreferences.edit().putFloat(mValueKey, mValueToPercentage.get(id)).apply();
                updatePercentage();
                getBackground().invalidateSelf();
            }
        }

        @Override
        public void setBounds(final int left, final int top, final int right, final int bottom) {
            super.setBounds(left, top, right, bottom);
            updatePercentage();
        }

        @Override
        public void draw(final Canvas canvas) {
            canvas.clipRect(mDrawableRect);
            super.draw(canvas);
        }

        public void updatePercentage() {
            Rect bounds = getBounds();
            mDrawableRect.set(bounds.left, bounds.top,
                    bounds.right - Math.round(
                            ((float) bounds.width()) * (1f - mValueToPercentage.get(mCurrentValueId))),
                    bounds.bottom);
        }
    }
}
