package com.yaroslavlancelot.eafall.game.campaign.pass;

import android.content.Context;
import android.content.SharedPreferences;

import com.yaroslavlancelot.eafall.EaFallApplication;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import timber.log.Timber;

/**
 * Implementation for {@link CampaignPassage} where data stored in {@link android.content.SharedPreferences}
 *
 * @author Yaroslav Havrylovych
 */
class CampaignPassageSharedPrefImpl implements CampaignPassage {
    /** passage campaign file name */
    private String mFileName;
    private static final int PREFERENCES_MODE = Context.MODE_PRIVATE;
    private static final String CAMPAIGN_KEY = "campaign_key";
    private static final String LAST_PLAYED_MISSION = "last_played_mission_key";
    private Context mContext;

    /**
     * Get's passage information for particular campaign
     *
     * @param fileName campaign file name
     * @param context  used to store and retrieve data from preferences
     */
    CampaignPassageSharedPrefImpl(String fileName, Context context) {
        try {
            mFileName = URLEncoder.encode(fileName, "utf-8");
        } catch (UnsupportedEncodingException e) {
            Timber.e(e, "can't campaign encode file name");
            mFileName = fileName.substring(fileName.length() - 1);
        }
        mContext = context;
    }


    @Override
    public int getPassedCampaignsAmount() {
        return mContext.getSharedPreferences(mFileName, PREFERENCES_MODE)
                .getInt(CAMPAIGN_KEY, 0);
    }

    @Override
    public void markNewCampaignPassed() {
        SharedPreferences.Editor editor = mContext
                .getSharedPreferences(mFileName, PREFERENCES_MODE).edit();
        editor.putInt(CAMPAIGN_KEY, getPassedCampaignsAmount() + 1);
        editor.apply();
    }

    @Override
    public int getLastPlayedMission() {
        return mContext.getSharedPreferences(mFileName, PREFERENCES_MODE)
                .getInt(LAST_PLAYED_MISSION, 0);
    }

    @Override
    public void setLastPlayedMission(int id) {
        SharedPreferences.Editor editor = mContext
                .getSharedPreferences(mFileName, PREFERENCES_MODE).edit();
        editor.putInt(LAST_PLAYED_MISSION, id);
        editor.apply();
    }

    @Override
    public boolean checkCampaignPassed(int position) {
        return position < getPassedCampaignsAmount();
    }

    @Override
    public void reset() {
        SharedPreferences.Editor editor = mContext
                .getSharedPreferences(mFileName, PREFERENCES_MODE).edit();
        editor.putInt(CAMPAIGN_KEY, 0);
        editor.apply();
    }
}
