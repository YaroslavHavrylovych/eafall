package com.gmail.yaroslavlancelot.eafall.game.popup.rolling.description.updater.building;

import android.content.Context;

import com.gmail.yaroslavlancelot.eafall.R;
import com.gmail.yaroslavlancelot.eafall.game.alliance.AllianceHolder;
import com.gmail.yaroslavlancelot.eafall.game.alliance.IAlliance;
import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.building.BuildingId;
import com.gmail.yaroslavlancelot.eafall.game.events.aperiodic.ingame.building.BuildingsAmountChangedEvent;
import com.gmail.yaroslavlancelot.eafall.game.player.IPlayer;
import com.gmail.yaroslavlancelot.eafall.game.player.PlayersHolder;
import com.gmail.yaroslavlancelot.eafall.game.popup.rolling.description.updater.BasePopupUpdater;
import com.gmail.yaroslavlancelot.eafall.game.visual.buttons.TextButton;
import com.gmail.yaroslavlancelot.eafall.general.EbSubscribersHolder;
import com.gmail.yaroslavlancelot.eafall.general.locale.LocaleImpl;

import org.andengine.entity.IEntity;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.shape.Shape;
import org.andengine.opengl.font.FontManager;
import org.andengine.opengl.texture.TextureManager;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

/**
 * Base building description functionality
 */
public abstract class BaseBuildingPopupUpdater extends BasePopupUpdater {
    protected static final BuildingId sNoValue = null;
    protected static final int BUTTON_MARGIN = 20;
    protected volatile BuildingId mBuildingId = sNoValue;
    protected volatile String mPlayerName = "";
    /** basically used for display buildings amount on building image */
    protected AmountDrawer mAmountDrawer;
    /** building description object (update description area which u pass to it) */
    protected IDescriptionAreaUpdater mDescriptionAreaUpdater;

    public BaseBuildingPopupUpdater(VertexBufferObjectManager vertexBufferObjectManager, Scene scene) {
        super(vertexBufferObjectManager, scene);
        mAmountDrawer = new AmountDrawer(vertexBufferObjectManager);
        EbSubscribersHolder.register(this);
    }

    @Override
    public void updateImage(Shape drawArea, Object objectId, String allianceName, String playerName) {
        IPlayer player = PlayersHolder.getInstance().getElement(playerName);
        mBuildingId = (BuildingId) objectId;
        updateBuildingsAmount(drawArea,
                player.getPlanet().getBuildingsAmount(mBuildingId.getId()));
        mPlayerName = playerName;
        super.updateImage(drawArea, objectId, allianceName, playerName);
    }

    @Override
    public void clear() {
        super.clear();
        mAmountDrawer.detach();
        mBuildingId = sNoValue;
        mPlayerName = "";
        mDescriptionAreaUpdater.clearDescription();
    }

    @Override
    protected String getDescribedObjectName(Object objectId, String allianceName) {
        return LocaleImpl.getInstance().getStringById
                (AllianceHolder.getInstance().getElement(allianceName)
                        .getBuildingDummy((BuildingId) objectId).getStringId());
    }

    @Override
    protected ITextureRegion getDescriptionImage(Object objectId, String allianceName) {
        IAlliance alliance = AllianceHolder.getInstance().getElement(allianceName);
        BuildingId buildingId = (BuildingId) objectId;
        return alliance.getBuildingDummy(buildingId).getImageTextureRegionArray(buildingId.getUpgrade());
    }

    @Override
    public void updateDescription(Shape drawArea, Object objectId, String allianceName, String playerName) {
        //description
        mDescriptionAreaUpdater.updateDescription(drawArea, objectId, allianceName, playerName);
        //build button
        mBaseButton.setText(LocaleImpl.getInstance().getStringById(R.string.description_build_button));
        mBaseButton.setPosition(mBaseButton.getWidth() / 2, mBaseButton.getHeight() / 2);
        drawArea.attachChild(mBaseButton);
    }

    @Override
    public void updateAdditionInfo(final Shape drawArea, final Object objectId, final String allianceName, final String playerName) {
        if (mAdditionDescriptionImage != null) {
            mAdditionDescriptionImage.detachSelf();
        }
    }

    private void updateBuildingsAmount(IEntity entity, int buildingsAmount) {
        mAmountDrawer.setText(buildingsAmount);
        mAmountDrawer.draw(entity);
    }

    /** updates buildings amount */
    public void onEvent(final BuildingsAmountChangedEvent buildingsAmountChangedEvent) {
        if (!mPlayerName.equals(buildingsAmountChangedEvent.getPlayerName())
                || !mBuildingId.equals(buildingsAmountChangedEvent.getBuildingId())) {
            return;
        }
        mAmountDrawer.setText(buildingsAmountChangedEvent.getNewBuildingsAmount());
    }

    public static void loadFonts(FontManager fontManager, TextureManager textureManager) {
        //amount font
        AmountDrawer.loadFonts(fontManager, textureManager);
        TextButton.loadFonts(fontManager, textureManager);
    }

    public static void loadResources(Context context, TextureManager textureManager) {
        TextButton.loadResources(context, textureManager);
        AmountDrawer.loadResources(context, textureManager);
    }
}
