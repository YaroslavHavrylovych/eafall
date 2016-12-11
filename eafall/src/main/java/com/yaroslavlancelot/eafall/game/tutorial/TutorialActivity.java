package com.yaroslavlancelot.eafall.game.tutorial;

import android.content.DialogInterface;

import com.yaroslavlancelot.eafall.R;
import com.yaroslavlancelot.eafall.game.BaseTutorialActivity;
import com.yaroslavlancelot.eafall.game.client.thick.income.ClientIncomeHandler;
import com.yaroslavlancelot.eafall.game.constant.SizeConstants;
import com.yaroslavlancelot.eafall.game.entity.gameobject.building.buildings.IUnitBuilding;
import com.yaroslavlancelot.eafall.game.entity.gameobject.staticobject.planet.PlanetStaticObject;
import com.yaroslavlancelot.eafall.game.events.aperiodic.ingame.building.BuildingsAmountChangedEvent;
import com.yaroslavlancelot.eafall.game.events.aperiodic.ingame.description.BuildingDescriptionShowEvent;
import com.yaroslavlancelot.eafall.game.events.aperiodic.ingame.description.BuildingSettingsPopupShowEvent;
import com.yaroslavlancelot.eafall.game.popup.IPopup;
import com.yaroslavlancelot.eafall.game.popup.PopupScene;
import com.yaroslavlancelot.eafall.game.popup.rolling.IRollingPopup;
import com.yaroslavlancelot.eafall.game.popup.rolling.RollingPopupManager;
import com.yaroslavlancelot.eafall.game.popup.rolling.construction.ConstructionsPopup;
import com.yaroslavlancelot.eafall.game.popup.rolling.description.DescriptionPopup;
import com.yaroslavlancelot.eafall.game.scene.hud.ClientGameHud;
import com.yaroslavlancelot.eafall.game.scene.scenes.EaFallScene;

import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.entity.sprite.ButtonSprite;

import java.util.Set;

import de.greenrobot.event.EventBus;

/**
 * Tutorial mission
 * <br/>
 * It was developed ASAP and contains a lot of code duplication. I didn't have a time to do that
 * in a pretty way. It's already in a public.
 *
 * @author Yaroslav Havrylovych
 */
public class TutorialActivity extends BaseTutorialActivity {
    // ===========================================================
    // Constants
    // ===========================================================

    // ===========================================================
    // Fields
    // ===========================================================
    private boolean mWaitingForBuildingCreation = false;
    private boolean mWaitingForDescriptionUpdate = false;
    private int mCreatedBuildingsAmount = 0;

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
    protected void onPopulateWorkingScene(final EaFallScene scene) {
        super.onPopulateWorkingScene(scene);
        ClientIncomeHandler.getIncomeHandler().registerIncomeListener(new HintableIncomeListener());
    }


    // ===========================================================
    // Methods
    // ===========================================================

    // ===========================================================
    // Inner and Anonymous Classes
    // ===========================================================

