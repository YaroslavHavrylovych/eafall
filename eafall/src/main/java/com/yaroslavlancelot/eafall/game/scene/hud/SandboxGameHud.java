package com.yaroslavlancelot.eafall.game.scene.hud;

import android.content.Context;

import com.yaroslavlancelot.eafall.game.batching.BatchingKeys;
import com.yaroslavlancelot.eafall.game.batching.SpriteGroupHolder;
import com.yaroslavlancelot.eafall.game.configuration.mission.MissionConfig;
import com.yaroslavlancelot.eafall.game.constant.SizeConstants;
import com.yaroslavlancelot.eafall.game.constant.StringConstants;
import com.yaroslavlancelot.eafall.game.entity.TextureRegionHolder;
import com.yaroslavlancelot.eafall.game.entity.gameobject.building.BuildingId;
import com.yaroslavlancelot.eafall.game.events.SharedEvents;
import com.yaroslavlancelot.eafall.game.events.aperiodic.ingame.description.UnitByBuildingDescriptionShowEvent;
import com.yaroslavlancelot.eafall.game.player.IPlayer;
import com.yaroslavlancelot.eafall.game.player.PlayersHolder;
import com.yaroslavlancelot.eafall.game.popup.rolling.IRollingPopup;
import com.yaroslavlancelot.eafall.game.popup.rolling.RollingPopupManager;
import com.yaroslavlancelot.eafall.game.popup.rolling.menu.MenuPopup;
import com.yaroslavlancelot.eafall.game.sandbox.intents.SandboxIntent;
import com.yaroslavlancelot.eafall.game.sandbox.popup.SandboxDescriptionPopup;
import com.yaroslavlancelot.eafall.game.visual.buttons.ConstructionPopupButton;
import com.yaroslavlancelot.eafall.game.visual.buttons.MenuPopupButton;

import org.andengine.engine.camera.Camera;
import org.andengine.engine.camera.hud.HUD;
import org.andengine.entity.IEntity;
import org.andengine.entity.sprite.ButtonSprite;
import org.andengine.opengl.font.FontManager;
import org.andengine.opengl.texture.TextureManager;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * Custom Sandbox HUD.
 * <br/>
 * Customized to handle some not {@link HUD} operations:
 * <ul>
 * <li>init's on screen text and hud elements</li>
 * <li>passes selected unit screen to the game popup</li>
 * <li>load's resources</li>
 * </ul>
 *
 * @author Yaroslav Havrylovych
 */
public abstract class SandboxGameHud extends BaseGameHud {
    // ===========================================================
    // Constants
    // ===========================================================
    private static final String TAG = SandboxGameHud.class.getCanonicalName();

    // ===========================================================
    // Fields
    // ===========================================================
    private ArrayList<HudGameValue> mLeftPart = new ArrayList<>(3);
    private ArrayList<HudGameValue> mRightPart = new ArrayList<>(3);

    // ===========================================================
    // Constructors
    // ===========================================================

    // ===========================================================
    // Getter & Setter
    // ===========================================================

    // ===========================================================
    // Methods
    // ===========================================================
    protected abstract int getCurrentUnitId();

    @Override
    public void attachChild(final IEntity pEntity) throws IllegalStateException {
        super.attachChild(pEntity);
        pEntity.setAlpha(getAlpha());
    }

    // ===========================================================
    // Methods for/from SuperClass/Interfaces
    // ===========================================================

    @Override
    public void initHudElements(Camera camera, VertexBufferObjectManager vboManager,
                                MissionConfig missionConfig) {
        //TODO logger was here
        SpriteGroupHolder.attachSpriteGroups(this, BatchingKeys.BatchTag.GAME_HUD.value());
        //menu
        initMainMenu(vboManager);
        //game values (oxygen, offence units limit, time)
        for (final IPlayer player : PlayersHolder.getInstance().getElements()) {
            boolean left = player.getName().equals(SandboxIntent.FIRST_PLAYER_NAME);
            List<HudGameValue> list = left ? mLeftPart : mRightPart;
            float xPos = left ? SizeConstants.HUD_VALUES_X_LEFT : SizeConstants.HUD_VALUES_X_RIGHT;
            initMovableUnitsLimit(player, list, xPos, vboManager, missionConfig.getMovableUnitsLimit());
        }
        initPopups(camera, vboManager);
    }

