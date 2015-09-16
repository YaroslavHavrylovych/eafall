package com.gmail.yaroslavlancelot.eafall.game.popup.rolling.description.updater.building.creep;

import com.gmail.yaroslavlancelot.eafall.R;
import com.gmail.yaroslavlancelot.eafall.game.alliance.AllianceHolder;
import com.gmail.yaroslavlancelot.eafall.game.alliance.IAlliance;
import com.gmail.yaroslavlancelot.eafall.game.constant.SizeConstants;
import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.building.BuildingId;
import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.building.buildings.ICreepBuilding;
import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.building.dummy.BuildingDummy;
import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.building.dummy.CreepBuildingDummy;
import com.gmail.yaroslavlancelot.eafall.game.events.aperiodic.ingame.building.BuildingsAmountChangedEvent;
import com.gmail.yaroslavlancelot.eafall.game.events.aperiodic.ingame.building.CreateBuildingEvent;
import com.gmail.yaroslavlancelot.eafall.game.events.aperiodic.ingame.building.UpgradeBuildingEvent;
import com.gmail.yaroslavlancelot.eafall.game.events.aperiodic.ingame.description.BuildingDescriptionShowEvent;
import com.gmail.yaroslavlancelot.eafall.game.events.aperiodic.ingame.description.UnitByBuildingDescriptionShowEvent;
import com.gmail.yaroslavlancelot.eafall.game.player.IPlayer;
import com.gmail.yaroslavlancelot.eafall.game.player.PlayersHolder;
import com.gmail.yaroslavlancelot.eafall.game.popup.rolling.description.updater.building.BaseBuildingPopupUpdater;
import com.gmail.yaroslavlancelot.eafall.game.touch.TouchHelper;
import com.gmail.yaroslavlancelot.eafall.game.visual.buttons.TextButton;
import com.gmail.yaroslavlancelot.eafall.general.locale.LocaleImpl;

import org.andengine.entity.scene.Scene;
import org.andengine.entity.shape.Shape;
import org.andengine.entity.sprite.ButtonSprite;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.adt.color.Color;

import de.greenrobot.event.EventBus;

/**
 * For CREEP building :
 * <br/>
 * updates buildings description popup
 */
public class CreepBuildingPopupUpdater extends BaseBuildingPopupUpdater {
    /** press to upgrade building (if such exist) */
    private TextButton mSecondButton;
    /** press to set units path for the current building */
    private TextButton mThirdButton;

    public CreepBuildingPopupUpdater(VertexBufferObjectManager vertexBufferObjectManager, Scene scene) {
        super(vertexBufferObjectManager, scene);
        mDescriptionAreaUpdater = new DescriptionAreaUpdater(vertexBufferObjectManager, scene);
        initButtons(vertexBufferObjectManager);
    }

    @Override
    public void clear() {
        super.clear();
        mSecondButton.detachSelf();
        mThirdButton.detachSelf();
    }

    @Override
    public void updateDescription(Shape drawArea, Object objectId, String allianceName, final String playerName) {
        super.updateDescription(drawArea, objectId, allianceName, playerName);
        IAlliance alliance = AllianceHolder.getAlliance(allianceName);
        final BuildingId buildingId = (BuildingId) objectId;
        //button or back build
        IPlayer player = PlayersHolder.getPlayer(playerName);
        ICreepBuilding building = (ICreepBuilding) player.getPlanet().getBuilding(buildingId.getId());
        BuildingDummy buildingDummy = alliance.getBuildingDummy(buildingId);
        boolean upgradeShowed =
                //building not created
                (building == null && buildingId.getUpgrade() > 0)
                        //user looking the upgrade, this upgraded building not exist
                        || (building != null && buildingId.getUpgrade() > building.getUpgrade());
        final Object event;
        if (upgradeShowed) {
            mFirstButton.setText(LocaleImpl.getInstance().getStringById(R.string.description_back_button));
            event = new BuildingDescriptionShowEvent(
                    BuildingId.makeId(buildingId.getId(), buildingId.getUpgrade() - 1), playerName);
        } else {
            if (player.getPlanet().getBuildingsAmount(buildingId.getId())
                    >= buildingDummy.getAmountLimit()) {
                event = null;
            } else {
                event = new CreateBuildingEvent(mPlayerName, buildingId);
            }
        }
        mFirstButton.setOnClickListener(new ButtonSprite.OnClickListener() {
            @Override
            public void onClick(ButtonSprite pButtonSprite, float pTouchAreaLocalX, float pTouchAreaLocalY) {
                if (event != null) {
                    EventBus.getDefault().post(event);
                }
            }
        });
        //button upgrade
        boolean isSecondButtonVisible = building != null
                && alliance.isUpgradeAvailable(buildingId)
                && !upgradeShowed;
        //check if upgrades available
        if (isSecondButtonVisible) {
            mSecondButton.setPosition(mFirstButton.getX() + mFirstButton.getWidth() / 2
                    + BUTTON_MARGIN + mSecondButton.getWidth() / 2, mFirstButton.getY());
            drawArea.attachChild(mSecondButton);
            mSecondButton.setOnClickListener(new ButtonSprite.OnClickListener() {
                @Override
                public void onClick(ButtonSprite pButtonSprite, float pTouchAreaLocalX, float pTouchAreaLocalY) {
                    EventBus.getDefault().post(new UpgradeBuildingEvent(mPlayerName, buildingId));
                }
            });
        }
        //button path
        if (building == null || upgradeShowed) {
            mThirdButton.setVisible(false);
        } else {
            ButtonSprite button = isSecondButtonVisible ? mSecondButton : mFirstButton;
            mThirdButton.setPosition(button.getX() + button.getWidth() / 2 + BUTTON_MARGIN
                    + mThirdButton.getWidth() / 2, button.getY());
            setPathText(mThirdButton, building);
            drawArea.attachChild(mThirdButton);
            mThirdButton.setOnClickListener(new CreepPathClickListener(building));
            mThirdButton.setVisible(true);
        }
    }