    /**
     * 5
     * <p>
     * defence building created
     * <p>
     * and
     * <p>
     * 7
     * building settings
     */
    @SuppressWarnings("Unused")
    public void onEvent(final BuildingsAmountChangedEvent event) {
        if (mWaitingForBuildingCreation) {
            mWaitingForBuildingCreation = false;
        } else {
            return;
        }
        if (mCreatedBuildingsAmount == 0) {
            //arrow
            mClickOnPointPopup.resetTouchToDefault();
            mClickOnPointPopup.initPointer1(790, 465, 180);
            mClickOnPointPopup.initText1(SizeConstants.HALF_FIELD_WIDTH,
                    SizeConstants.DESCRIPTION_POPUP_HEIGHT
                            + 3 * SizeConstants.HUD_ON_SCREEN_TEXT_FONT_SIZE,
                    R.string.tutorial_description_buildings_amount);
            mClickOnPointPopup.setStateChangeListener(new IPopup.StateChangingListener() {
                @Override
                public void onShowed() {
                }

                @Override
                public void onHided() {
                    mClickOnPointPopup.removeStateChangeListener();
                    mClickOnPointPopup.passClickEvent(745, 550, 50, 80);
                    mClickOnPointPopup.initPointer1(750, 745, 90);
                    mClickOnPointPopup.initText1(SizeConstants.HALF_FIELD_WIDTH, 960,
                            R.string.tutorial_description_previous_building);
                    mWaitingForDescriptionUpdate = true;
                    mClickOnPointPopup.showPopup();
                }
            });
        }
        if (mCreatedBuildingsAmount == 1) {
            //settings
            mClickOnPointPopup.resetTouchToDefault();
            mClickOnPointPopup.initPointer1(1300, 420, 90);
            mClickOnPointPopup.initText1(SizeConstants.HALF_FIELD_WIDTH, 900,
                    R.string.tutorial_description_building_settings);
            mClickOnPointPopup.setStateChangeListener(new IPopup.StateChangingListener() {
                @Override
                public void onShowed() {
                }

                @Override
                public void onHided() {
                    mClickOnPointPopup.removeStateChangeListener();
                    Set<Integer> buildings = mFirstPlayer.getPlanet().getExistingBuildingsTypes();
                    int buildingForSettingsIf = Integer.MAX_VALUE;
                    for (int id : buildings) {
                        if (id < buildingForSettingsIf) {
                            buildingForSettingsIf = id;
                        }
                    }
                    EventBus.getDefault().post(new BuildingSettingsPopupShowEvent(
                            (IUnitBuilding) mFirstPlayer.getPlanet()
                                    .getBuilding(buildingForSettingsIf),
                            new DialogInterface.OnDismissListener() {
                                @Override
                                public void onDismiss(final DialogInterface dialog) {
                                    mBuildingSettingsDialog.setOnDismissListener(null);
                                    mClickOnPointPopup.resetTouchToDefault();
                                    mClickOnPointPopup.initText1(SizeConstants.HALF_FIELD_WIDTH,
                                            SizeConstants.HALF_FIELD_HEIGHT + SizeConstants.HALF_FIELD_HEIGHT / 2,
                                            R.string.tutorial_description_hide);
                                    mClickOnPointPopup.setClickableArea(SizeConstants.HALF_FIELD_WIDTH,
                                            SizeConstants.DESCRIPTION_POPUP_HEIGHT +
                                                    (SizeConstants.GAME_FIELD_HEIGHT - SizeConstants.DESCRIPTION_POPUP_HEIGHT) / 2,
                                            SizeConstants.HALF_FIELD_WIDTH / 2,
                                            (SizeConstants.GAME_FIELD_HEIGHT - SizeConstants.DESCRIPTION_POPUP_HEIGHT) / 2);
                                    mClickOnPointPopup.setStateChangeListener(new IPopup.StateChangingListener() {
                                        @Override
                                        public void onShowed() {
                                        }

                                        @Override
                                        public void onHided() {
                                            mClickOnPointPopup.removeStateChangeListener();
                                            IRollingPopup popup = RollingPopupManager.getOpen();
                                            if (popup != null) {
                                                popup.setStateChangeListener(new DescriptionHiddenListener());
                                                popup.triggerPopup();
                                            }
                                        }
                                    });
                                    mClickOnPointPopup.showPopup();
                                }
                            }));
                }
            });
            mClickOnPointPopup.showPopup();
        }
        mCreatedBuildingsAmount++;
    }

    /**
     * 6
     * <p>
     * unit building creation
     */
    public void onEvent(final BuildingDescriptionShowEvent event) {
        if (mWaitingForDescriptionUpdate) {
            mWaitingForDescriptionUpdate = true;
        } else {
            return;
        }
        mClickOnPointPopup.resetTouchToDefault();
        mClickOnPointPopup.initText1(SizeConstants.HALF_FIELD_WIDTH,
                SizeConstants.DESCRIPTION_POPUP_HEIGHT
                        + 3 * SizeConstants.HUD_ON_SCREEN_TEXT_FONT_SIZE,
                R.string.tutorial_description_create_building_2);
        mClickOnPointPopup.initPointer1(1050, 150, 180);
        mClickOnPointPopup.passClickEvent(795, 150,
                150, SizeConstants.DESCRIPTION_POPUP_DES_BUTTON_HEIGHT / 2);
        mWaitingForBuildingCreation = true;
    }

    /**
     * 1
     * <p>
     * first hint to press '+'
     */
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
                                        mSceneManager.getWorkingScene().registerUpdateHandler(
                                                new TimerHandler(.5f, new ShowOxygenValueCallback()));
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

    /**
     * 2
     * <p>
     * shows oxygen
     */
    private class ShowOxygenValueCallback implements ITimerCallback {

