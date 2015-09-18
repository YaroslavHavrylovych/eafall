package com.gmail.yaroslavlancelot.eafall.game.popup.rolling.description;

import android.content.Context;

import com.gmail.yaroslavlancelot.eafall.game.constant.SizeConstants;
import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.building.BuildingId;
import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.building.dummy.BuildingDummy;
import com.gmail.yaroslavlancelot.eafall.game.events.aperiodic.ingame.description.BuildingDescriptionShowEvent;
import com.gmail.yaroslavlancelot.eafall.game.events.aperiodic.ingame.description.UnitByBuildingDescriptionShowEvent;
import com.gmail.yaroslavlancelot.eafall.game.player.IPlayer;
import com.gmail.yaroslavlancelot.eafall.game.player.PlayersHolder;
import com.gmail.yaroslavlancelot.eafall.game.popup.rolling.RollingPopup;
import com.gmail.yaroslavlancelot.eafall.game.popup.rolling.description.updater.IPopupUpdater;
import com.gmail.yaroslavlancelot.eafall.game.popup.rolling.description.updater.building.BaseBuildingPopupUpdater;
import com.gmail.yaroslavlancelot.eafall.game.popup.rolling.description.updater.building.unit.offence.OffenceBuildingPopupUpdater;
import com.gmail.yaroslavlancelot.eafall.game.popup.rolling.description.updater.building.unit.defence.DefenceBuildingPopupUpdater;
import com.gmail.yaroslavlancelot.eafall.game.popup.rolling.description.updater.building.special.SpecialBuildingPopupUpdater;
import com.gmail.yaroslavlancelot.eafall.game.popup.rolling.description.updater.building.wealth.WealthBuildingPopupUpdater;
import com.gmail.yaroslavlancelot.eafall.game.popup.rolling.description.updater.unit.UnitPopupUpdater;
import com.gmail.yaroslavlancelot.eafall.game.touch.TouchHelper;
import com.gmail.yaroslavlancelot.eafall.game.visual.buttons.ConstructionPopupButton;
import com.gmail.yaroslavlancelot.eafall.game.visual.text.DescriptionText;
import com.gmail.yaroslavlancelot.eafall.game.visual.text.Link;
import com.gmail.yaroslavlancelot.eafall.general.EbSubscribersHolder;

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
public class DescriptionPopup extends RollingPopup {
    public static final String TAG = DescriptionPopup.class.getCanonicalName();
    public static final String KEY = TAG;
    /** general elements of the popup (background sprite, close button, description image) */
    private DescriptionPopupBackground mDescriptionPopupBackground;
    /** unit building updater */
    private OffenceBuildingPopupUpdater mOffenceBuildingPopupUpdater;
    /** special buildings updater */
    private SpecialBuildingPopupUpdater mSpecialBuildingPopupUpdater;
    /** wealth building updater */
    private WealthBuildingPopupUpdater mWealthBuildingPopupUpdater;
    /** unit updater */
    private UnitPopupUpdater mUnitsDescriptionUpdater;
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
        super(scene, camera);
        int width = SizeConstants.GAME_FIELD_WIDTH;
        int height = SizeConstants.DESCRIPTION_POPUP_HEIGHT;
        mDescriptionPopupBackground = new DescriptionPopupBackground(
                SizeConstants.HALF_FIELD_WIDTH, -height / 2,
                width, height, vertexBufferObjectManager);
        mDescriptionPopupBackground
                .setTouchCallback(new TouchHelper.EntityTouchToChildren(mDescriptionPopupBackground));
        mBackgroundSprite = mDescriptionPopupBackground;
        attachChild(mBackgroundSprite);

        mOffenceBuildingPopupUpdater = new OffenceBuildingPopupUpdater(vertexBufferObjectManager, this);
        mWealthBuildingPopupUpdater = new WealthBuildingPopupUpdater(vertexBufferObjectManager, this);
        mSpecialBuildingPopupUpdater = new SpecialBuildingPopupUpdater(vertexBufferObjectManager, this);
        mUnitsDescriptionUpdater = new UnitPopupUpdater(vertexBufferObjectManager, this);
        mDefenceBuildingPopupUpdater = new DefenceBuildingPopupUpdater(vertexBufferObjectManager, this);
        init();
        EbSubscribersHolder.register(this);
    }

    @SuppressWarnings("unused")
    /** really used by {@link de.greenrobot.event.EventBus} */
    public void onEvent(final BuildingDescriptionShowEvent buildingDescriptionShowEvent) {
        onEvent();
        BuildingId buildingId = buildingDescriptionShowEvent.getObjectId();
        IPlayer player = PlayersHolder.getInstance().getElement(buildingDescriptionShowEvent.getPlayerName());
        BuildingDummy buildingDummy = player.getAlliance().getBuildingDummy(buildingId);
        IPopupUpdater popupUpdater;
        switch (buildingDummy.getBuildingType()) {
            case CREEP_BUILDING: {
                popupUpdater = mOffenceBuildingPopupUpdater;
                break;
            }
            case WEALTH_BUILDING: {
                popupUpdater = mWealthBuildingPopupUpdater;
                break;
            }
            case SPECIAL_BUILDING: {
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
        mDescriptionPopupBackground.updateDescription(popupUpdater, buildingId,
                player.getAlliance().getAllianceName(), player.getName());
        showPopup();
    }

    private void onEvent() {
        clear();
    }

    private void clear() {
        mOffenceBuildingPopupUpdater.clear();
        mUnitsDescriptionUpdater.clear();
        mWealthBuildingPopupUpdater.clear();
        mSpecialBuildingPopupUpdater.clear();
        mDefenceBuildingPopupUpdater.clear();
    }

    @SuppressWarnings("unused")
    /** really used by {@link de.greenrobot.event.EventBus} */
    public void onEvent(final UnitByBuildingDescriptionShowEvent unitByBuildingDescriptionShowEvent) {
        onEvent();
        Object objectId = unitByBuildingDescriptionShowEvent.getBuildingId();
        IPlayer player = PlayersHolder.getInstance().getElement(unitByBuildingDescriptionShowEvent.getPlayerName());
        mDescriptionPopupBackground.updateDescription(mUnitsDescriptionUpdater, objectId,
                player.getAlliance().getAllianceName(), player.getName());
        showPopup();
    }

    public static void loadResources(Context context, TextureManager textureManager) {
        ConstructionPopupButton.loadResources(context, textureManager);
        DescriptionPopupBackground.loadResources(context, textureManager);
        BaseBuildingPopupUpdater.loadResources(context, textureManager);
    }

    public static void loadFonts(FontManager fontManager, TextureManager textureManager) {
        DescriptionPopupBackground.loadFonts(fontManager, textureManager);
        BaseBuildingPopupUpdater.loadFonts(fontManager, textureManager);
        DescriptionText.loadFonts(fontManager, textureManager);
        Link.loadFonts(fontManager, textureManager);
    }
}
