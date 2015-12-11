package com.gmail.yaroslavlancelot.eafall.game.scene.hud;

import android.content.Context;
import android.graphics.Color;

import com.gmail.yaroslavlancelot.eafall.EaFallApplication;
import com.gmail.yaroslavlancelot.eafall.android.LoggerHelper;
import com.gmail.yaroslavlancelot.eafall.game.GameState;
import com.gmail.yaroslavlancelot.eafall.game.batching.BatchingKeys;
import com.gmail.yaroslavlancelot.eafall.game.batching.SpriteGroupHolder;
import com.gmail.yaroslavlancelot.eafall.game.configuration.mission.MissionConfig;
import com.gmail.yaroslavlancelot.eafall.game.constant.SizeConstants;
import com.gmail.yaroslavlancelot.eafall.game.constant.StringConstants;
import com.gmail.yaroslavlancelot.eafall.game.entity.TextureRegionHolder;
import com.gmail.yaroslavlancelot.eafall.game.events.SharedEvents;
import com.gmail.yaroslavlancelot.eafall.game.events.periodic.time.GameTime;
import com.gmail.yaroslavlancelot.eafall.game.player.IPlayer;
import com.gmail.yaroslavlancelot.eafall.game.player.PlayersHolder;
import com.gmail.yaroslavlancelot.eafall.game.popup.rolling.RollingPopupManager;
import com.gmail.yaroslavlancelot.eafall.game.popup.rolling.construction.ConstructionsPopup;
import com.gmail.yaroslavlancelot.eafall.game.popup.rolling.menu.MenuPopup;
import com.gmail.yaroslavlancelot.eafall.game.visual.buttons.ConstructionPopupButton;
import com.gmail.yaroslavlancelot.eafall.game.visual.buttons.MenuPopupButton;
import com.gmail.yaroslavlancelot.eafall.game.visual.font.FontHolder;
import com.gmail.yaroslavlancelot.eafall.game.visual.other.HealthBarCarcassSprite;
import com.gmail.yaroslavlancelot.eafall.general.locale.LocaleImpl;

import org.andengine.engine.camera.Camera;
import org.andengine.engine.camera.hud.HUD;
import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.AlphaModifier;
import org.andengine.entity.sprite.ButtonSprite;
import org.andengine.entity.text.Text;
import org.andengine.opengl.font.FontFactory;
import org.andengine.opengl.font.FontManager;
import org.andengine.opengl.font.IFont;
import org.andengine.opengl.texture.ITexture;
import org.andengine.opengl.texture.TextureManager;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.modifier.IModifier;
import org.andengine.util.modifier.ease.EaseStrongOut;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * In-game HUD.
 * <br/>
 * Customized to handle some not {@link HUD} operations
 *
 * @author Yaroslav Havrylovych
 */
public class ClientGameHud extends BaseGameHud {
    public final static String sFontKey = "hud_on_screen_text_font_key";
    // ===========================================================
    // Constants
    // ===========================================================
    private static final String TAG = ClientGameHud.class.getCanonicalName();
    private static final DecimalFormat TIME_FORMAT = new DecimalFormat("00");
    // ===========================================================
    // Fields
    // ===========================================================
    private ArrayList<HudGameValue> mLeftPart = new ArrayList<>(3);
    private ArrayList<HudGameValue> mRightPart = new ArrayList<>(3);
    private Text mHudText;
    private AlphaModifier mHudTextShowAlphaModifier = new AlphaModifier(.5f, 0, 1,
            EaseStrongOut.getInstance());
    private AlphaModifier mHudTextHideAlphaModifier = new AlphaModifier(.5f, 1, 0,
            EaseStrongOut.getInstance());

    // ===========================================================
    // Constructors
    // ===========================================================

    // ===========================================================
    // Getter & Setter
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

    @Override
    public void initHudElements(Camera camera, VertexBufferObjectManager vboManager,
                                MissionConfig missionConfig) {
        LoggerHelper.methodInvocation(TAG, "initHudElements");
        SpriteGroupHolder.attachSpriteGroups(this, BatchingKeys.BatchTag.GAME_HUD.value());
        //HUD text
        mHudText = new Text(SizeConstants.HALF_FIELD_WIDTH, SizeConstants.HALF_FIELD_HEIGHT / 3,
                FontHolder.getInstance().getElement(sFontKey), "", 50, vboManager);
        mHudTextHideAlphaModifier.addModifierListener(new IModifier.IModifierListener<IEntity>() {
            @Override
            public void onModifierStarted(final IModifier<IEntity> pModifier, final IEntity pItem) {
            }

            @Override
            public void onModifierFinished(final IModifier<IEntity> pModifier, final IEntity pItem) {
                mHudText.setVisible(false);
                mHudText.setIgnoreUpdate(true);
            }
        });
        attachChild(mHudText);
        //health carcass
        initHealthCarcass(vboManager);
        //menu
        initMainMenu(vboManager);
        //game values (oxygen, offence units limit, time)
        for (final IPlayer player : PlayersHolder.getInstance().getElements()) {
            boolean left = player.getPlanet().isLeft();
            List<HudGameValue> list = left ? mLeftPart : mRightPart;
            float xPos = left ? SizeConstants.HUD_VALUES_X_LEFT : SizeConstants.HUD_VALUES_X_RIGHT;
            if (player.getControlType().user()) {
                initPopups(player, camera, vboManager);
                initOxygen(player, list, xPos, vboManager);
                initMovableUnitsLimit(player, list, xPos, vboManager, missionConfig.getMovableUnitsLimit());
                initTimer(list, xPos, vboManager, missionConfig);
            } else {
                initMovableUnitsLimit(player, list, xPos, vboManager, missionConfig.getMovableUnitsLimit());
            }
        }
    }

