package com.gmail.yaroslavlancelot.eafall.game.popup.rolling.description.updater.building.unit.offence;

import com.gmail.yaroslavlancelot.eafall.R;
import com.gmail.yaroslavlancelot.eafall.game.constant.SizeConstants;
import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.building.BuildingId;
import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.building.buildings.IUnitBuilding;
import com.gmail.yaroslavlancelot.eafall.game.events.aperiodic.ingame.building.BuildingsAmountChangedEvent;
import com.gmail.yaroslavlancelot.eafall.game.player.IPlayer;
import com.gmail.yaroslavlancelot.eafall.game.player.PlayersHolder;
import com.gmail.yaroslavlancelot.eafall.game.popup.rolling.description.updater.building.unit.UnitBuildingPopupUpdater;
import com.gmail.yaroslavlancelot.eafall.game.visual.buttons.TextButton;
import com.gmail.yaroslavlancelot.eafall.general.locale.LocaleImpl;

import org.andengine.entity.scene.Scene;
import org.andengine.entity.shape.Shape;
import org.andengine.entity.sprite.ButtonSprite;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.adt.color.Color;

/**
 * For CREEP building :
 * <br/>
 * updates buildings description popup
 */
public class OffenceBuildingPopupUpdater extends UnitBuildingPopupUpdater {
    /** press to set units path for the current building */
    private TextButton mPathButton;

    public OffenceBuildingPopupUpdater(VertexBufferObjectManager vertexBufferObjectManager, Scene scene) {
        super(vertexBufferObjectManager, scene);
        mPathButton = new TextButton(vertexBufferObjectManager, 300,
                SizeConstants.DESCRIPTION_POPUP_DES_BUTTON_HEIGHT);
        mPathButton.setFixedSize(true);
        mDescriptionAreaUpdater = new DescriptionAreaUpdater(vertexBufferObjectManager, scene);
    }

    @Override
    public void clear() {
        super.clear();
        mPathButton.detachSelf();
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
            mPathButton.setVisible(false);
        } else {
            ButtonSprite button = mUpgradeButton.getParent() != null ? mUpgradeButton : mBaseButton;
            mPathButton.setPosition(button.getX() + button.getWidth() / 2 + BUTTON_MARGIN
                    + mPathButton.getWidth() / 2, button.getY());
            setPathText(mPathButton, building);
            drawArea.attachChild(mPathButton);
            mPathButton.setOnClickListener(new UnitPathClickListener(building));
            mPathButton.setVisible(true);
        }
    }

    @Override
    public void onEvent(final BuildingsAmountChangedEvent buildingsAmountChangedEvent) {
        super.onEvent(buildingsAmountChangedEvent);
        if (mDescriptionAreaUpdater instanceof DescriptionAreaUpdater) {
            ((DescriptionAreaUpdater)
                    mDescriptionAreaUpdater).updateUpgradeCost(buildingsAmountChangedEvent.getBuildingId(), buildingsAmountChangedEvent.getPlayerName());
        }
        mPathButton.setVisible(true);
    }

    private void setPathText(TextButton button, IUnitBuilding unitBuilding) {
        String text;
        if (unitBuilding.isPaused()) {
            text = LocaleImpl.getInstance().getStringById(R.string.description_path_button_pause);
            button.setTextColor(Color.RED);
        } else if (unitBuilding.isTopPath()) {
            text = LocaleImpl.getInstance().getStringById(R.string.description_path_button_top);
            button.setTextColor(Color.GREEN);
        } else {
            text = LocaleImpl.getInstance().getStringById(R.string.description_path_button_bottom);
            button.setTextColor(Color.GREEN);
        }
        button.setText(text);
    }

    private class UnitPathClickListener implements ButtonSprite.OnClickListener {
        private final IUnitBuilding mUnitBuildings;

        UnitPathClickListener(IUnitBuilding unitBuilding) {
            mUnitBuildings = unitBuilding;
        }

        @Override
        public void onClick(final ButtonSprite pButtonSprite, final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
            if (mUnitBuildings.isPaused()) {
                mUnitBuildings.setPath(true);
                mUnitBuildings.unPause();
            } else if (mUnitBuildings.isTopPath()) {
                mUnitBuildings.setPath(false);
            } else {
                mUnitBuildings.pause();
            }
            setPathText((TextButton) pButtonSprite, mUnitBuildings);
        }
    }
}