        @Override
        public void onTimePassed(final TimerHandler pTimerHandler) {
            mSceneManager.getWorkingScene().unregisterUpdateHandler(pTimerHandler);
            mClickOnPointPopup.initText1(1010, 920, R.string.tutorial_oxygen_value);
            mClickOnPointPopup.initPointer1(365, 1005, 190);
            mClickOnPointPopup.resetTouchToDefault();
            mClickOnPointPopup.showPopup();
            mClickOnPointPopup.setStateChangeListener(new IPopup.StateChangingListener() {
                @Override
                public void onShowed() {
                }

                @Override
                public void onHided() {
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
                            hud.blockInput(true);
                            pause(false);
                            final ConstructionsPopup constructionsPopup = (ConstructionsPopup)
                                    RollingPopupManager.getInstance().getPopup(ConstructionsPopup.KEY);
                            constructionsPopup.removeTouch();
                            constructionsPopup.setStateChangeListener(
                                    new ConstructionsPopupCallback(constructionsPopup));
                        }
                    });
                    mClickOnPointPopup.showPopup();
                }
            });
            pause(true);
        }
    }

    /**
     * 3
     * <p>
     * shows hint on constructions
     */
    private class ConstructionsPopupCallback implements PopupScene.StateChangingListener {
        private ConstructionsPopup mConstructionsPopup;

        private ConstructionsPopupCallback(ConstructionsPopup constructionsPopup) {
            mConstructionsPopup = constructionsPopup;
        }

        @Override
        public void onShowed() {
            pause(true);
            mConstructionsPopup.resetTouchToDefault();
            mClickOnPointPopup.setScene(mConstructionsPopup);
            mClickOnPointPopup.initText1(SizeConstants.HALF_FIELD_WIDTH, 986, R.string.tutorial_construction_popup);
            mClickOnPointPopup.initPointer1(417, 1000, 90);
            mClickOnPointPopup.initPointer2(1518, 1000, 90);
            mClickOnPointPopup.resetTouchToDefault();
            mClickOnPointPopup.setStateChangeListener(new IPopup.StateChangingListener() {
                @Override
                public void onShowed() {
                }

                @Override
                public void onHided() {
                    mClickOnPointPopup.removeStateChangeListener();
                    mClickOnPointPopup.initText1(SizeConstants.HALF_FIELD_WIDTH, 986, R.string.tutorial_constructions_defence_building);
                    float y = SizeConstants.CONSTRUCTIONS_POPUP_FIRST_ROW_Y +
                            2 * SizeConstants.CONSTRUCTIONS_POPUP_ELEMENT_HEIGHT;
                    mClickOnPointPopup.initPointer1(815, y, 0);
                    mClickOnPointPopup.passClickEvent(
                            SizeConstants.HALF_FIELD_WIDTH
                                    + SizeConstants.CONSTRUCTIONS_POPUP_ELEMENT_WIDTH / 2,
                            y,
                            SizeConstants.CONSTRUCTIONS_POPUP_ELEMENT_WIDTH / 2,
                            SizeConstants.CONSTRUCTIONS_POPUP_ELEMENT_HEIGHT / 2);
                    DescriptionPopup descriptionPopup = (DescriptionPopup)
                            RollingPopupManager.getInstance().getPopup(DescriptionPopup.KEY);
                    descriptionPopup.removeTouch();
                    descriptionPopup.setStateChangeListener(
                            new DescriptionDefenceBuildingListener(descriptionPopup));
                    mClickOnPointPopup.showPopup();
                }
            });
            mClickOnPointPopup.showPopup();
            pause(true);
        }

        @Override
        public void onHided() {
            mConstructionsPopup.removeStateChangeListener();
            mClickOnPointPopup.hidePopup();
        }
    }

    /**
     * 4
     * <p>
     * Before defence building creation
     */
    private class DescriptionDefenceBuildingListener implements IPopup.StateChangingListener {
        private DescriptionPopup mDescriptionPopup;

        private DescriptionDefenceBuildingListener(DescriptionPopup descriptionPopup) {
            mDescriptionPopup = descriptionPopup;
        }

        @Override
        public void onShowed() {
            mDescriptionPopup.removeStateChangeListener();
            mDescriptionPopup.resetTouchToDefault();
            mClickOnPointPopup.resetTouchToDefault();
            mClickOnPointPopup.setScene(mDescriptionPopup);
            mClickOnPointPopup.initText1(SizeConstants.HALF_FIELD_WIDTH,
                    SizeConstants.GAME_FIELD_HEIGHT - SizeConstants.HALF_FIELD_HEIGHT / 3,
                    R.string.tutorial_description_object_characteristics);
            mClickOnPointPopup.initPointer1(700, 625, 90);
            mClickOnPointPopup.setStateChangeListener(new IPopup.StateChangingListener() {
                @Override
                public void onShowed() {
                }

                @Override
                public void onHided() {
                    mClickOnPointPopup.removeStateChangeListener();
                    mClickOnPointPopup.initText1(SizeConstants.HALF_FIELD_WIDTH,
                            SizeConstants.GAME_FIELD_HEIGHT - SizeConstants.HALF_FIELD_HEIGHT / 2,
                            R.string.tutorial_description_object_unit);
                    mClickOnPointPopup.initPointer1(1572, 625, 90);
                    mClickOnPointPopup.initPointer2(980, 156, 270);
                    mClickOnPointPopup.setStateChangeListener(new IPopup.StateChangingListener() {
                        @Override
                        public void onShowed() {
                        }

                        @Override
                        public void onHided() {
                            mClickOnPointPopup.removeStateChangeListener();
                            mClickOnPointPopup.initText1(SizeConstants.HALF_FIELD_WIDTH,
                                    SizeConstants.DESCRIPTION_POPUP_HEIGHT
                                            + 3 * SizeConstants.HUD_ON_SCREEN_TEXT_FONT_SIZE,
                                    R.string.tutorial_description_create_building);
                            mClickOnPointPopup.initPointer1(1050, 150, 180);
                            mClickOnPointPopup.passClickEvent(795, 150,
                                    150, SizeConstants.DESCRIPTION_POPUP_DES_BUTTON_HEIGHT / 2);
                            mWaitingForBuildingCreation = true;
                            mClickOnPointPopup.showPopup();

                        }
                    });
                    mClickOnPointPopup.showPopup();
                }
            });
            mClickOnPointPopup.showPopup();
        }

        @Override
        public void onHided() {

        }
    }


    /**
     * 8
     * <p>
     * shows final messages before an end of the tutorial
     */
    private class DescriptionHiddenListener implements IPopup.StateChangingListener {
        @Override
        public void onShowed() {
        }

        @Override
        public void onHided() {
            (RollingPopupManager.getInstance().getPopup(DescriptionPopup.KEY)).removeStateChangeListener();
            mClickOnPointPopup.resetTouchToDefault();
            mClickOnPointPopup.removeStateChangeListener();
            mClickOnPointPopup.setScene(mHud);
            mClickOnPointPopup.initText1(SizeConstants.HALF_FIELD_WIDTH,
                    SizeConstants.HALF_FIELD_HEIGHT + SizeConstants.HALF_FIELD_HEIGHT / 2,
                    R.string.tutorial_description_suppressor_usage);
            mClickOnPointPopup.initPointer1(475, SizeConstants.HALF_FIELD_HEIGHT, 180);
            mClickOnPointPopup.setStateChangeListener(new IPopup.StateChangingListener() {
                @Override
                public void onShowed() {
                }

                @Override
                public void onHided() {
                    mClickOnPointPopup.removeStateChangeListener();
                    mClickOnPointPopup.initText1(SizeConstants.HALF_FIELD_WIDTH,
                            SizeConstants.HALF_FIELD_HEIGHT + SizeConstants.HALF_FIELD_HEIGHT / 2,
                            R.string.tutorial_description_suppressor_description);
                    mClickOnPointPopup.initPointer1(475, SizeConstants.HALF_FIELD_HEIGHT, 180);
                    mClickOnPointPopup.setStateChangeListener(new IPopup.StateChangingListener() {
                        @Override
                        public void onShowed() {
                        }

                        @Override
                        public void onHided() {
                            mClickOnPointPopup.removeStateChangeListener();
                            mClickOnPointPopup.initText1(SizeConstants.HALF_FIELD_WIDTH,
                                    SizeConstants.HALF_FIELD_HEIGHT + SizeConstants.HALF_FIELD_HEIGHT / 2,
                                    R.string.tutorial_task);
                            mClickOnPointPopup.initPointer1(65, 665, 270);
                            mClickOnPointPopup.setStateChangeListener(new IPopup.StateChangingListener() {
                                @Override
                                public void onShowed() {
                                }

                                @Override
                                public void onHided() {
                                    pause(false);
                                    ((ClientGameHud) mHud).blockInput(false);
                                }
                            });
                            mClickOnPointPopup.showPopup();
                        }
                    });
                    mClickOnPointPopup.showPopup();
                }
            });
            mClickOnPointPopup.showPopup();
        }
    }
}