    // ===========================================================
    // Methods for/from SuperClass/Interfaces
    // ===========================================================

    // ===========================================================
    // Methods
    // ===========================================================

    public void showHudText(int text) {
        showHudText(LocaleImpl.getInstance().getStringById(text));
    }

    public void showHudText(String text) {
        mHudText.unregisterEntityModifier(mHudTextHideAlphaModifier);
        mHudText.setText(text);
        mHudTextShowAlphaModifier.reset();
        mHudText.registerEntityModifier(mHudTextShowAlphaModifier);
        mHudText.setVisible(true);
        mHudText.setIgnoreUpdate(false);
    }

    public void hideHudText() {
        mHudText.unregisterEntityModifier(mHudTextShowAlphaModifier);
        mHudTextHideAlphaModifier.reset();
        mHudText.registerEntityModifier(mHudTextHideAlphaModifier);
    }

    private void initPopups(IPlayer player, Camera camera, VertexBufferObjectManager vboManager) {
        RollingPopupManager.init(player.getName(), this, camera, vboManager);
        //constructions button
        ConstructionPopupButton button = new ConstructionPopupButton(vboManager);
        button.setOnClickListener(new ButtonSprite.OnClickListener() {
            @Override
            public void onClick(ButtonSprite pButtonSprite, float pTouchAreaLocalX, float pTouchAreaLocalY) {
                if (GameState.isResumed()) {
                    RollingPopupManager.getPopup(ConstructionsPopup.KEY).triggerPopup();
                }
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
        final String key = player.getOxygenChangedKey();
        SharedEvents.addCallback(new SharedEvents.DataChangedCallback(key) {
            @Override
            public void callback(String callbackKey, Object value) {
                gameValue.setText(value.toString());
            }
        });
        list.add(gameValue);
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
                RollingPopupManager.getPopup(MenuPopup.KEY).showPopup();
            }
        });
        registerTouchArea(menuButton);
    }

    private void initHealthCarcass(VertexBufferObjectManager vertexManager) {
        HealthBarCarcassSprite healthBarCarcassSprite = new HealthBarCarcassSprite(vertexManager);
        SpriteGroupHolder.getGroup(BatchingKeys.PLAYER_HEALTH).attachChild(healthBarCarcassSprite);
    }

    /** time text on the screen (if timing enabled) initialization */
    private void initTimer(final List<HudGameValue> list, final float xPos,
                           final VertexBufferObjectManager vertexManager,
                           final MissionConfig missionConfig) {
        if (!missionConfig.isTimerEnabled()) {
            return;
        }
        int id = list.size();
        final HudGameValue gameValue = new HudGameValue(id, xPos, StringConstants.FILE_CLOCK_HUD,
                formatTime(missionConfig.getTime()), vertexManager);
        gameValue.setImageOffset(0, 5);
        gameValue.attachToParent(this);
        String callbackKey = GameTime.GAME_TIMER_TICK_KEY;
        SharedEvents.addCallback(new SharedEvents.DataChangedCallback(callbackKey) {
            @Override
            public void callback(final String key, final Object value) {
                gameValue.setText(formatTime((Integer) value));
            }
        });
    }

    /**
     * return formatted time from int value
     * <br/>
     * format:
     * mm:ss
     */
    private String formatTime(int time) {
        int min = time / 60;
        int sec = time % 60;
        return TIME_FORMAT.format(min) + ":" + TIME_FORMAT.format(sec);
    }

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
        final ITexture fontTexture = new BitmapTextureAtlas(textureManager, 512, 1024);
        IFont font = FontFactory.createFromAsset(fontManager, fontTexture,
                EaFallApplication.getContext().getAssets(), "fonts/MyriadPro-Regular.ttf",
                SizeConstants.HUD_ON_SCREEN_TEXT_FONT_SIZE,
                true, Color.WHITE);
        font.load();
        FontHolder.getInstance().addElement(sFontKey, font);
    }

    // ===========================================================
    // Inner and Anonymous Classes
    // ===========================================================
}
