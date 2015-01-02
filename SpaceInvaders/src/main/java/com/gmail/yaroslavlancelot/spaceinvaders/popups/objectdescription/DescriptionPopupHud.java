package com.gmail.yaroslavlancelot.spaceinvaders.popups.objectdescription;

import android.content.Context;

import com.gmail.yaroslavlancelot.spaceinvaders.constants.SizeConstants;
import com.gmail.yaroslavlancelot.spaceinvaders.eventbus.description.BuildingDescriptionShowEvent;
import com.gmail.yaroslavlancelot.spaceinvaders.eventbus.description.UnitByBuildingDescriptionShowEvent;
import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.objects.buildings.BuildingId;
import com.gmail.yaroslavlancelot.spaceinvaders.popups.PopupHud;
import com.gmail.yaroslavlancelot.spaceinvaders.popups.objectdescription.updater.building.BuildingDescriptionPopupUpdater;
import com.gmail.yaroslavlancelot.spaceinvaders.popups.objectdescription.updater.unit.UnitsDescriptionPopupUpdater;
import com.gmail.yaroslavlancelot.spaceinvaders.teams.ITeam;
import com.gmail.yaroslavlancelot.spaceinvaders.teams.TeamsHolder;
import com.gmail.yaroslavlancelot.spaceinvaders.utils.TouchUtils;
import com.gmail.yaroslavlancelot.spaceinvaders.visualelements.text.Link;

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
    private DescriptionPopupBackgroundSprite mDescriptionPopupBackgroundSprite;
    /** building updater */
    private BuildingDescriptionPopupUpdater mBuildingDescriptionUpdater;
    /** unit updater */
    private UnitsDescriptionPopupUpdater mUnitsDescriptionUpdater;

    /**
     * single instance that's why it's private constructor
     *
     * @param scene                     popup attaches to this scene
     * @param vertexBufferObjectManager object manager to create inner elements
     */
    public DescriptionPopupHud(Scene scene, VertexBufferObjectManager vertexBufferObjectManager) {
        super(scene);
        mPopupRectangle = new Rectangle(0, SizeConstants.GAME_FIELD_HEIGHT - SizeConstants.DESCRIPTION_POPUP_HEIGHT,
                SizeConstants.GAME_FIELD_WIDTH, SizeConstants.DESCRIPTION_POPUP_HEIGHT, vertexBufferObjectManager);

        mBuildingDescriptionUpdater = new BuildingDescriptionPopupUpdater(vertexBufferObjectManager, this);
        mUnitsDescriptionUpdater = new UnitsDescriptionPopupUpdater(vertexBufferObjectManager, this);

        mPopupRectangle.setTouchCallback(TouchUtils.EmptyTouch.getInstance());
        initBackgroundSprite(vertexBufferObjectManager);
        EventBus.getDefault().register(this);
    }

    private void initBackgroundSprite(VertexBufferObjectManager vertexBufferObjectManager) {
        mDescriptionPopupBackgroundSprite = new DescriptionPopupBackgroundSprite(
                mPopupRectangle.getWidth(), mPopupRectangle.getHeight(), vertexBufferObjectManager);
        mPopupRectangle.attachChild(mDescriptionPopupBackgroundSprite);
    }

    public static void loadResources(Context context, TextureManager textureManager) {
        DescriptionPopupBackgroundSprite.loadResources(context, textureManager);
        BuildingDescriptionPopupUpdater.loadResources(context, textureManager);
    }

    public static void loadFonts(FontManager fontManager, TextureManager textureManager) {
        DescriptionPopupBackgroundSprite.loadFonts(fontManager, textureManager);
        BuildingDescriptionPopupUpdater.loadFonts(fontManager, textureManager);
        DescriptionText.loadFonts(fontManager, textureManager);
        Link.loadFonts(fontManager, textureManager);
    }

    @SuppressWarnings("unused")
    /** really used by {@link de.greenrobot.event.EventBus} */
    public void onEvent(final BuildingDescriptionShowEvent buildingDescriptionShowEvent) {
        onEvent();
        BuildingId buildingId = buildingDescriptionShowEvent.getObjectId();
        ITeam team = TeamsHolder.getInstance().getElement(buildingDescriptionShowEvent.getTeamName());
        mDescriptionPopupBackgroundSprite.updateDescription(mBuildingDescriptionUpdater, buildingId,
                team.getTeamRace().getAllianceName(), team.getTeamName());
        showPopup();
    }

    private void onEvent() {
        clear();
    }

    private void clear() {
        mBuildingDescriptionUpdater.clear();
        mUnitsDescriptionUpdater.clear();
    }

    @SuppressWarnings("unused")
    /** really used by {@link de.greenrobot.event.EventBus} */
    public void onEvent(final UnitByBuildingDescriptionShowEvent unitByBuildingDescriptionShowEvent) {
        onEvent();
        Object objectId = unitByBuildingDescriptionShowEvent.getBuildingId();
        ITeam team = TeamsHolder.getInstance().getElement(unitByBuildingDescriptionShowEvent.getTeamName());
        mDescriptionPopupBackgroundSprite.updateDescription(mUnitsDescriptionUpdater, objectId,
                team.getTeamRace().getAllianceName(), team.getTeamName());
        showPopup();
    }
}
