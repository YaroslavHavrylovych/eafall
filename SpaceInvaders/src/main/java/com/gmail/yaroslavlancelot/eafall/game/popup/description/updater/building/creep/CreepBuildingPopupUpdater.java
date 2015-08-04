package com.gmail.yaroslavlancelot.eafall.game.popup.description.updater.building.creep;

import com.gmail.yaroslavlancelot.eafall.R;
import com.gmail.yaroslavlancelot.eafall.game.alliance.AllianceHolder;
import com.gmail.yaroslavlancelot.eafall.game.alliance.IAlliance;
import com.gmail.yaroslavlancelot.eafall.game.constant.SizeConstants;
import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.building.BuildingId;
import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.building.IBuilding;
import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.building.dummy.BuildingDummy;
import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.building.dummy.CreepBuildingDummy;
import com.gmail.yaroslavlancelot.eafall.game.events.aperiodic.ingame.building.BuildingsAmountChangedEvent;
import com.gmail.yaroslavlancelot.eafall.game.events.aperiodic.ingame.building.CreateBuildingEvent;
import com.gmail.yaroslavlancelot.eafall.game.events.aperiodic.ingame.building.UpgradeBuildingEvent;
import com.gmail.yaroslavlancelot.eafall.game.events.aperiodic.ingame.description.BuildingDescriptionShowEvent;
import com.gmail.yaroslavlancelot.eafall.game.events.aperiodic.ingame.description.UnitByBuildingDescriptionShowEvent;
import com.gmail.yaroslavlancelot.eafall.game.events.aperiodic.ingame.path.ShowUnitPathChooser;
import com.gmail.yaroslavlancelot.eafall.game.player.IPlayer;
import com.gmail.yaroslavlancelot.eafall.game.player.PlayersHolder;
import com.gmail.yaroslavlancelot.eafall.game.popup.description.updater.building.BaseBuildingPopupUpdater;
import com.gmail.yaroslavlancelot.eafall.game.touch.StaticHelper;
import com.gmail.yaroslavlancelot.eafall.game.visual.buttons.TextButton;
import com.gmail.yaroslavlancelot.eafall.general.locale.LocaleImpl;

import org.andengine.entity.scene.Scene;
import org.andengine.entity.shape.Shape;
import org.andengine.entity.sprite.ButtonSprite;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import de.greenrobot.event.EventBus;

/**
 * For CREEP building : <br/>
 * updates buildings description popup
 */
public class CreepBuildingPopupUpdater extends BaseBuildingPopupUpdater {
    /** press to upgrade building (if such exist) */
    private TextButton mUpgradeButton;
    /** press to set units path for the current building */
    private TextButton mPathButton;

    public CreepBuildingPopupUpdater(VertexBufferObjectManager vertexBufferObjectManager, Scene scene) {
        super(vertexBufferObjectManager, scene);
        mDescriptionAreaUpdater = new DescriptionAreaUpdater(vertexBufferObjectManager, scene);
        initButtons(vertexBufferObjectManager);
    }

    @Override
    public void updateAdditionInfo(Shape drawArea, Object objectId, String allianceName, final String playerName) {
        super.updateAdditionInfo(drawArea, objectId, allianceName, playerName);

        ITextureRegion textureRegion = getAdditionalInformationImage(objectId, allianceName);
        mAdditionDescriptionImage = drawInArea(drawArea, textureRegion);

        final BuildingId buildingId = (BuildingId) objectId;
        mAdditionDescriptionImage.setTouchCallback(
                new StaticHelper.CustomTouchListener(mAdditionDescriptionImage) {
                    @Override
                    public void click() {
                        super.click();
                        EventBus.getDefault().post(new UnitByBuildingDescriptionShowEvent(buildingId, playerName));
                    }
                });
        mScene.registerTouchArea(mAdditionDescriptionImage);
    }

    @Override
    public void clear() {
        super.clear();
        mUpgradeButton.detachSelf();
        mPathButton.detachSelf();
    }