    @Override
    public void updateAdditionInfo(Shape drawArea, Object objectId, String allianceName, final String playerName) {
        super.updateAdditionInfo(drawArea, objectId, allianceName, playerName);

        ITextureRegion textureRegion = getAdditionalInformationImage(objectId, allianceName);
        mAdditionDescriptionImage = drawInArea(drawArea, textureRegion);

        final BuildingId buildingId = (BuildingId) objectId;
        mAdditionDescriptionImage.setTouchCallback(
                new TouchHelper.EntityCustomTouch(mAdditionDescriptionImage) {
                    @Override
                    public void click() {
                        super.click();
                        EventBus.getDefault().post(new UnitByBuildingDescriptionShowEvent(buildingId, playerName));
                    }
                });
    }

    @Override
    public void onEvent(final BuildingsAmountChangedEvent buildingsAmountChangedEvent) {
        super.onEvent(buildingsAmountChangedEvent);
        if (mDescriptionAreaUpdater instanceof com.gmail.yaroslavlancelot.eafall.game.popup.rolling.description.updater.building.creep.DescriptionAreaUpdater) {
            ((com.gmail.yaroslavlancelot.eafall.game.popup.rolling.description.updater.building.creep.DescriptionAreaUpdater)
                    mDescriptionAreaUpdater).updateUpgradeCost(buildingsAmountChangedEvent.getBuildingId(), buildingsAmountChangedEvent.getPlayerName());
        }
        mThirdButton.setVisible(true);
    }

    private void initButtons(VertexBufferObjectManager vertexBufferObjectManager) {
        //upgrade
        mSecondButton = new TextButton(vertexBufferObjectManager, 300,
                SizeConstants.DESCRIPTION_POPUP_DES_BUTTON_HEIGHT);
        mSecondButton.setText(LocaleImpl.getInstance().getStringById(R.string.description_upgrade_button));
        //path button
        mThirdButton = new TextButton(vertexBufferObjectManager, 300,
                SizeConstants.DESCRIPTION_POPUP_DES_BUTTON_HEIGHT);
        mThirdButton.setFixedSize(true);
    }

    protected ITextureRegion getAdditionalInformationImage(Object objectId, String allianceName) {
        IAlliance alliance = AllianceHolder.getAlliance(allianceName);
        BuildingId buildingId = (BuildingId) objectId;
        CreepBuildingDummy dummy = (CreepBuildingDummy) alliance.getBuildingDummy(buildingId);
        final int unitId = dummy.getMovableUnitId(buildingId.getUpgrade());
        return alliance.getUnitDummy(unitId).getImageTextureRegion();
    }

    private void setPathText(TextButton button, ICreepBuilding creepBuilding) {
        String text;
        if (creepBuilding.isPaused()) {
            text = LocaleImpl.getInstance().getStringById(R.string.description_path_button_pause);
            button.setTextColor(Color.RED);
        } else if (creepBuilding.isTopPath()) {
            text = LocaleImpl.getInstance().getStringById(R.string.description_path_button_top);
            button.setTextColor(Color.GREEN);
        } else {
            text = LocaleImpl.getInstance().getStringById(R.string.description_path_button_bottom);
            button.setTextColor(Color.GREEN);
        }
        button.setText(text);
    }

    private class CreepPathClickListener implements ButtonSprite.OnClickListener {
        private final ICreepBuilding mCreepBuildings;

        CreepPathClickListener(ICreepBuilding creepBuilding) {
            mCreepBuildings = creepBuilding;
        }

        @Override
        public void onClick(final ButtonSprite pButtonSprite, final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
            if (mCreepBuildings.isPaused()) {
                mCreepBuildings.setPath(true);
                mCreepBuildings.unPause();
            } else if (mCreepBuildings.isTopPath()) {
                mCreepBuildings.setPath(false);
            } else {
                mCreepBuildings.pause();
            }
            setPathText((TextButton) pButtonSprite, mCreepBuildings);
        }
    }
}
