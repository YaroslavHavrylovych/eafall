package com.yaroslavlancelot.eafall.game.popup.rolling.description.updater.building.unit.offence;

import com.yaroslavlancelot.eafall.R;
import com.yaroslavlancelot.eafall.game.constant.SizeConstants;
import com.yaroslavlancelot.eafall.game.entity.gameobject.building.BuildingId;
import com.yaroslavlancelot.eafall.game.entity.gameobject.building.buildings.IUnitBuilding;
import com.yaroslavlancelot.eafall.game.events.aperiodic.ingame.description.BuildingSettingsPopupShowEvent;
import com.yaroslavlancelot.eafall.game.player.IPlayer;
import com.yaroslavlancelot.eafall.game.player.PlayersHolder;
import com.yaroslavlancelot.eafall.game.popup.rolling.description.updater.building.unit.UnitBuildingPopupUpdater;
import com.yaroslavlancelot.eafall.game.visual.buttons.TextButton;

import org.andengine.entity.scene.Scene;
import org.andengine.entity.shape.Shape;
import org.andengine.entity.sprite.ButtonSprite;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import de.greenrobot.event.EventBus;

/**
 * For CREEP building :
 * <br/>
 * updates buildings description popup
 */
public class OffenceBuildingPopupUpdater extends UnitBuildingPopupUpdater {
    /** press to set units path for the current building */
    private TextButton mBuildingSettingsButton;
    /** buildings settings button click listener */
    private BuildingSettingsClickListener mBuildingSettingsListener;

    public OffenceBuildingPopupUpdater(VertexBufferObjectManager vertexBufferObjectManager, Scene scene) {
        super(vertexBufferObjectManager, scene);
        mBuildingSettingsButton = new TextButton(vertexBufferObjectManager, 300,
                SizeConstants.DESCRIPTION_POPUP_DES_BUTTON_HEIGHT);
        mBuildingSettingsButton.setFixedSize(true);
        mBuildingSettingsButton.setText(R.string.description_settings_button);
        mBuildingSettingsListener = new BuildingSettingsClickListener();
        mBuildingSettingsButton.setOnClickListener(mBuildingSettingsListener);
        mDescriptionAreaUpdater = new OffenceBuildingDescriptionAreaUpdater(vertexBufferObjectManager, scene);
    }

    @Override
    public void clear() {
        super.clear();
        mBuildingSettingsButton.detachSelf();
    }

    @Override
    public void updateDescription(Shape drawArea, Object objectId, String allianceName, final String playerName) {
        super.updateDescription(drawArea, objectId, allianceName, playerName);
        final BuildingId buildingId = (BuildingId) objectId;
        IPlayer player = PlayersHolder.getPlayer(playerName);
        IUnitBuilding building = (IUnitBuilding) player.getPlanet().getBuilding(buildingId.getId());
        boolean upgradeShowed =
                //building not created
                (building == null && buildingId.getUpgrade() > 0)
                        //user looking the upgrade, this upgraded building not exist
                        || (building != null && buildingId.getUpgrade() > building.getUpgrade());
        if (building == null || upgradeShowed) {
            mBuildingSettingsButton.setVisible(false);
        } else {
            ButtonSprite button = mUpgradeButton.getParent() != null ? mUpgradeButton : mBaseButton;
            mBuildingSettingsButton.setPosition(button.getX() + button.getWidth() / 2 + BUTTON_MARGIN
                    + mBuildingSettingsButton.getWidth() / 2, button.getY());
            if (!mBuildingSettingsButton.hasParent()) {
                drawArea.attachChild(mBuildingSettingsButton);
            }
            mBuildingSettingsListener.setUnitBuilding(building);
            mBuildingSettingsButton.setVisible(true);

        }
    }

    private class BuildingSettingsClickListener implements ButtonSprite.OnClickListener {
        private IUnitBuilding mUnitBuilding;

        private BuildingSettingsClickListener() {
        }

        private void setUnitBuilding(IUnitBuilding unitBuilding) {
            mUnitBuilding = unitBuilding;
        }

        @Override
        public void onClick(final ButtonSprite pButtonSprite, final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
            EventBus.getDefault().post(new BuildingSettingsPopupShowEvent(mUnitBuilding));
        }
    }
}
