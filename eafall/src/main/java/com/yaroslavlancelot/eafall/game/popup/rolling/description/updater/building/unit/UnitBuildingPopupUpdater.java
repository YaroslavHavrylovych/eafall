package com.yaroslavlancelot.eafall.game.popup.rolling.description.updater.building.unit;

import com.yaroslavlancelot.eafall.R;
import com.yaroslavlancelot.eafall.game.alliance.AllianceHolder;
import com.yaroslavlancelot.eafall.game.alliance.IAlliance;
import com.yaroslavlancelot.eafall.game.constant.SizeConstants;
import com.yaroslavlancelot.eafall.game.entity.gameobject.building.BuildingId;
import com.yaroslavlancelot.eafall.game.entity.gameobject.building.IBuilding;
import com.yaroslavlancelot.eafall.game.entity.gameobject.building.dummy.BuildingDummy;
import com.yaroslavlancelot.eafall.game.entity.gameobject.building.dummy.UnitBuildingDummy;
import com.yaroslavlancelot.eafall.game.events.aperiodic.ingame.building.BuildingsAmountChangedEvent;
import com.yaroslavlancelot.eafall.game.events.aperiodic.ingame.building.CreateBuildingEvent;
import com.yaroslavlancelot.eafall.game.events.aperiodic.ingame.building.UpgradeBuildingEvent;
import com.yaroslavlancelot.eafall.game.events.aperiodic.ingame.description.BuildingDescriptionShowEvent;
import com.yaroslavlancelot.eafall.game.events.aperiodic.ingame.description.UnitByBuildingDescriptionShowEvent;
import com.yaroslavlancelot.eafall.game.player.IPlayer;
import com.yaroslavlancelot.eafall.game.player.PlayersHolder;
import com.yaroslavlancelot.eafall.game.popup.rolling.description.updater.building.BaseBuildingPopupUpdater;
import com.yaroslavlancelot.eafall.game.touch.TouchHelper;
import com.yaroslavlancelot.eafall.game.visual.buttons.TextButton;
import com.yaroslavlancelot.eafall.general.locale.LocaleImpl;

import org.andengine.entity.scene.Scene;
import org.andengine.entity.shape.Shape;
import org.andengine.entity.sprite.ButtonSprite;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import de.greenrobot.event.EventBus;

/**
 * For CREEP building :
 * <br/>
 * updates buildings description popup
 */
public class UnitBuildingPopupUpdater extends BaseBuildingPopupUpdater {
    /** press to upgrade building (if such exist) */
    protected TextButton mUpgradeButton;

    public UnitBuildingPopupUpdater(VertexBufferObjectManager vertexBufferObjectManager, Scene scene) {
        super(vertexBufferObjectManager, scene);
        initButtons(vertexBufferObjectManager);
    }

    @Override
    public void clear() {
        super.clear();
        mUpgradeButton.detachSelf();
    }

    @Override
    public void updateDescription(Shape drawArea, Object objectId, String allianceName, final String playerName) {
        super.updateDescription(drawArea, objectId, allianceName, playerName);
        IAlliance alliance = AllianceHolder.getAlliance(allianceName);
        final BuildingId buildingId = (BuildingId) objectId;
        //init base button (it can hold build or back operation)
        final IPlayer player = PlayersHolder.getPlayer(playerName);
        IBuilding building = player.getPlanet().getBuilding(buildingId.getId());
        BuildingDummy buildingDummy = alliance.getBuildingDummy(buildingId);
        boolean upgradeShowed =
                //building not created
                (building == null && buildingId.getUpgrade() > 0)
                        //user looking the upgrade, this upgraded building not exist
                        || (building != null && buildingId.getUpgrade() > building.getUpgrade());
        final Object event;
        if (upgradeShowed) {
            mBaseButton.setText(LocaleImpl.getInstance().getStringById(R.string.description_back_button));
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
        mBaseButton.setOnClickListener(new ButtonSprite.OnClickListener() {
            @Override
            public void onClick(ButtonSprite pButtonSprite, float pTouchAreaLocalX, float pTouchAreaLocalY) {
                if (event != null) {
                    EventBus.getDefault().post(event);
                }
            }
        });
        //button upgrade
        boolean isUpgradeButtonVisible = building != null
                && alliance.isUpgradeAvailable(buildingId)
                && !upgradeShowed;
        //check if upgrades available
        if (isUpgradeButtonVisible) {
            mUpgradeButton.setPosition(mBaseButton.getX() + mBaseButton.getWidth() / 2
                    + BUTTON_MARGIN + mUpgradeButton.getWidth() / 2, mBaseButton.getY());
            if (!mUpgradeButton.hasParent()) {
                drawArea.attachChild(mUpgradeButton);
            }
            mUpgradeButton.setOnClickListener(new ButtonSprite.OnClickListener() {
                @Override
                public void onClick(ButtonSprite pButtonSprite, float pTouchAreaLocalX, float pTouchAreaLocalY) {
                    EventBus.getDefault().post(new UpgradeBuildingEvent(mPlayerName, buildingId));
                }
            });
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
        if (visible() && checkPlayer(buildingsAmountChangedEvent.getPlayerName())) {
            super.onEvent(buildingsAmountChangedEvent);
            String playerName = buildingsAmountChangedEvent.getPlayerName();
            String allianceName = PlayersHolder.getPlayer(playerName).getAlliance().getAllianceName();
            BuildingId buildingId = buildingsAmountChangedEvent.getBuildingId();
            updateDescription((Shape) mBaseButton.getParent(), buildingId, allianceName, playerName);
        }
    }

    protected void initButtons(VertexBufferObjectManager vertexBufferObjectManager) {
        //upgrade
        mUpgradeButton = new TextButton(vertexBufferObjectManager, 300,
                SizeConstants.DESCRIPTION_POPUP_DES_BUTTON_HEIGHT);
        mUpgradeButton.setText(LocaleImpl.getInstance().getStringById(R.string.description_upgrade_button));
    }

    protected ITextureRegion getAdditionalInformationImage(Object objectId, String allianceName) {
        IAlliance alliance = AllianceHolder.getAlliance(allianceName);
        BuildingId buildingId = (BuildingId) objectId;
        UnitBuildingDummy dummy = (UnitBuildingDummy) alliance.getBuildingDummy(buildingId);
        final int unitId = dummy.getUnitId(buildingId.getUpgrade());
        return alliance.getUnitDummy(unitId).getImageTextureRegion();
    }
}
