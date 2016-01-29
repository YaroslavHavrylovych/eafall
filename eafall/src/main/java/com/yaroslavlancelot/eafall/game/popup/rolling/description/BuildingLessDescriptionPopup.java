package com.yaroslavlancelot.eafall.game.popup.rolling.description;

import android.content.Context;

import com.yaroslavlancelot.eafall.game.constant.SizeConstants;
import com.yaroslavlancelot.eafall.game.events.aperiodic.ingame.description.UnitByBuildingDescriptionShowEvent;
import com.yaroslavlancelot.eafall.game.player.IPlayer;
import com.yaroslavlancelot.eafall.game.player.PlayersHolder;
import com.yaroslavlancelot.eafall.game.popup.rolling.RollingPopup;
import com.yaroslavlancelot.eafall.game.popup.rolling.description.updater.unit.BaseUnitPopupUpdater;
import com.yaroslavlancelot.eafall.game.touch.TouchHelper;
import com.yaroslavlancelot.eafall.game.visual.buttons.ConstructionPopupButton;
import com.yaroslavlancelot.eafall.game.visual.text.DescriptionText;
import com.yaroslavlancelot.eafall.game.visual.text.Link;
import com.yaroslavlancelot.eafall.general.EbSubscribersHolder;

import org.andengine.engine.camera.Camera;
import org.andengine.entity.scene.Scene;
import org.andengine.opengl.font.FontManager;
import org.andengine.opengl.texture.TextureManager;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

/**
 * Base abstract description popup which has only unit description updater area.
 *
 * @author Yaroslav Havrylovych
 */
public abstract class BuildingLessDescriptionPopup extends RollingPopup {
    public static final String TAG = BuildingLessDescriptionPopup.class.getCanonicalName();
    public static final String KEY = TAG;
    /** general elements of the popup (background sprite, close button, description image) */
    protected DescriptionPopupBackground mDescriptionPopupBackground;
    /** unit updater */
    private BaseUnitPopupUpdater mUnitsDescriptionUpdater;

    /**
     * single instance that's why it's private constructor
     *
     * @param scene                     popup attaches to this scene
     * @param camera                    game camera
     * @param vertexBufferObjectManager object manager to create inner elements
     */
    public BuildingLessDescriptionPopup(Scene scene, Camera camera,
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

        mUnitsDescriptionUpdater = createUnitPopupUpdater(vertexBufferObjectManager);
        init();
        EbSubscribersHolder.register(this);
    }

    protected abstract BaseUnitPopupUpdater createUnitPopupUpdater(VertexBufferObjectManager vboManager);

    /**
     * Basic verification before showing popup.
     * <p/>
     * Clears current displayed information.
     *
     * @param player player which initiates popup showing
     * @return true if popup has to be shown and false in other case
     */
    protected boolean onEvent(IPlayer player) {
        if (player.getControlType().user()) {
            clear();
            return true;
        }
        return false;
    }

    protected void clear() {
        mUnitsDescriptionUpdater.clear();
    }

    @SuppressWarnings("unused")
    /** really used by {@link de.greenrobot.event.EventBus} */
    public void onEvent(final UnitByBuildingDescriptionShowEvent unitByBuildingDescriptionShowEvent) {
        if (!onEvent(PlayersHolder.getPlayer(unitByBuildingDescriptionShowEvent.getPlayerName()))) {
            return;
        }
        Object objectId = unitByBuildingDescriptionShowEvent.getBuildingId();
        IPlayer player = PlayersHolder.getPlayer(unitByBuildingDescriptionShowEvent.getPlayerName());
        mDescriptionPopupBackground.setLeftBlockVisibility(true);
        mDescriptionPopupBackground.updateDescription(mUnitsDescriptionUpdater, objectId,
                player.getAlliance().getAllianceName(), player.getName());
        showPopup();
    }

    public static void loadResources(Context context, TextureManager textureManager) {
        DescriptionPopupBackground.loadResources(context, textureManager);
        ConstructionPopupButton.loadResources(context, textureManager);
    }

    public static void loadFonts(FontManager fontManager, TextureManager textureManager) {
        DescriptionPopupBackground.loadFonts(fontManager, textureManager);
        DescriptionText.loadFonts(fontManager, textureManager);
        Link.loadFonts(fontManager, textureManager);
    }
}