    private void initPopups(Camera camera, VertexBufferObjectManager vboManager) {
        RollingPopupManager.init(this, camera, vboManager);
        //description
        IRollingPopup popup = new SandboxDescriptionPopup(this, camera, vboManager);
        RollingPopupManager.getInstance().addPopup(SandboxDescriptionPopup.KEY, popup);
        //constructions button
        ConstructionPopupButton button = new ConstructionPopupButton(vboManager);
        button.setOnClickListener(new ButtonSprite.OnClickListener() {
            @Override
            public void onClick(ButtonSprite pButtonSprite, float pTouchAreaLocalX, float pTouchAreaLocalY) {
                EventBus.getDefault().post(new UnitByBuildingDescriptionShowEvent(
                        BuildingId.makeId((getCurrentUnitId() / 10) * 10, getCurrentUnitId() % 10),
                        SandboxIntent.FIRST_PLAYER_NAME));
                RollingPopupManager.getInstance().getPopup(SandboxDescriptionPopup.KEY).triggerPopup();
            }
        });
        attachChild(button);
        registerTouchArea(button);
    }

    private void initMovableUnitsLimit(IPlayer player, List<HudGameValue> list, float x,
                                       VertexBufferObjectManager objectManager,
                                       int movableUnitsLimit) {
        final String textSuffix = "/" + movableUnitsLimit;
        int id = list.size();
        final HudGameValue gameValue = new HudGameValue(id, x, StringConstants.FILE_SHUTTLE_HUD,
                "0" + textSuffix, objectManager);
        gameValue.setImageOffset(0, 5);
        gameValue.attachToParent(this);
        final String key = player.getMovableUnitsAmountChangedKey();
        SharedEvents.addCallback(new SharedEvents.DataChangedCallback(key) {
            @Override
            public void callback(String callbackKey, Object value) {
                gameValue.setText(value.toString() + textSuffix);
            }
        });
        list.add(gameValue);
    }

    /** attaches main menu buttons to the scene and init`s click listener */
    private void initMainMenu(VertexBufferObjectManager objectManager) {
        MenuPopupButton menuButton = new MenuPopupButton(objectManager);
        attachChild(menuButton);
        menuButton.setOnClickListener(new ButtonSprite.OnClickListener() {
            @Override
            public void onClick(final ButtonSprite pButtonSprite, final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
                RollingPopupManager.getInstance().getPopup(MenuPopup.KEY).showPopup();
            }
        });
        registerTouchArea(menuButton);
    }

    //TODO remove everything but units limit
    public static void loadResource(Context context, TextureManager textureManager) {
        int padding = SizeConstants.BETWEEN_TEXTURES_PADDING;
        int biggestSide = 112;
        BitmapTextureAtlas textureAtlas = new BitmapTextureAtlas(
                textureManager,
                biggestSide + padding, biggestSide + padding, TextureOptions.BILINEAR);
        TextureRegionHolder.addElementFromAssets(StringConstants.FILE_OXYGEN,
                textureAtlas, context, 0, 0);
        TextureRegionHolder.addElementFromAssets(StringConstants.FILE_OXYGEN_HUD,
                textureAtlas, context, SizeConstants.ICON_OXYGEN + padding, 0);
        TextureRegionHolder.addElementFromAssets(StringConstants.FILE_CLOCK_HUD,
                textureAtlas, context, 0, SizeConstants.ICON_OXYGEN + padding);
        TextureRegionHolder.addElementFromAssets(StringConstants.FILE_SHUTTLE_HUD,
                textureAtlas, context,
                SizeConstants.ICON_OXYGEN + padding, SizeConstants.ICON_OXYGEN + padding);
        textureAtlas.load();
    }

    public static void loadFonts(FontManager fontManager, TextureManager textureManager) {
        HudGameValue.loadFonts(fontManager, textureManager);
    }

    // ===========================================================
    // Inner and Anonymous Classes
    // ===========================================================
}
