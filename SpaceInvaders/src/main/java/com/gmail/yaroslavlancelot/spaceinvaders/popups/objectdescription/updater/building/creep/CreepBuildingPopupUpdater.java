package com.gmail.yaroslavlancelot.spaceinvaders.popups.objectdescription.updater.building.creep;

import com.gmail.yaroslavlancelot.spaceinvaders.R;
import com.gmail.yaroslavlancelot.spaceinvaders.alliances.AllianceHolder;
import com.gmail.yaroslavlancelot.spaceinvaders.alliances.IAlliance;
import com.gmail.yaroslavlancelot.spaceinvaders.eventbus.BuildingsAmountChangedEvent;
import com.gmail.yaroslavlancelot.spaceinvaders.eventbus.CreateBuildingEvent;
import com.gmail.yaroslavlancelot.spaceinvaders.eventbus.UpgradeBuildingEvent;
import com.gmail.yaroslavlancelot.spaceinvaders.eventbus.description.BuildingDescriptionShowEvent;
import com.gmail.yaroslavlancelot.spaceinvaders.eventbus.description.UnitByBuildingDescriptionShowEvent;
import com.gmail.yaroslavlancelot.spaceinvaders.eventbus.unitpath.ShowUnitPathChooser;
import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.objects.buildings.BuildingId;
import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.objects.buildings.IBuilding;
import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.objects.dummies.CreepBuildingDummy;
import com.gmail.yaroslavlancelot.spaceinvaders.popups.objectdescription.updater.building.BaseBuildingPopupUpdater;
import com.gmail.yaroslavlancelot.spaceinvaders.teams.TeamsHolder;
import com.gmail.yaroslavlancelot.spaceinvaders.utils.LocaleImpl;
import com.gmail.yaroslavlancelot.spaceinvaders.utils.TouchUtils;
import com.gmail.yaroslavlancelot.spaceinvaders.visualelements.buttons.TextButton;

