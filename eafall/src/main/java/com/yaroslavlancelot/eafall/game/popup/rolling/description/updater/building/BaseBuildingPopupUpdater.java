package com.yaroslavlancelot.eafall.game.popup.rolling.description.updater.building;

import android.content.Context;

import com.yaroslavlancelot.eafall.R;
import com.yaroslavlancelot.eafall.game.alliance.AllianceHolder;
import com.yaroslavlancelot.eafall.game.alliance.IAlliance;
import com.yaroslavlancelot.eafall.game.entity.gameobject.building.BuildingId;
import com.yaroslavlancelot.eafall.game.entity.gameobject.building.IBuilding;
import com.yaroslavlancelot.eafall.game.events.aperiodic.ingame.building.BuildingsAmountChangedEvent;
import com.yaroslavlancelot.eafall.game.events.aperiodic.ingame.description.BuildingDescriptionShowEvent;
import com.yaroslavlancelot.eafall.game.player.IPlayer;
import com.yaroslavlancelot.eafall.game.player.PlayersHolder;
import com.yaroslavlancelot.eafall.game.popup.rolling.description.updater.BasePopupUpdater;
import com.yaroslavlancelot.eafall.general.EbSubscribersHolder;
import com.yaroslavlancelot.eafall.general.locale.LocaleImpl;

import org.andengine.entity.IEntity;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.shape.Shape;
import org.andengine.entity.sprite.ButtonSprite;
import org.andengine.opengl.font.FontManager;
import org.andengine.opengl.texture.TextureManager;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import de.greenrobot.event.EventBus;

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
        IPlayer player = PlayersHolder.getPlayer(playerName);
        mBuildingId = (BuildingId) objectId;
        updateBuildingsAmount(drawArea,
                player.getPlanet().getBuildingsAmount(mBuildingId.getId()));
        mPlayerName = playerName;
        super.updateImage(drawArea, objectId, allianceName, playerName);
    }

    @Override
    public void updateHeaderButtons(ButtonSprite leftArrow, ButtonSprite rightArrow, Object objectId, final String playerName) {
        BuildingId buildingId = (BuildingId) objectId;
        final IPlayer player = PlayersHolder.getPlayer(playerName);
        final int id = buildingId.getId();
        int before = -1;
        int after = Integer.MAX_VALUE;
        int buildingsLimit = player.getBuildingsLimit();
        int i = 0;
        for (Integer ind : player.getAlliance().getBuildingsIds()) {
            if (i < buildingsLimit) {
                if (id > ind && before < ind) {
                    before = ind;
                }
                if (id < ind && after > ind) {
                    after = ind;
                }
                i++;
            } else {
                break;
            }
        }
        if (before != -1) {
            initArrowButton(before, player, leftArrow);
        } else {
            leftArrow.setVisible(false);
        }
        if (after != Integer.MAX_VALUE) {
            initArrowButton(after, player, rightArrow);
        } else {
            rightArrow.setVisible(false);
        }
    }

    @Override
    public void clear() {
        super.clear();
        if (mBaseButton != null) {
            mBaseButton.setSound(true);
        }
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
        mBaseButton.setSound(false);
        if (!mBaseButton.hasParent()) {
            drawArea.attachChild(mBaseButton);
        }
    }

    @Override
    public void updateAdditionInfo(final Shape drawArea, final Object objectId, final String allianceName, final String playerName) {
        if (mAdditionDescriptionImage != null) {
            mAdditionDescriptionImage.detachSelf();
        }
    }

    /** set visibility and click listener to arrow button */
    private void initArrowButton(final int before, final IPlayer player, ButtonSprite arrowButton) {
        arrowButton.setVisible(true);
        arrowButton.setOnClickListener(new ButtonSprite.OnClickListener() {
            @Override
            public void onClick(final ButtonSprite pButtonSprite, final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
                EventBus.getDefault().post(new BuildingDescriptionShowEvent(createBuildingId(player, before), player.getName()));
            }
        });
    }

    /**
     * creates {@link BuildingId} by the give screen. If building exist on the planet
     * then upgrade value of the result BuildingId will be equal to the upgrade of
     * the building on the planet. If the building doesn't exist then upgrade will
     * be equal to 0.
     *
     * @param player player which building is this
     * @param id     screen of the building
     * @return {@link BuildingId} instance
     */
    private BuildingId createBuildingId(IPlayer player, int id) {
        IBuilding building = player.getPlanet().getBuilding(id);
        if (building == null) {
            return BuildingId.makeId(id, 0);
        }
        return BuildingId.makeId(id, building.getUpgrade());
    }

    private void updateBuildingsAmount(IEntity entity, int buildingsAmount) {
        mAmountDrawer.setText(buildingsAmount);
        mAmountDrawer.draw(entity);
    }

    /**
     * check passed player name equality to last player name used in
     * {@link BaseBuildingPopupUpdater#updateDescription}
     */
    protected boolean checkPlayer(String playerName) {
        return mPlayerName.equals(playerName);
    }

    /** updates buildings amount */
    public void onEvent(final BuildingsAmountChangedEvent buildingsAmountChangedEvent) {
        if (visible() && checkPlayer(buildingsAmountChangedEvent.getPlayerName())) {
            if (mBuildingId.equals(buildingsAmountChangedEvent.getBuildingId())) {
                mAmountDrawer.setText(buildingsAmountChangedEvent.getNewBuildingsAmount());
            }
        }
    }

    public static void loadFonts(FontManager fontManager, TextureManager textureManager) {
        //amount font
        AmountDrawer.loadFonts(fontManager, textureManager);
    }

    public static void loadResources(Context context, TextureManager textureManager) {
        AmountDrawer.loadResources(context, textureManager);
    }
}
