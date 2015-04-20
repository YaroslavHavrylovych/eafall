package com.gmail.yaroslavlancelot.eafall.game.popup.description.updater.building.creep;

import com.gmail.yaroslavlancelot.eafall.R;
import com.gmail.yaroslavlancelot.eafall.game.alliance.AllianceHolder;
import com.gmail.yaroslavlancelot.eafall.game.alliance.IAlliance;
import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.building.BuildingId;
import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.building.IBuilding;
import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.building.dummy.CreepBuildingDummy;
import com.gmail.yaroslavlancelot.eafall.game.eventbus.building.BuildingsAmountChangedEvent;
import com.gmail.yaroslavlancelot.eafall.game.eventbus.building.CreateBuildingEvent;
import com.gmail.yaroslavlancelot.eafall.game.eventbus.building.UpgradeBuildingEvent;
import com.gmail.yaroslavlancelot.eafall.game.eventbus.description.BuildingDescriptionShowEvent;
import com.gmail.yaroslavlancelot.eafall.game.eventbus.description.UnitByBuildingDescriptionShowEvent;
import com.gmail.yaroslavlancelot.eafall.game.eventbus.path.ShowUnitPathChooser;
import com.gmail.yaroslavlancelot.eafall.game.popup.description.updater.building.BaseBuildingPopupUpdater;
import com.gmail.yaroslavlancelot.eafall.game.team.TeamsHolder;
import com.gmail.yaroslavlancelot.eafall.game.touch.StaticHelper;
import com.gmail.yaroslavlancelot.eafall.game.visual.buttons.TextButton;
import com.gmail.yaroslavlancelot.eafall.general.locale.LocaleImpl;

import org.andengine.entity.scene.Scene;
import org.andengine.entity.shape.Shape;
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
        //path button
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
    public void updateDescription(Shape drawArea, Object objectId, String raceName, final String teamName) {
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
        mUpgradeButton.setPosition(mBuildOrBackButton.getX() + mBuildOrBackButton.getWidth() / 2
                + BUTTON_MARGIN + mUpgradeButton.getWidth() / 2, mBuildOrBackButton.getY());
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
        mPathButton.setPosition(button.getX() + button.getWidth() / 2 + BUTTON_MARGIN
                + mPathButton.getWidth() / 2, button.getY());
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
        if (mDescriptionAreaUpdater instanceof com.gmail.yaroslavlancelot.eafall.game.popup.description.updater.building.creep.DescriptionAreaUpdater) {
            ((com.gmail.yaroslavlancelot.eafall.game.popup.description.updater.building.creep.DescriptionAreaUpdater)
                    mDescriptionAreaUpdater).updateUpgradeCost(buildingsAmountChangedEvent.getBuildingId(), buildingsAmountChangedEvent.getTeamName());
        }
        mPathButton.setVisible(true);
    }

    @Override
    public void updateAdditionInfo(Shape drawArea, Object objectId, String raceName, final String teamName) {
        if (mAdditionDescriptionImage != null) {
            mAdditionDescriptionImage.detachSelf();
            mAdditionInfoRectangle.detachSelf();
        }
        mAdditionDescriptionImage = new Sprite(
                drawArea.getWidth() / 2, drawArea.getHeight() / 2,
                drawArea.getWidth(), drawArea.getHeight(),
                getAdditionalInformationImage(objectId, raceName), mVertexBufferObjectManager);

        final BuildingId buildingId = (BuildingId) objectId;
        mAdditionInfoRectangle.setTouchCallback(
                new StaticHelper.CustomTouchListener(mAdditionDescriptionImage) {
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
        final int unitId = dummy.getMovableUnitId(buildingId.getUpgrade());
        return race.getUnitDummy(unitId).getImageTextureRegion();
    }
}
