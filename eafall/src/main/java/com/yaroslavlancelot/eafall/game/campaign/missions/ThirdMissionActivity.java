package com.yaroslavlancelot.eafall.game.campaign.missions;

import com.yaroslavlancelot.eafall.R;
import com.yaroslavlancelot.eafall.game.BaseTutorialActivity;
import com.yaroslavlancelot.eafall.game.client.thick.income.ClientIncomeHandler;
import com.yaroslavlancelot.eafall.game.constant.SizeConstants;
import com.yaroslavlancelot.eafall.game.scene.hud.ClientGameHud;
import com.yaroslavlancelot.eafall.game.scene.scenes.EaFallScene;

import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.entity.sprite.ButtonSprite;

/** First mission. Limited units amount and no supressor. Only few hints to build. */
public class ThirdMissionActivity extends BaseTutorialActivity {
    @Override
    protected void onPopulateWorkingScene(final EaFallScene scene) {
        super.onPopulateWorkingScene(scene);
        ClientIncomeHandler.getIncomeHandler().registerIncomeListener(new IncomeListener());
    }

    /**
     * 1
     * <p>
     * first hint to press '+'
     */
    private class IncomeListener implements ClientIncomeHandler.IncomeListener {
        private boolean mFirstIncome = true;

        @Override
        public void onIncome(final int value, final ClientIncomeHandler.IncomeType incomeType,
                             final ButtonSprite incomeButton) {
            final ClientGameHud hud = ((ClientGameHud) mHud);
            if (mFirstIncome) {
                ClientIncomeHandler.getIncomeHandler().unregisterIncomeListener(this);
                mFirstIncome = false;
                hud.blockInput(true);
                mSceneManager.getWorkingScene().registerUpdateHandler(new TimerHandler(.5f,
                        new ITimerCallback() {
                            @Override
                            public void onTimePassed(final TimerHandler pTimerHandler) {
                                mSceneManager.getWorkingScene().unregisterUpdateHandler(pTimerHandler);
                                pause(true);
                                hud.blockInput(false);
                                mClickOnPointPopup.resetTouchToDefault();
                                mClickOnPointPopup.initText1(SizeConstants.HALF_FIELD_WIDTH,
                                        SizeConstants.HALF_FIELD_HEIGHT,
                                        R.string.tutorial_description_suppressor_usage);
                            }
                        }));
            }
        }
    }
}
