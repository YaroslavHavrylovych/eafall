package com.gmail.yaroslavlancelot.eafall.game.scene.hud;

import android.content.Context;

import com.gmail.yaroslavlancelot.eafall.android.LoggerHelper;
import com.gmail.yaroslavlancelot.eafall.game.SharedDataCallbacks;
import com.gmail.yaroslavlancelot.eafall.game.batching.BatchingKeys;
import com.gmail.yaroslavlancelot.eafall.game.batching.SpriteGroupHolder;
import com.gmail.yaroslavlancelot.eafall.game.configuration.Config;
import com.gmail.yaroslavlancelot.eafall.game.constant.SizeConstants;
import com.gmail.yaroslavlancelot.eafall.game.constant.StringConstants;
import com.gmail.yaroslavlancelot.eafall.game.entity.TextureRegionHolder;
import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.unit.dynamic.path.PathHelper;
import com.gmail.yaroslavlancelot.eafall.game.player.IPlayer;
import com.gmail.yaroslavlancelot.eafall.game.player.Player;
import com.gmail.yaroslavlancelot.eafall.game.player.PlayersHolder;
import com.gmail.yaroslavlancelot.eafall.game.popup.PopupManager;
import com.gmail.yaroslavlancelot.eafall.game.popup.construction.ConstructionsPopupHud;
import com.gmail.yaroslavlancelot.eafall.game.visual.buttons.ConstructionPopupButton;
import com.gmail.yaroslavlancelot.eafall.game.visual.buttons.MenuPopupButton;
import com.gmail.yaroslavlancelot.eafall.game.visual.other.HealthBarCarcassSprite;

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

/**
 * Customized to handle some not {@link HUD} operations
 *
 * @author Yaroslav Havrylovych
 */
//TODO move the whole hud resources here (player health bar [and buttons?])
public class EaFallHud extends HUD {
    // ===========================================================
    // Constants
    // ===========================================================
    private static final String TAG = EaFallHud.class.getCanonicalName();

    // ===========================================================
    // Fields
    // ===========================================================
    private ArrayList<HudGameValue> mLeftPart = new ArrayList<HudGameValue>(3);
    private ArrayList<HudGameValue> mRightPart = new ArrayList<HudGameValue>(3);

    // ===========================================================
    // Constructors
    // ===========================================================

    // ===========================================================
    // Getter & Setter
    // ===========================================================

    // ===========================================================
    // Methods for/from SuperClass/Interfaces
    // ===========================================================

    @Override
    public void setAlpha(final float pAlpha) {
        super.setAlpha(pAlpha);
        if (mChildren != null) {
            for (IEntity child : mChildren) {
                child.setAlpha(pAlpha);
            }
        }
    }

    @Override
    public void attachChild(final IEntity pEntity) throws IllegalStateException {
        super.attachChild(pEntity);
        pEntity.setAlpha(getAlpha());
    }

    // ===========================================================
    // Methods
    // ===========================================================

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

    private void initPopups(IPlayer player, Camera camera, VertexBufferObjectManager objectManager) {
        PopupManager.init(player.getName(), this, camera, objectManager);
        //constructions button
        ConstructionPopupButton button = new ConstructionPopupButton(objectManager);
        button.setOnClickListener(new ButtonSprite.OnClickListener() {
            @Override
            public void onClick(ButtonSprite pButtonSprite, float pTouchAreaLocalX, float pTouchAreaLocalY) {
                PopupManager.getPopup(ConstructionsPopupHud.KEY).triggerPopup();
            }
        });
        attachChild(button);
        registerTouchArea(button);
    }

    private void initOxygen(IPlayer player, List<HudGameValue> list, float x,
                            VertexBufferObjectManager objectManager) {
        int id = list.size();
        final HudGameValue gameValue = new HudGameValue(id, x, StringConstants.FILE_OXYGEN_HUD,
                "0", objectManager);
        gameValue.setImageOffset(5, 6);
        gameValue.attachToParent(this);
        final String key = ((Player) player).OXYGEN_CHANGED_CALLBACK_KEY;
        SharedDataCallbacks.addCallback(new SharedDataCallbacks.DataChangedCallback(key) {
            @Override
            public void callback(String callbackKey, Object value) {
                gameValue.setText(value.toString());
            }
        });
        list.add(gameValue);
    }

    private void initMovableUnitsLimit(IPlayer player, List<HudGameValue> list, float x,
                                       VertexBufferObjectManager objectManager) {
        final String textSuffix = "/" + Config.getConfig().getMovableUnitsLimit();
        int id = list.size();
        final HudGameValue gameValue = new HudGameValue(id, x, StringConstants.FILE_SHUTTLE_HUD,
                "0" + textSuffix, objectManager);
        gameValue.setImageOffset(0, 5);
        gameValue.attachToParent(this);
        final String key = ((Player) player).MOVABLE_UNITS_AMOUNT_CHANGED_CALLBACK_KEY;
        SharedDataCallbacks.addCallback(new SharedDataCallbacks.DataChangedCallback(key) {
            @Override
            public void callback(String callbackKey, Object value) {
                gameValue.setText(value.toString() + textSuffix);
            }
        });
        list.add(gameValue);
    }

    private void initMainMenu(VertexBufferObjectManager objectManager) {
        MenuPopupButton menuButton = new MenuPopupButton(objectManager);
        attachChild(menuButton);
    }

    private void initHealthCarcass(VertexBufferObjectManager vertexManager) {
        HealthBarCarcassSprite healthBarCarcassSprite = new HealthBarCarcassSprite(vertexManager);
        SpriteGroupHolder.getGroup(BatchingKeys.PLAYER_HEALTH).attachChild(healthBarCarcassSprite);
    }

    public void initHudElements(Camera camera, VertexBufferObjectManager vertexManager) {
        LoggerHelper.methodInvocation(TAG, "initHudElements");
        SpriteGroupHolder.attachSpriteGroups(this, BatchingKeys.BatchTag.GAME_HUD.value());
        //health carcass
        initHealthCarcass(vertexManager);
        //menu
        initMainMenu(vertexManager);
        //game values (oxygen, movable units limit, time)
        for (final IPlayer player : PlayersHolder.getInstance().getElements()) {
            boolean left = PathHelper.isLeftSide(player.getPlanet().getSpawnPointX());
            List<HudGameValue> list = left ? mLeftPart : mRightPart;
            float xPos = left ? SizeConstants.HUD_VALUES_X_LEFT : SizeConstants.HUD_VALUES_X_RIGHT;
            if (IPlayer.ControlType.isUserControlType(player.getControlType())) {
                initPopups(player, camera, vertexManager);
                initOxygen(player, list, xPos, vertexManager);
                initMovableUnitsLimit(player, list, xPos, vertexManager);
                player.setMoney(5000);
            } else {
                initMovableUnitsLimit(player, list, xPos, vertexManager);
            }
        }
    }

    // ===========================================================
    // Inner and Anonymous Classes
    // ===========================================================
}
