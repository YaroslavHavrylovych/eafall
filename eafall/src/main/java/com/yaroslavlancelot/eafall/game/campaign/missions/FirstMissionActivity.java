package com.yaroslavlancelot.eafall.game.campaign.missions;

import com.yaroslavlancelot.eafall.R;
import com.yaroslavlancelot.eafall.game.BaseTutorialActivity;
import com.yaroslavlancelot.eafall.game.client.thick.income.ClientIncomeHandler;
import com.yaroslavlancelot.eafall.game.constant.SizeConstants;
import com.yaroslavlancelot.eafall.game.entity.gameobject.staticobject.planet.PlanetStaticObject;
import com.yaroslavlancelot.eafall.game.popup.IPopup;
import com.yaroslavlancelot.eafall.game.scene.hud.ClientGameHud;
import com.yaroslavlancelot.eafall.game.scene.scenes.EaFallScene;

import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.entity.sprite.ButtonSprite;

/** First mission. Limited units amount and no supressor. Only few hints to build. */
public class FirstMissionActivity extends BaseTutorialActivity {
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
        private boolean mSecondIncome = true;

        @Override
        public void onIncome(final int value, final ClientIncomeHandler.IncomeType incomeType,
                             final ButtonSprite incomeButton) {
            final ClientGameHud hud = ((ClientGameHud) mHud);
            if (mFirstIncome) {
                mFirstIncome = false;
                hud.blockInput(true);
                mSceneManager.getWorkingScene().registerUpdateHandler(new TimerHandler(.5f,
                        new ITimerCallback() {
                            @Override
                            public void onTimePassed(final TimerHandler pTimerHandler) {
                                //shows click on + hint
                                mSceneManager.getWorkingScene().unregisterUpdateHandler(pTimerHandler);
                                pause(true);
                                hud.blockInput(false);
                                PlanetStaticObject planet = mFirstPlayer.getPlanet();
                                mClickOnPointPopup.initText1(1040, 910, R.string.tutorial_click_plus);
                                mClickOnPointPopup.initPointer1(525, 805, 160);
                                float x = ClientIncomeHandler.getPlanetIncomeX(planet.getX(), planet.getWidth());
                                float y = ClientIncomeHandler.getPlanetIncomeY(planet.getY(), planet.getHeight());
                                float[] res = mCamera.getCameraSceneCoordinatesFromSceneCoordinates(x, y);
                                mClickOnPointPopup.setClickableArea(res[0], res[1],
                                        SizeConstants.INCOME_IMAGE_SIZE, SizeConstants.INCOME_IMAGE_SIZE);
                                mClickOnPointPopup.setStateChangeListener(new IPopup.StateChangingListener() {
                                    @Override
                                    public void onShowed() {
                                    }

                                    @Override
                                    public void onHided() {
                                        mClickOnPointPopup.removeStateChangeListener();
                                        emulateClick(incomeButton);
                                        pause(false);
                                        hud.blockInput(true);
                                        final ClientGameHud hud = (ClientGameHud) mHud;
                                        mClickOnPointPopup.removeStateChangeListener();
                                        mClickOnPointPopup.initText1(SizeConstants.HALF_FIELD_WIDTH,
                                                350, R.string.tutorial_constructions_button);
                                        mClickOnPointPopup.initPointer1(675, 170, 36);
                                        mClickOnPointPopup.setClickableArea(SizeConstants.HALF_FIELD_WIDTH,
                                                SizeConstants.CONSTRUCTIONS_POPUP_INVOCATION_BUTTON_HEIGHT / 2,
                                                SizeConstants.CONSTRUCTIONS_POPUP_INVOCATION_BUTTON_WIDTH,
                                                SizeConstants.CONSTRUCTIONS_POPUP_INVOCATION_BUTTON_HEIGHT);
                                        mClickOnPointPopup.setStateChangeListener(new IPopup.StateChangingListener() {
                                            @Override
                                            public void onShowed() {
                                            }

                                            @Override
                                            public void onHided() {
                                                mClickOnPointPopup.removeStateChangeListener();
                                                hud.blockInput(false);
                                                ButtonSprite button = hud.getConstructionButton();
                                                emulateClick(button);
                                            }
                                        });
                                        mClickOnPointPopup.showPopup();
                                    }
                                });
                                mClickOnPointPopup.showPopup();
                            }
                        }));
                return;
            }
            if (mSecondIncome) {
                mSecondIncome = false;
                ClientIncomeHandler.getIncomeHandler().unregisterIncomeListener(this);
            }
        }
    }
}
