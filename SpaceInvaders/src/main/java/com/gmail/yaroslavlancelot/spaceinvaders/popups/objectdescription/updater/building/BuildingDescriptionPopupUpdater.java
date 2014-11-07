package com.gmail.yaroslavlancelot.spaceinvaders.popups.objectdescription.updater.building;

import android.content.Context;

import com.gmail.yaroslavlancelot.spaceinvaders.R;
import com.gmail.yaroslavlancelot.spaceinvaders.eventbus.BuildingsAmountChangedEvent;
import com.gmail.yaroslavlancelot.spaceinvaders.eventbus.CreateBuildingEvent;
import com.gmail.yaroslavlancelot.spaceinvaders.eventbus.UpgradeBuildingEvent;
import com.gmail.yaroslavlancelot.spaceinvaders.eventbus.description.BuildingDescriptionShowEvent;
import com.gmail.yaroslavlancelot.spaceinvaders.eventbus.description.UnitByBuildingDescriptionShowEvent;
import com.gmail.yaroslavlancelot.spaceinvaders.eventbus.unitpath.ShowUnitPathChooser;
import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.objects.dummies.CreepBuildingDummy;
import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.objects.staticobjects.Building;
import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.objects.staticobjects.BuildingId;
import com.gmail.yaroslavlancelot.spaceinvaders.popups.objectdescription.updater.BaseDescriptionPopupUpdater;
import com.gmail.yaroslavlancelot.spaceinvaders.races.IRace;
import com.gmail.yaroslavlancelot.spaceinvaders.races.RacesHolder;
import com.gmail.yaroslavlancelot.spaceinvaders.teams.ITeam;
import com.gmail.yaroslavlancelot.spaceinvaders.teams.TeamsHolder;
import com.gmail.yaroslavlancelot.spaceinvaders.utils.LocaleImpl;
import com.gmail.yaroslavlancelot.spaceinvaders.utils.TouchUtils;
import com.gmail.yaroslavlancelot.spaceinvaders.visualelements.buttons.TextButton;

import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.shape.RectangularShape;
import org.andengine.entity.sprite.ButtonSprite;
import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.font.FontManager;
import org.andengine.opengl.texture.TextureManager;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.color.Color;

import de.greenrobot.event.EventBus;

/** updates buildings description popup */
public class BuildingDescriptionPopupUpdater extends BaseDescriptionPopupUpdater {
    private static final BuildingId sNoValue = null;
    private volatile BuildingId mBuildingId = sNoValue;
    private static final int BUTTON_MARGIN = 20;
    private volatile String mTeamName = "";
    /** basically used for display buildings amount on building image */
    private AmountDrawer mAmountDrawer;
    /** press to create a building */
    private TextButton mBuildOrBackButton;
    /** press to upgrade building (if such exist) */
    private TextButton mUpgradeButton;
    /** press to set units path for the current building */
    private TextButton mPathButton;
    /** image for addition information */
    private Sprite mAdditionDescriptionImage;
    /** building description object (update description area which u pass to it) */
    private DescriptionAreaUpdater mDescriptionAreaUpdater;
    /** used only for intercept touch event on addition information area */
    private Rectangle mAdditionInfoRectangle;

    public BuildingDescriptionPopupUpdater(VertexBufferObjectManager vertexBufferObjectManager, Scene scene) {
        super(vertexBufferObjectManager, scene);
        mAmountDrawer = new AmountDrawer(vertexBufferObjectManager);
        mDescriptionAreaUpdater = new BuildingDescriptionAreaUpdater(vertexBufferObjectManager, scene);
        initBuildUpgradeButtons(vertexBufferObjectManager);
        initAdditionInformationArea();
        EventBus.getDefault().register(this);
    }

    private void initBuildUpgradeButtons(VertexBufferObjectManager vertexBufferObjectManager) {
        //build
        mBuildOrBackButton = new TextButton(vertexBufferObjectManager, 300, 70);
        mScene.registerTouchArea(mBuildOrBackButton);
        //upgrade
        mUpgradeButton = new TextButton(vertexBufferObjectManager, 300, 70);
        mUpgradeButton.setText(LocaleImpl.getInstance().getStringById(R.string.description_upgrade_button));
        mScene.registerTouchArea(mUpgradeButton);
        //upgrade
        mPathButton = new TextButton(vertexBufferObjectManager, 300, 70);
        mPathButton.setText(LocaleImpl.getInstance().getStringById(R.string.description_path_button));
        mScene.registerTouchArea(mPathButton);
    }

    private void initAdditionInformationArea() {
        mAdditionInfoRectangle = new Rectangle(0, 0, 10, 10, mVertexBufferObjectManager);
        mAdditionInfoRectangle.setColor(Color.TRANSPARENT);
        mScene.registerTouchArea(mAdditionInfoRectangle);
    }

    public static void loadFonts(FontManager fontManager, TextureManager textureManager) {
        //amount font
        AmountDrawer.loadFonts(fontManager, textureManager);
        TextButton.loadFonts(fontManager, textureManager);
    }