import org.andengine.entity.scene.Scene;
import org.andengine.entity.shape.RectangularShape;
import org.andengine.entity.sprite.ButtonSprite;
import org.andengine.entity.sprite.Sprite;
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

    private void initButtons(VertexBufferObjectManager vertexBufferObjectManager) {
        //upgrade
        mUpgradeButton = new TextButton(vertexBufferObjectManager, 300, 70);
        mUpgradeButton.setText(LocaleImpl.getInstance().getStringById(R.string.description_upgrade_button));
        mScene.registerTouchArea(mUpgradeButton);
        //upgrade
        mPathButton = new TextButton(vertexBufferObjectManager, 300, 70);
        mPathButton.setText(LocaleImpl.getInstance().getStringById(R.string.description_path_button));
        mScene.registerTouchArea(mPathButton);
    }

    @Override
    public void clear() {
        super.clear();
        mUpgradeButton.detachSelf();
        mPathButton.detachSelf();
    }

    @Override
    public void updateDescription(RectangularShape drawArea, Object objectId, String raceName, final String teamName) {
        super.updateDescription(drawArea, objectId, raceName, teamName);
        IAlliance race = AllianceHolder.getRace(raceName);
        final BuildingId buildingId = (BuildingId) objectId;
        //button or back build
        IBuilding building = TeamsHolder.getTeam(teamName).getTeamPlanet().getBuilding(buildingId.getId());
        final Object event;
        if (//user looking on upgraded version of the not created building
                (building == null && buildingId.getUpgrade() > 0)
                        //check the building is created and in that case check the user looks on upgrade or on existing building
                        || (building != null && buildingId.getUpgrade() > building.getUpgrade())) {
            mBuildOrBackButton.setText(LocaleImpl.getInstance().getStringById(R.string.description_back_button));
            event = new BuildingDescriptionShowEvent(
                    BuildingId.makeId(buildingId.getId(), buildingId.getUpgrade() - 1), teamName);
        } else {
            event = new CreateBuildingEvent(mTeamName, buildingId);
        }
        mBuildOrBackButton.setOnClickListener(new ButtonSprite.OnClickListener() {
            @Override
            public void onClick(ButtonSprite pButtonSprite, float pTouchAreaLocalX, float pTouchAreaLocalY) {
                EventBus.getDefault().post(event);
            }
        });
        //button upgrade
        mUpgradeButton.setPosition(mBuildOrBackButton.getX() + mBuildOrBackButton.getWidth() + BUTTON_MARGIN, mBuildOrBackButton.getY());
        boolean isUpgradeAvailable = race.isUpgradeAvailable(buildingId);
        //check if upgrades available
        if (isUpgradeAvailable) {
            drawArea.attachChild(mUpgradeButton);
            mUpgradeButton.setOnClickListener(new ButtonSprite.OnClickListener() {
                @Override
                public void onClick(ButtonSprite pButtonSprite, float pTouchAreaLocalX, float pTouchAreaLocalY) {
                    EventBus.getDefault().post(new UpgradeBuildingEvent(mTeamName, buildingId));
                }
            });
        }
        //button path
        ButtonSprite button = isUpgradeAvailable ? mUpgradeButton : mBuildOrBackButton;
        mPathButton.setPosition(button.getX() + button.getWidth() + BUTTON_MARGIN, button.getY());
        drawArea.attachChild(mPathButton);
        mPathButton.setOnClickListener(new ButtonSprite.OnClickListener() {
            @Override
            public void onClick(ButtonSprite pButtonSprite, float pTouchAreaLocalX, float pTouchAreaLocalY) {
                EventBus.getDefault().post(new ShowUnitPathChooser(teamName, buildingId.getId()));
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
        if (mDescriptionAreaUpdater instanceof com.gmail.yaroslavlancelot.spaceinvaders.popups.objectdescription.updater.building.creep.DescriptionAreaUpdater) {
            ((com.gmail.yaroslavlancelot.spaceinvaders.popups.objectdescription.updater.building.creep.DescriptionAreaUpdater)
                    mDescriptionAreaUpdater).updateUpgradeCost(buildingsAmountChangedEvent.getBuildingId(), buildingsAmountChangedEvent.getTeamName());
        }
        mPathButton.setVisible(true);
    }

    @Override
    public void updateAdditionInfo(RectangularShape drawArea, Object objectId, String raceName, final String teamName) {
        if (mAdditionDescriptionImage != null) {
            mAdditionDescriptionImage.detachSelf();
            mAdditionInfoRectangle.detachSelf();
        }
        mAdditionDescriptionImage = new Sprite(0, 0, drawArea.getWidth(), drawArea.getHeight(),
                getAdditionalInformationImage(objectId, raceName), mVertexBufferObjectManager);

        mAdditionInfoRectangle.setWidth(drawArea.getWidth());
        mAdditionInfoRectangle.setHeight(drawArea.getHeight());

        final BuildingId buildingId = (BuildingId) objectId;
        mAdditionInfoRectangle.setTouchCallback(
                new TouchUtils.CustomTouchListener(mAdditionDescriptionImage) {
                    @Override
                    public void click() {
                        super.click();
                        EventBus.getDefault().post(new UnitByBuildingDescriptionShowEvent(buildingId, teamName));
                    }
                });
        mAdditionInfoRectangle.attachChild(mAdditionDescriptionImage);
        drawArea.attachChild(mAdditionInfoRectangle);
    }

    protected ITextureRegion getAdditionalInformationImage(Object objectId, String raceName) {
        IAlliance race = AllianceHolder.getRace(raceName);
        BuildingId buildingId = (BuildingId) objectId;
        CreepBuildingDummy dummy = (CreepBuildingDummy) race.getBuildingDummy(buildingId);
        final int unitId = dummy.getUnitId(buildingId.getUpgrade());
        return race.getUnitDummy(unitId).getTextureRegion();
    }
}
