package com.gmail.yaroslavlancelot.spaceinvaders.popups.objectdescription;

import android.content.Context;

import com.gmail.yaroslavlancelot.spaceinvaders.eventbus.description.BuildingDescriptionShowEvent;
import com.gmail.yaroslavlancelot.spaceinvaders.eventbus.description.UnitDescriptionShowEvent;
import com.gmail.yaroslavlancelot.spaceinvaders.popups.objectdescription.updater.DescriptionUpdater;
import com.gmail.yaroslavlancelot.spaceinvaders.popups.objectdescription.updater.building.BuildingDescriptionUpdater;
import com.gmail.yaroslavlancelot.spaceinvaders.popups.objectdescription.updater.unit.UnitsDescriptionUpdater;
import com.gmail.yaroslavlancelot.spaceinvaders.teams.ITeam;
import com.gmail.yaroslavlancelot.spaceinvaders.teams.TeamsHolder;

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
public class DescriptionPopup {
    public static final String TAG = DescriptionPopup.class.getCanonicalName();
    /** Single instance. Each object to display redraw content as it needs. */
    private static volatile DescriptionPopup sDescriptionPopup;
    /** general elements of the popup (background sprite, close button, description image) */
    private DescriptionPopupBackgroundSprite mDescriptionPopupBackgroundSprite;
    /** building updater */
    private BuildingDescriptionUpdater mBuildingDescriptionUpdater;
    /** unit updater */
    private UnitsDescriptionUpdater mUnitsDescriptionUpdater;

    /**
     * single instance that's why it's private constructor
     *
     * @param vertexBufferObjectManager object manager to create inner elements
     * @param scene                     popup will be attached to this scene
     */
    private DescriptionPopup(VertexBufferObjectManager vertexBufferObjectManager, Scene scene) {
        mBuildingDescriptionUpdater = new BuildingDescriptionUpdater(vertexBufferObjectManager, scene);
        mUnitsDescriptionUpdater = new UnitsDescriptionUpdater(vertexBufferObjectManager, scene);
        initBackgroundSprite(vertexBufferObjectManager, scene, mBuildingDescriptionUpdater, mUnitsDescriptionUpdater);
        EventBus.getDefault().register(this);
    }

    private void initBackgroundSprite(VertexBufferObjectManager vertexBufferObjectManager, Scene scene, final DescriptionUpdater... updaters) {
        mDescriptionPopupBackgroundSprite = new DescriptionPopupBackgroundSprite(vertexBufferObjectManager, scene) {
            @Override
            void hide() {
                super.hide();
                for (DescriptionUpdater updater : updaters) {
                    updater.clear();
                }
            }
        };
    }

    /** singleton */
    public static synchronized DescriptionPopup getInstance() {
        return sDescriptionPopup;
    }

    /**
     * init this class so you can get its instance (singleton)
     *
     * @param objectManager object manager to create inner elements
     * @param scene         popup will be attached to this scene
     */
    public static synchronized DescriptionPopup init(VertexBufferObjectManager objectManager, Scene scene) {
        sDescriptionPopup =
                new DescriptionPopup(objectManager, scene);

        return sDescriptionPopup;
    }

    public static void loadResources(Context context, TextureManager textureManager) {
        DescriptionPopupBackgroundSprite.loadResources(context, textureManager);
        BuildingDescriptionUpdater.loadResources(context, textureManager);
    }

    public static void loadFonts(FontManager fontManager, TextureManager textureManager) {
        DescriptionPopupBackgroundSprite.loadFonts(fontManager, textureManager);
        BuildingDescriptionUpdater.loadFonts(fontManager, textureManager);
    }

    @SuppressWarnings("unused")
    /** really used by {@link de.greenrobot.event.EventBus} */
    public void onEvent(final BuildingDescriptionShowEvent buildingDescriptionShowEvent) {
        onEvent();
        int objectId = buildingDescriptionShowEvent.getObjectId();
        ITeam team = TeamsHolder.getInstance().getElement(buildingDescriptionShowEvent.getTeamName());
        mDescriptionPopupBackgroundSprite.updateDescription(mBuildingDescriptionUpdater, objectId,
                team.getTeamRace().getRaceName(), team.getTeamName());
        mDescriptionPopupBackgroundSprite.show();
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
    public void onEvent(final UnitDescriptionShowEvent unitDescriptionShowEvent) {
        onEvent();
        int objectId = unitDescriptionShowEvent.getUnitId();
        ITeam team = TeamsHolder.getInstance().getElement(unitDescriptionShowEvent.getTeamName());
        mDescriptionPopupBackgroundSprite.updateDescription(mUnitsDescriptionUpdater, objectId,
                team.getTeamRace().getRaceName(), team.getTeamName());
        mDescriptionPopupBackgroundSprite.show();
    }

    public DescriptionPopupBackgroundSprite getPopupSprite() {
        return mDescriptionPopupBackgroundSprite;
    }
}