    public static void loadResources(Context context, TextureManager textureManager) {
        TextButton.loadResources(context, textureManager);
    }

    @SuppressWarnings("unused")
    /** really used by {@link de.greenrobot.event.EventBus} */
    public void onEvent(final BuildingsAmountChangedEvent buildingsAmountChangedEvent) {
        if (!mTeamName.equals(buildingsAmountChangedEvent.getTeamName())
                || !mBuildingId.equals(buildingsAmountChangedEvent.getBuildingId())) {
            return;
        }
        mAmountDrawer.setText(buildingsAmountChangedEvent.getNewBuildingsAmount());
        if (mDescriptionAreaUpdater instanceof BuildingDescriptionAreaUpdater) {
            ((BuildingDescriptionAreaUpdater) mDescriptionAreaUpdater).updateUpgradeCost(
                    buildingsAmountChangedEvent.getBuildingId(), buildingsAmountChangedEvent.getTeamName());
        }
        mPathButton.setVisible(true);
    }

    @Override
    public void updateImage(RectangularShape drawArea, Object objectId, String raceName, String teamName) {
        super.updateImage(drawArea, objectId, raceName, teamName);
        ITeam team = TeamsHolder.getInstance().getElement(teamName);
        mBuildingId = (BuildingId) objectId;
        updateBuildingsAmount(team.getTeamPlanet().getBuildingsAmount(mBuildingId.getId()));
        mTeamName = teamName;
    }

    @Override
    protected String getDescribedObjectName(Object objectId, String raceName) {
        return LocaleImpl.getInstance().getStringById
                (RacesHolder.getInstance().getElement(raceName).getBuildingDummy((BuildingId) objectId).getStringId());
    }

    @Override
    public void clear() {
        super.clear();
        mAmountDrawer.detach();
        mBuildOrBackButton.detachSelf();
        mUpgradeButton.detachSelf();
        mPathButton.detachSelf();
        if (mAdditionDescriptionImage != null) {
            mAdditionDescriptionImage.detachSelf(mScene);
            mAdditionDescriptionImage = null;
            mAdditionInfoRectangle.detachSelf();
        }
        mDescriptionAreaUpdater.clearDescription();
        mBuildingId = sNoValue;
        mTeamName = "";
    }

    @Override
    protected ITextureRegion getDescriptionImage(Object objectId, String raceName) {
        IRace race = RacesHolder.getInstance().getElement(raceName);
        BuildingId buildingId = (BuildingId) objectId;
        return race.getBuildingDummy(buildingId).getTextureRegionArray(buildingId.getUpgrade());
    }

    private void updateBuildingsAmount(int buildingsAmount) {
        mAmountDrawer.setText(buildingsAmount);
        mAmountDrawer.draw(mObjectImage);
    }

    @Override
    public void updateDescription(RectangularShape drawArea, Object objectId, String raceName, final String teamName) {
        IRace race = RacesHolder.getRace(raceName);
        final BuildingId buildingId = (BuildingId) objectId;
        //description
        mDescriptionAreaUpdater.updateDescription(drawArea, objectId, raceName, teamName);
        //button or back build
        Building building = TeamsHolder.getTeam(teamName).getTeamPlanet().getBuilding(buildingId.getId());
        String text;
        final Object event;
        if (//user looking on upgraded version of the not created building
                (building == null && buildingId.getUpgrade() > 0)
                        //check the building is created and in that case check the user looks on upgrade or on existing building
                        || (building != null && buildingId.getUpgrade() > building.getUpgrade())) {
            text = LocaleImpl.getInstance().getStringById(R.string.description_back_button);
            event = new BuildingDescriptionShowEvent(
                    BuildingId.makeId(buildingId.getId(), buildingId.getUpgrade() - 1), teamName);
        } else {
            text = LocaleImpl.getInstance().getStringById(R.string.description_build_button);
            event = new CreateBuildingEvent(mTeamName, buildingId);
        }
        mBuildOrBackButton.setText(text);
        mBuildOrBackButton.setOnClickListener(new ButtonSprite.OnClickListener() {
            @Override
            public void onClick(ButtonSprite pButtonSprite, float pTouchAreaLocalX, float pTouchAreaLocalY) {
                EventBus.getDefault().post(event);
            }
        });
        mBuildOrBackButton.setPosition(0, drawArea.getHeight() - mBuildOrBackButton.getHeight());
        drawArea.attachChild(mBuildOrBackButton);
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
        CreepBuildingDummy dummy = TeamsHolder.getTeam(teamName).getTeamRace().getBuildingDummy(buildingId);
        final int unitId = dummy.getUnitId(buildingId.getUpgrade());
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
        IRace race = RacesHolder.getRace(raceName);
        BuildingId buildingId = (BuildingId) objectId;
        CreepBuildingDummy dummy = race.getBuildingDummy(buildingId);
        final int unitId = dummy.getUnitId(buildingId.getUpgrade());
        return race.getUnitDummy(unitId).getTextureRegion();
    }
}
