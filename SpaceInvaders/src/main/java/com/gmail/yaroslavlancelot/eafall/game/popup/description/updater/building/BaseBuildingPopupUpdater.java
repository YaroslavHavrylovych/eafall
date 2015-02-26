package com.gmail.yaroslavlancelot.eafall.game.popup.description.updater.building;

import android.content.Context;

import com.gmail.yaroslavlancelot.eafall.R;
import com.gmail.yaroslavlancelot.eafall.game.alliance.AllianceHolder;
import com.gmail.yaroslavlancelot.eafall.game.alliance.IAlliance;
import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.building.BuildingId;
import com.gmail.yaroslavlancelot.eafall.game.eventbus.building.BuildingsAmountChangedEvent;
import com.gmail.yaroslavlancelot.eafall.game.popup.description.updater.BasePopupUpdater;
import com.gmail.yaroslavlancelot.eafall.game.team.ITeam;
import com.gmail.yaroslavlancelot.eafall.game.team.TeamsHolder;
import com.gmail.yaroslavlancelot.eafall.game.visual.buttons.TextButton;
import com.gmail.yaroslavlancelot.eafall.general.locale.LocaleImpl;

import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.shape.Shape;
import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.font.FontManager;
import org.andengine.opengl.texture.TextureManager;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.adt.color.Color;

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
    //TODO I dont like this thing
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
    public void updateImage(Shape drawArea, Object objectId, String raceName, String teamName) {
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
            mAdditionDescriptionImage.detachSelf();
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
    public void updateDescription(Shape drawArea, Object objectId, String raceName, String teamName) {
        //description
        mDescriptionAreaUpdater.updateDescription(drawArea, objectId, raceName, teamName);
        //build button
        mBuildOrBackButton.setText(LocaleImpl.getInstance().getStringById(R.string.description_build_button));
        mBuildOrBackButton.setPosition(mBuildOrBackButton.getWidth() / 2, mBuildOrBackButton.getHeight() / 2);
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
