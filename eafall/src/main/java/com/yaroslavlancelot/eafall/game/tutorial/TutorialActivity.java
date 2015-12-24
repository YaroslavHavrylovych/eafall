package com.yaroslavlancelot.eafall.game.tutorial;

import android.view.MotionEvent;

import com.yaroslavlancelot.eafall.R;
import com.yaroslavlancelot.eafall.game.client.thick.income.ClientIncomeHandler;
import com.yaroslavlancelot.eafall.game.client.thick.single.SinglePlayerGameActivity;
import com.yaroslavlancelot.eafall.game.entity.gameobject.staticobject.planet.PlanetStaticObject;
import com.yaroslavlancelot.eafall.game.popup.IPopup;
import com.yaroslavlancelot.eafall.game.scene.hud.ClientGameHud;
import com.yaroslavlancelot.eafall.game.scene.scenes.EaFallScene;

import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.entity.IEntity;
import org.andengine.entity.sprite.ButtonSprite;
import org.andengine.input.touch.TouchEvent;

/**
 * Tutorial mission
 *
 * @author Yaroslav Havrylovych
 */
public class TutorialActivity extends SinglePlayerGameActivity {
    // ===========================================================
    // Constants
    // ===========================================================

    // ===========================================================
    // Fields
    // ===========================================================
    private TutorialPopup mTutorialPopup;

    // ===========================================================
    // Constructors
    // ===========================================================

    // ===========================================================
    // Getter & Setter
    // ===========================================================

    // ===========================================================
    // Methods for/from SuperClass/Interfaces
    // ===========================================================


    @Override
    protected void startAsyncResourceLoading() {
        super.startAsyncResourceLoading();
        TutorialPopup.loadResources(this, getTextureManager());
        TutorialPopup.loadFonts(getFontManager(), getTextureManager());
    }

    @Override
    protected void onPopulateWorkingScene(final EaFallScene scene) {
        super.onPopulateWorkingScene(scene);
        mTutorialPopup = new TutorialPopup(mHud, mCamera, getVertexBufferObjectManager());
        ClientIncomeHandler.getIncomeHandler().registerIncomeListener(new HintableIncomeListener());
    }


    // ===========================================================
    // Methods
    // ===========================================================
    private void emulateClick(IEntity entity) {
        long time = System.currentTimeMillis();
        entity.onAreaTouched(createTouch(entity, MotionEvent.ACTION_DOWN, time), 0, 0);
        entity.onAreaTouched(createTouch(entity, MotionEvent.ACTION_UP, time), 0, 0);
    }

    private TouchEvent createTouch(IEntity entity, int action, long time) {
        long eventTime = time + 100;
        int metaState = 0;
        float x = entity.getX();
        float y = entity.getY();

        MotionEvent motionEvent = MotionEvent.obtain(time, eventTime, action,
                entity.getX(), entity.getY(), metaState);
        return TouchEvent.obtain(x, y, action, 0, motionEvent);
    }

    // ===========================================================
    // Inner and Anonymous Classes
    // ===========================================================

    /** first hint to press '+' */
    private class HintableIncomeListener implements ClientIncomeHandler.IncomeListener {
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
                                mTutorialPopup.initText1(1040, 910, R.string.tutorial_click_plus);
                                mTutorialPopup.initPointer1(525, 805, 160);
                                mTutorialPopup.setClickableArea(
                                        ClientIncomeHandler.getPlanetIncomeX(planet.getX(), planet.getWidth()),
                                        ClientIncomeHandler.getPlanetIncomeY(planet.getY(), planet.getHeight()),
                                        25, 25);
                                mTutorialPopup.setStateChangeListener(new IPopup.StateChangingListener() {
                                    @Override
                                    public void onShowed() {
                                    }

                                    @Override
                                    public void onHided() {
                                        emulateClick(incomeButton);
                                        pause(false);
                                        hud.blockInput(true);
                                        mSceneManager.getWorkingScene().registerUpdateHandler(
                                                new TimerHandler(.5f,
                                                        new ShowOxygenValueCallback(mTutorialPopup))
                                        );
                                    }
                                });
                                mTutorialPopup.showPopup();
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

    /** shows oxygen */
    private class ShowOxygenValueCallback implements ITimerCallback {
        private TutorialPopup mTutorialPopup;

        private ShowOxygenValueCallback(TutorialPopup tutorialPopup) {
            mTutorialPopup = tutorialPopup;
        }

        @Override
        public void onTimePassed(final TimerHandler pTimerHandler) {
            mSceneManager.getWorkingScene().unregisterUpdateHandler(pTimerHandler);
            mTutorialPopup.initText1(1010, 920, R.string.tutorial_oxygen_value);
            mTutorialPopup.initPointer1(365, 1005, 190);
            mTutorialPopup.showPopup();
            pause(true);
            //TODO constructions popup invoker
        }
    }
}