    @Override
    public void updateDescription(Shape drawArea, Object objectId, String allianceName, final String playerName) {
        super.updateDescription(drawArea, objectId, allianceName, playerName);
        IAlliance alliance = AllianceHolder.getAlliance(allianceName);
        final BuildingId buildingId = (BuildingId) objectId;
        //button or back build
        IPlayer player = PlayersHolder.getPlayer(playerName);
        IBuilding building = player.getPlanet().getBuilding(buildingId.getId());
        BuildingDummy buildingDummy = alliance.getBuildingDummy(buildingId);
        final Object event;
        if (//user looking on upgraded version of the not created building
                (building == null && buildingId.getUpgrade() > 0)
                        //check the building is created and in that case check the user looks on upgrade or on existing building
                        || (building != null && buildingId.getUpgrade() > building.getUpgrade())) {
            mButton.setText(LocaleImpl.getInstance().getStringById(R.string.description_back_button));
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
        mButton.setOnClickListener(new ButtonSprite.OnClickListener() {
            @Override
            public void onClick(ButtonSprite pButtonSprite, float pTouchAreaLocalX, float pTouchAreaLocalY) {
                if (event != null) {
                    EventBus.getDefault().post(event);
                }
            }
        });
        //button upgrade
        mUpgradeButton.setPosition(mButton.getX() + mButton.getWidth() / 2
                + BUTTON_MARGIN + mUpgradeButton.getWidth() / 2, mButton.getY());
        boolean isUpgradeAvailable = alliance.isUpgradeAvailable(buildingId);
        //check if upgrades available
        if (isUpgradeAvailable) {
            drawArea.attachChild(mUpgradeButton);
            mUpgradeButton.setOnClickListener(new ButtonSprite.OnClickListener() {
                @Override
                public void onClick(ButtonSprite pButtonSprite, float pTouchAreaLocalX, float pTouchAreaLocalY) {
                    EventBus.getDefault().post(new UpgradeBuildingEvent(mPlayerName, buildingId));
                }
            });
        }
        //button path
        ButtonSprite button = isUpgradeAvailable ? mUpgradeButton : mButton;
        mPathButton.setPosition(button.getX() + button.getWidth() / 2 + BUTTON_MARGIN
                + mPathButton.getWidth() / 2, button.getY());
        drawArea.attachChild(mPathButton);
        mPathButton.setOnClickListener(new ButtonSprite.OnClickListener() {
            @Override
            public void onClick(ButtonSprite pButtonSprite, float pTouchAreaLocalX, float pTouchAreaLocalY) {
                EventBus.getDefault().post(new ShowUnitPathChooser(playerName, buildingId.getId()));
            }
        });
        if (building != null && buildingId.getUpgrade() == building.getUpgrade()) {
            mPathButton.setVisible(true);
        } else {
            mPathButton.setVisible(false);
        }
    }

    @Override
    public void onEvent(final BuildingsAmountChangedEvent buildingsAmountChangedEvent) {
        super.onEvent(buildingsAmountChangedEvent);
        if (mDescriptionAreaUpdater instanceof com.gmail.yaroslavlancelot.eafall.game.popup.description.updater.building.creep.DescriptionAreaUpdater) {
            ((com.gmail.yaroslavlancelot.eafall.game.popup.description.updater.building.creep.DescriptionAreaUpdater)
                    mDescriptionAreaUpdater).updateUpgradeCost(buildingsAmountChangedEvent.getBuildingId(), buildingsAmountChangedEvent.getPlayerName());
        }
        mPathButton.setVisible(true);
    }

    private void initButtons(VertexBufferObjectManager vertexBufferObjectManager) {
        //upgrade
        mUpgradeButton = new TextButton(vertexBufferObjectManager, 300,
                SizeConstants.DESCRIPTION_POPUP_DES_BUTTON_HEIGHT);
        mUpgradeButton.setText(LocaleImpl.getInstance().getStringById(R.string.description_upgrade_button));
        mScene.registerTouchArea(mUpgradeButton);
        //path button
        mPathButton = new TextButton(vertexBufferObjectManager, 300,
                SizeConstants.DESCRIPTION_POPUP_DES_BUTTON_HEIGHT);
        mPathButton.setText(LocaleImpl.getInstance().getStringById(R.string.description_path_button));
        mScene.registerTouchArea(mPathButton);
    }

    protected ITextureRegion getAdditionalInformationImage(Object objectId, String allianceName) {
        IAlliance alliance = AllianceHolder.getAlliance(allianceName);
        BuildingId buildingId = (BuildingId) objectId;
        CreepBuildingDummy dummy = (CreepBuildingDummy) alliance.getBuildingDummy(buildingId);
        final int unitId = dummy.getMovableUnitId(buildingId.getUpgrade());
        return alliance.getUnitDummy(unitId).getImageTextureRegion();
    }
}
