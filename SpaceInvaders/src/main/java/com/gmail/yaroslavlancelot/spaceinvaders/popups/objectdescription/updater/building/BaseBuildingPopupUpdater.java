package com.gmail.yaroslavlancelot.spaceinvaders.popups.objectdescription.updater.building;

import android.content.Context;

import com.gmail.yaroslavlancelot.spaceinvaders.R;
import com.gmail.yaroslavlancelot.spaceinvaders.alliances.AllianceHolder;
import com.gmail.yaroslavlancelot.spaceinvaders.alliances.IAlliance;
import com.gmail.yaroslavlancelot.spaceinvaders.eventbus.BuildingsAmountChangedEvent;
import com.gmail.yaroslavlancelot.spaceinvaders.objects.objects.buildings.BuildingId;
import com.gmail.yaroslavlancelot.spaceinvaders.popups.objectdescription.updater.BasePopupUpdater;
import com.gmail.yaroslavlancelot.spaceinvaders.teams.ITeam;
import com.gmail.yaroslavlancelot.spaceinvaders.teams.TeamsHolder;
import com.gmail.yaroslavlancelot.spaceinvaders.utils.LocaleImpl;
import com.gmail.yaroslavlancelot.spaceinvaders.visualelements.buttons.TextButton;

import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.shape.RectangularShape;
import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.font.FontManager;
import org.andengine.opengl.texture.TextureManager;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.color.Color;

import de.greenrobot.event.EventBus;

/**
 * Base building description functionality
 */
public abstract class BaseBuildingPopupUpdater extends BasePopupUpdater {
    protected static final BuildingId sNoValue = null;
    protected volatile BuildingId mBuildingId = sNoValue;
    protected static final int BUTTON_MARGIN = 20;
    protected volatile String mTeamName = "";
    /** basically used for display buildings amount on building image */
    protected AmountDrawer mAmountDrawer;
    /**
     * contains logic to create a building, or if the building can't be build,
     * sed as back button to return to previous popup state
     */
    protected TextButton mBuildOrBackButton;
    /** image for addition information */
    protected Sprite mAdditionDescriptionImage;
    /** used only for intercept touch event on addition information area */
    protected Rectangle mAdditionInfoRectangle;
    /** building description object (update description area which u pass to it) */
    protected IDescriptionAreaUpdater mDescriptionAreaUpdater;

    public BaseBuildingPopupUpdater(VertexBufferObjectManager vertexBufferObjectManager, Scene scene) {
        super(vertexBufferObjectManager, scene);
        mAmountDrawer = new AmountDrawer(vertexBufferObjectManager);

        //build
        mBuildOrBackButton = new TextButton(vertexBufferObjectManager, 300, 70);
        mScene.registerTouchArea(mBuildOrBackButton);

        initAdditionInformationArea();

        EventBus.getDefault().register(this);
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

    @Override
    public void updateImage(RectangularShape drawArea, Object objectId, String raceName, String teamName) {
        super.updateImage(drawArea, objectId, raceName, teamName);
        ITeam team = TeamsHolder.getInstance().getElement(teamName);
        mBuildingId = (BuildingId) objectId;
        updateBuildingsAmount(team.getTeamPlanet().getBuildingsAmount(mBuildingId.getId()));
        mTeamName = teamName;
    }

    private void updateBuildingsAmount(int buildingsAmount) {
        mAmountDrawer.setText(buildingsAmount);
        mAmountDrawer.draw(mObjectImage);
    }

    @Override
    protected String getDescribedObjectName(Object objectId, String raceName) {
        return LocaleImpl.getInstance().getStringById
                (AllianceHolder.getInstance().getElement(raceName).getBuildingDummy((BuildingId) objectId).getStringId());
    }

    @Override
    public void clear() {
        super.clear();
        mAmountDrawer.detach();
        mBuildOrBackButton.detachSelf();
        if (mAdditionDescriptionImage != null) {
            mAdditionDescriptionImage.detachSelf(mScene);
            mAdditionDescriptionImage = null;
            mAdditionInfoRectangle.detachSelf();
        }
        mBuildingId = sNoValue;
        mTeamName = "";
        mDescriptionAreaUpdater.clearDescription();
    }

    @Override
    protected ITextureRegion getDescriptionImage(Object objectId, String raceName) {
        IAlliance race = AllianceHolder.getInstance().getElement(raceName);
        BuildingId buildingId = (BuildingId) objectId;
        return race.getBuildingDummy(buildingId).getTextureRegionArray(buildingId.getUpgrade());
    }

    @Override
    public void updateDescription(RectangularShape drawArea, Object objectId, String raceName, String teamName) {
        //description
        mDescriptionAreaUpdater.updateDescription(drawArea, objectId, raceName, teamName);
        //build button
        mBuildOrBackButton.setText(LocaleImpl.getInstance().getStringById(R.string.description_build_button));
        mBuildOrBackButton.setPosition(0, drawArea.getHeight() - mBuildOrBackButton.getHeight());
        drawArea.attachChild(mBuildOrBackButton);
    }

    /** updates buildings amount */
    public void onEvent(final BuildingsAmountChangedEvent buildingsAmountChangedEvent) {
        if (!mTeamName.equals(buildingsAmountChangedEvent.getTeamName())
                || !mBuildingId.equals(buildingsAmountChangedEvent.getBuildingId())) {
            return;
        }
        mAmountDrawer.setText(buildingsAmountChangedEvent.getNewBuildingsAmount());
    }
}
