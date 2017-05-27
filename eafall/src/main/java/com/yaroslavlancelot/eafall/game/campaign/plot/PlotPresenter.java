package com.yaroslavlancelot.eafall.game.campaign.plot;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.view.View;

import com.yaroslavlancelot.eafall.R;

/**
 * Used to present game plot. Accepts the string with the plot. Plot is some consequence
 * of sentences. The presented is a dialog. The whole plot splits in multiple dialog presented
 * one by one. So if you place {@link #PLOT_TEXT_SEPARATOR} in a plot (string with a text) than
 * each popup will have a part which separated by the splitter {@link #PLOT_TEXT_SEPARATOR}
 */
public class PlotPresenter {
    public static final String PLOT_TEXT_SEPARATOR = "/:";
    private final String[] mPlot;
    private final Activity mActivity;
    private final PlotPresentedCallback mPlotPresentedCallback;
    private final PlotTellerDialog mPlotTellerDialog;

    public PlotPresenter(@StringRes int plotId, @NonNull String missionName,
                         Activity activity,
                         PlotPresentedCallback plotPresentedCallback) {
        mActivity = activity;
        mPlotPresentedCallback = plotPresentedCallback;
        mPlot = activity.getString(plotId, missionName).split(PLOT_TEXT_SEPARATOR);
        mPlotTellerDialog = new PlotTellerDialog(activity);
    }

    public void startPresentation() {
        presentMessage(0);
        mPlotTellerDialog.show();
    }

    private void presentMessage(final int messagePosition) {
        mPlotTellerDialog.setText(mPlot[messagePosition]);
        if (messagePosition == mPlot.length - 1) {
            mPlotTellerDialog.setNextButtonText(mActivity.getString(R.string.done));
            mPlotTellerDialog.setOnNextCallback(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mPlotPresentedCallback.onPresentationDone();
                    mPlotTellerDialog.dismiss();
                }
            });
        } else {
            mPlotTellerDialog.setNextButtonText(mActivity.getString(R.string.next));
            mPlotTellerDialog.setOnNextCallback(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    presentMessage(messagePosition + 1);
                }
            });
        }
    }

    public interface PlotPresentedCallback {
        void onPresentationDone();
    }
}
