package com.yaroslavlancelot.eafall.game.popup.rolling.description;

import android.content.Context;

import com.yaroslavlancelot.eafall.game.entity.gameobject.building.BuildingId;
import com.yaroslavlancelot.eafall.game.entity.gameobject.building.dummy.BuildingDummy;
import com.yaroslavlancelot.eafall.game.events.aperiodic.ingame.description.BuildingDescriptionShowEvent;
import com.yaroslavlancelot.eafall.game.player.IPlayer;
import com.yaroslavlancelot.eafall.game.player.PlayersHolder;
import com.yaroslavlancelot.eafall.game.popup.rolling.description.updater.IPopupUpdater;
import com.yaroslavlancelot.eafall.game.popup.rolling.description.updater.building.BaseBuildingPopupUpdater;
import com.yaroslavlancelot.eafall.game.popup.rolling.description.updater.building.special.SpecialBuildingPopupUpdater;
import com.yaroslavlancelot.eafall.game.popup.rolling.description.updater.building.unit.defence.DefenceBuildingPopupUpdater;
import com.yaroslavlancelot.eafall.game.popup.rolling.description.updater.building.unit.offence.OffenceBuildingPopupUpdater;
import com.yaroslavlancelot.eafall.game.popup.rolling.description.updater.building.wealth.WealthBuildingPopupUpdater;
import com.yaroslavlancelot.eafall.game.popup.rolling.description.updater.unit.BaseUnitPopupUpdater;
import com.yaroslavlancelot.eafall.game.popup.rolling.description.updater.unit.UnitPopupUpdater;

import org.andengine.engine.camera.Camera;
import org.andengine.entity.scene.Scene;
import org.andengine.opengl.font.FontManager;
import org.andengine.opengl.texture.TextureManager;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

/**
 * Handle logic of redrawing and showing/hiding description popup.
 * Appears in the bottom of the screen when you want to create a building
 * or see unit (other object) characteristics.
 */
public class DescriptionPopup extends BuildingLessDescriptionPopup {
    /** unit building updater */
    private OffenceBuildingPopupUpdater mOffenceBuildingPopupUpdater;
    /** special buildings updater */
    private SpecialBuildingPopupUpdater mSpecialBuildingPopupUpdater;
    /** wealth building updater */
    private WealthBuildingPopupUpdater mWealthBuildingPopupUpdater;
    /** defence building updater */
    private DefenceBuildingPopupUpdater mDefenceBuildingPopupUpdater;

    /**
     * single instance that's why it's private constructor
     *
     * @param scene                     popup attaches to this scene
     * @param camera                    game camera
     * @param vertexBufferObjectManager object manager to create inner elements
     */
    public DescriptionPopup(Scene scene, Camera camera,
                            VertexBufferObjectManager vertexBufferObjectManager) {
        super(scene, camera, vertexBufferObjectManager);
        mOffenceBuildingPopupUpdater = new OffenceBuildingPopupUpdater(vertexBufferObjectManager, this);
        mWealthBuildingPopupUpdater = new WealthBuildingPopupUpdater(vertexBufferObjectManager, this);
        mSpecialBuildingPopupUpdater = new SpecialBuildingPopupUpdater(vertexBufferObjectManager, this);
        mDefenceBuildingPopupUpdater = new DefenceBuildingPopupUpdater(vertexBufferObjectManager, this);
    }

    @Override
    protected BaseUnitPopupUpdater createUnitPopupUpdater(final VertexBufferObjectManager vboManager) {
        return new UnitPopupUpdater(vboManager, this);
    }

    protected void clear() {
        super.clear();
        mOffenceBuildingPopupUpdater.clear();
        mWealthBuildingPopupUpdater.clear();
        mSpecialBuildingPopupUpdater.clear();
        mDefenceBuildingPopupUpdater.clear();
    }

    @SuppressWarnings("unused")
    /** really used by {@link de.greenrobot.event.EventBus} */
    public void onEvent(final BuildingDescriptionShowEvent buildingDescriptionShowEvent) {
        onEvent();
        BuildingId buildingId = buildingDescriptionShowEvent.getObjectId();
        IPlayer player = PlayersHolder.getPlayer(buildingDescriptionShowEvent.getPlayerName());
        BuildingDummy buildingDummy = player.getAlliance().getBuildingDummy(buildingId);
        IPopupUpdater popupUpdater;
        boolean leftBlockVisibility = true;
        switch (buildingDummy.getBuildingType()) {
            case CREEP_BUILDING: {
                popupUpdater = mOffenceBuildingPopupUpdater;
                break;
            }
            case WEALTH_BUILDING: {
                leftBlockVisibility = false;
                popupUpdater = mWealthBuildingPopupUpdater;
                break;
            }
            case SPECIAL_BUILDING: {
                leftBlockVisibility = false;
                popupUpdater = mSpecialBuildingPopupUpdater;
                break;
            }
            case DEFENCE_BUILDING: {
                popupUpdater = mDefenceBuildingPopupUpdater;
                break;
            }
            default: {
                throw new IllegalArgumentException("unknown building type for popup updater");
            }
        }
        mDescriptionPopupBackground.setLeftBlockVisibility(leftBlockVisibility);
        mDescriptionPopupBackground.updateDescription(popupUpdater, buildingId,
                player.getAlliance().getAllianceName(), player.getName());
        showPopup();
    }

    public static void loadResources(Context context, TextureManager textureManager) {
        BuildingLessDescriptionPopup.loadResources(context, textureManager);
        BaseBuildingPopupUpdater.loadResources(context, textureManager);
    }

    public static void loadFonts(FontManager fontManager, TextureManager textureManager) {
        BuildingLessDescriptionPopup.loadFonts(fontManager, textureManager);
        BaseBuildingPopupUpdater.loadFonts(fontManager, textureManager);
    }
}
