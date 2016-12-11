package com.yaroslavlancelot.eafall.game.campaign.missions;

import com.yaroslavlancelot.eafall.R;
import com.yaroslavlancelot.eafall.game.BaseTutorialActivity;
import com.yaroslavlancelot.eafall.game.constant.SizeConstants;
import com.yaroslavlancelot.eafall.game.entity.gameobject.building.buildings.IUnitBuilding;
import com.yaroslavlancelot.eafall.game.events.aperiodic.ingame.building.BuildingsAmountChangedEvent;
import com.yaroslavlancelot.eafall.game.events.aperiodic.ingame.description.BuildingSettingsPopupShowEvent;
import com.yaroslavlancelot.eafall.game.player.PlayersHolder;
import com.yaroslavlancelot.eafall.game.popup.IPopup;
import com.yaroslavlancelot.eafall.game.popup.rolling.RollingPopupManager;
import com.yaroslavlancelot.eafall.game.popup.rolling.description.DescriptionPopup;

import de.greenrobot.event.EventBus;

/** First mission. Limited units amount and no supressor. Only few hints to build. */
public class SecondMissionActivity extends BaseTutorialActivity {
    private int mCreatedBuildingsAmount;

    @SuppressWarnings("Unused")
    public void onEvent(final BuildingsAmountChangedEvent event) {
        if (PlayersHolder.getPlayer(event.getPlayerName()).getControlType().bot()) {
            return;
        }
        if (mCreatedBuildingsAmount == 0) {
            //settings
            mClickOnPointPopup.setScene((DescriptionPopup) RollingPopupManager.getInstance()
                    .getPopup(DescriptionPopup.KEY));
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
                    mClickOnPointPopup.setScene(mHud);
                    mClickOnPointPopup.removeStateChangeListener();
                    EventBus.getDefault().post(new BuildingSettingsPopupShowEvent((IUnitBuilding)
                            mFirstPlayer.getPlanet().getBuilding(event.getBuildingId().getId())));
                }
            });
            mClickOnPointPopup.showPopup();
        }
        mCreatedBuildingsAmount++;
    }
}
