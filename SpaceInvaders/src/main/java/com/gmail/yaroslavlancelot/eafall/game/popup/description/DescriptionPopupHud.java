package com.gmail.yaroslavlancelot.eafall.game.popup.description;

import android.content.Context;

import com.gmail.yaroslavlancelot.eafall.game.constant.Sizes;
import com.gmail.yaroslavlancelot.eafall.game.eventbus.description.BuildingDescriptionShowEvent;
import com.gmail.yaroslavlancelot.eafall.game.eventbus.description.UnitByBuildingDescriptionShowEvent;
import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.building.BuildingId;
import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.building.dummy.BuildingDummy;
import com.gmail.yaroslavlancelot.eafall.game.popup.PopupHud;
import com.gmail.yaroslavlancelot.eafall.game.popup.description.updater.IPopupUpdater;
import com.gmail.yaroslavlancelot.eafall.game.popup.description.updater.building.BaseBuildingPopupUpdater;
import com.gmail.yaroslavlancelot.eafall.game.popup.description.updater.building.creep.CreepBuildingPopupUpdater;
import com.gmail.yaroslavlancelot.eafall.game.popup.description.updater.building.defence.DefenceBuildingPopupUpdater;
import com.gmail.yaroslavlancelot.eafall.game.popup.description.updater.building.special.SpecialBuildingPopupUpdater;
import com.gmail.yaroslavlancelot.eafall.game.popup.description.updater.building.wealth.WealthBuildingPopupUpdater;
import com.gmail.yaroslavlancelot.eafall.game.popup.description.updater.unit.UnitPopupUpdater;
import com.gmail.yaroslavlancelot.eafall.game.team.ITeam;
import com.gmail.yaroslavlancelot.eafall.game.team.TeamsHolder;
import com.gmail.yaroslavlancelot.eafall.game.touch.StaticHelper;
import com.gmail.yaroslavlancelot.eafall.game.visual.text.DescriptionText;
import com.gmail.yaroslavlancelot.eafall.game.visual.text.Link;

import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.scene.Scene;
import org.andengine.opengl.font.FontManager;
import org.andengine.opengl.texture.TextureManager;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import de.greenrobot.event.EventBus;

/**
 * Handle logic of redrawing and showing/hiding description popup.
 * Appears in the bottom of the screen when you want to create a building
 * or see unit (other object) characteristics.
 */
public class DescriptionPopupHud extends PopupHud {
    public static final String TAG = DescriptionPopupHud.class.getCanonicalName();
    public static final String KEY = TAG;
    /** general elements of the popup (background sprite, close button, description image) */
    private DescriptionPopupBackground mDescriptionPopupBackground;
    /** creep building updater */
    private CreepBuildingPopupUpdater mCreepBuildingPopupUpdater;
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
     * @param vertexBufferObjectManager object manager to create inner elements
     */
    public DescriptionPopupHud(Scene scene, VertexBufferObjectManager vertexBufferObjectManager) {
        super(scene);
        mPopupRectangle = new Rectangle(0, Sizes.GAME_FIELD_HEIGHT - Sizes.DESCRIPTION_POPUP_HEIGHT,
                Sizes.GAME_FIELD_WIDTH, Sizes.DESCRIPTION_POPUP_HEIGHT, vertexBufferObjectManager);

        mCreepBuildingPopupUpdater = new CreepBuildingPopupUpdater(vertexBufferObjectManager, this);
        mWealthBuildingPopupUpdater = new WealthBuildingPopupUpdater(vertexBufferObjectManager, this);
        mSpecialBuildingPopupUpdater = new SpecialBuildingPopupUpdater(vertexBufferObjectManager, this);
        mUnitsDescriptionUpdater = new UnitPopupUpdater(vertexBufferObjectManager, this);
        mDefenceBuildingPopupUpdater = new DefenceBuildingPopupUpdater(vertexBufferObjectManager, this);

        mPopupRectangle.setTouchCallback(StaticHelper.EmptyTouch.getInstance());
        initBackgroundSprite(vertexBufferObjectManager);
        EventBus.getDefault().register(this);
    }

    private void initBackgroundSprite(VertexBufferObjectManager vertexBufferObjectManager) {
        mDescriptionPopupBackground = new DescriptionPopupBackground(
                mPopupRectangle.getWidth(), mPopupRectangle.getHeight(), vertexBufferObjectManager);
        mPopupRectangle.attachChild(mDescriptionPopupBackground);
    }

    public static void loadResources(Context context, TextureManager textureManager) {
        DescriptionPopupBackground.loadResources(context, textureManager);
        BaseBuildingPopupUpdater.loadResources(context, textureManager);
    }

    public static void loadFonts(FontManager fontManager, TextureManager textureManager) {
        DescriptionPopupBackground.loadFonts(fontManager, textureManager);
        BaseBuildingPopupUpdater.loadFonts(fontManager, textureManager);
        DescriptionText.loadFonts(fontManager, textureManager);
        Link.loadFonts(fontManager, textureManager);
    }

    @SuppressWarnings("unused")
    /** really used by {@link de.greenrobot.event.EventBus} */
    public void onEvent(final BuildingDescriptionShowEvent buildingDescriptionShowEvent) {
        onEvent();
        BuildingId buildingId = buildingDescriptionShowEvent.getObjectId();
        ITeam team = TeamsHolder.getInstance().getElement(buildingDescriptionShowEvent.getTeamName());
        BuildingDummy buildingDummy = team.getTeamRace().getBuildingDummy(buildingId);
        IPopupUpdater popupUpdater;
        switch (buildingDummy.getBuildingType()) {
            case CREEP_BUILDING: {
                popupUpdater = mCreepBuildingPopupUpdater;
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
                team.getTeamRace().getAllianceName(), team.getTeamName());
        showPopup();
    }

    private void onEvent() {
        clear();
    }

    private void clear() {
        mCreepBuildingPopupUpdater.clear();
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
        ITeam team = TeamsHolder.getInstance().getElement(unitByBuildingDescriptionShowEvent.getTeamName());
        mDescriptionPopupBackground.updateDescription(mUnitsDescriptionUpdater, objectId,
                team.getTeamRace().getAllianceName(), team.getTeamName());
        showPopup();
    }
}
