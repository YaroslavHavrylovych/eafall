package com.gmail.yaroslavlancelot.eafall.game.popup.rolling.menu;

import android.content.Context;

import com.gmail.yaroslavlancelot.eafall.R;
import com.gmail.yaroslavlancelot.eafall.game.GameState;
import com.gmail.yaroslavlancelot.eafall.game.constant.SizeConstants;
import com.gmail.yaroslavlancelot.eafall.game.constant.StringConstants;
import com.gmail.yaroslavlancelot.eafall.game.entity.TextureRegionHolder;
import com.gmail.yaroslavlancelot.eafall.game.events.aperiodic.ingame.PauseGameEvent;
import com.gmail.yaroslavlancelot.eafall.game.popup.rolling.RollingPopup;
import com.gmail.yaroslavlancelot.eafall.game.touch.TouchHelper;
import com.gmail.yaroslavlancelot.eafall.game.visual.buttons.TextButton;
import com.gmail.yaroslavlancelot.eafall.general.locale.LocaleImpl;

import org.andengine.engine.camera.Camera;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.ButtonSprite;
import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.texture.TextureManager;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import de.greenrobot.event.EventBus;

/**
 * @author Yaroslav Havrylovych
 */
public class MenuPopup extends RollingPopup {
    // ===========================================================
    // Constants
    // ===========================================================
    public static final String TAG = MenuPopup.class.getCanonicalName();
    public static final String KEY = TAG;

    // ===========================================================
    // Fields
    // ===========================================================
    private TextButton mPauseButton;
    private TextButton mMissionButton;
    private TextButton mSettingsButton;
    private TextButton mExitButton;

    // ===========================================================
    // Constructors
    // ===========================================================
    public MenuPopup(Scene scene, Camera camera, VertexBufferObjectManager vboManager) {
        this(scene, camera, vboManager, false);
    }

    private MenuPopup(Scene scene, Camera camera, VertexBufferObjectManager vboManager,
                      boolean rollingFromBottom) {
        super(scene, camera, rollingFromBottom);
        int width = SizeConstants.MENU_POPUP_WIDTH;
        int height = SizeConstants.MENU_POPUP_HEIGHT;
        mBackgroundSprite = new Sprite(SizeConstants.HALF_FIELD_WIDTH,
                SizeConstants.GAME_FIELD_HEIGHT + height / 2,
                width, height,
                TextureRegionHolder.getRegion(StringConstants.FILE_MENU_POPUP_BACKGROUND),
                vboManager);
        mBackgroundSprite.setTouchCallback(new TouchHelper.EntityTouchToChildren(mBackgroundSprite));
        attachChild(mBackgroundSprite);
        initButtons(vboManager);
        init();
    }

    // ===========================================================
    // Getter & Setter
    // ===========================================================

    // ===========================================================
    // Methods for/from SuperClass/Interfaces
    // ===========================================================

    // ===========================================================
    // Methods
    // ===========================================================
    private void initButtons(VertexBufferObjectManager vboManager) {
        mPauseButton = createButton(R.string.menu_pause, 0, vboManager);
        initPauseButton(mPauseButton);
        mMissionButton = createButton(R.string.menu_mission, 1, vboManager);
        initMissionButton(mMissionButton);
        mSettingsButton = createButton(R.string.menu_settings, 2, vboManager);
        initSettingsButton(mSettingsButton);
        mExitButton = createButton(R.string.menu_exit, 3, vboManager);
        initExitButton(mExitButton);
    }

    private void initPauseButton(final TextButton button) {
        button.setOnClickListener(new ButtonSprite.OnClickListener() {
            @Override
            public void onClick(final ButtonSprite pTextButton, final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
                boolean newState;
                if (GameState.isPaused()) {
                    button.setText(R.string.menu_pause);
                    newState = false;
                } else {
                    button.setText(R.string.menu_resume);
                    newState = true;
                }
                EventBus.getDefault().post(new PauseGameEvent(newState));
            }
        });
        mBackgroundSprite.attachChild(button);
    }

    private void initMissionButton(TextButton button) {
        button.setOnClickListener(new ButtonSprite.OnClickListener() {
            @Override
            public void onClick(final ButtonSprite pTextButton, final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
                //TODO mission
            }
        });
        mBackgroundSprite.attachChild(button);
    }

    private void initSettingsButton(TextButton button) {
        button.setOnClickListener(new ButtonSprite.OnClickListener() {
            @Override
            public void onClick(final ButtonSprite pTextButton, final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
                //TODO settings
            }
        });
        mBackgroundSprite.attachChild(button);
    }

    private void initExitButton(TextButton button) {
        button.setOnClickListener(new ButtonSprite.OnClickListener() {
            @Override
            public void onClick(final ButtonSprite pTextButton, final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
                setStateChangeListener(new StateChangingListener() {
                    @Override
                    public void onShowed() {
                    }

                    @Override
                    public void onHided() {
                        //TODO show confirmation
                    }
                });
                hidePopup();
            }
        });
        mBackgroundSprite.attachChild(button);
    }

    private TextButton createButton(int stringId, int position, VertexBufferObjectManager vboManager) {
        float height = SizeConstants.MENU_POPUP_BUTTON_HEIGHT / 2;
        float width = SizeConstants.MENU_POPUP_BUTTON_WIDTH;
        float popup_height = SizeConstants.MENU_POPUP_HEIGHT;
        float popup_width = SizeConstants.MENU_POPUP_WIDTH;
        TextButton button = new TextButton(vboManager,
                (ITiledTextureRegion) TextureRegionHolder.getRegion(StringConstants.FILE_MENU_POPUP_BUTTON),
                width, height, popup_width / 2,
                popup_height - SizeConstants.MENU_POPUP_FIRST_BUTTON_Y
                        - height / 2 - position * height);
        button.setFixedSize(true);
        button.setText(LocaleImpl.getInstance().getStringById(stringId));
        return button;
    }

    public static void loadResource(Context context, TextureManager textureManager) {
        BitmapTextureAtlas textureAtlas = new BitmapTextureAtlas(textureManager,
                SizeConstants.MENU_POPUP_WIDTH,
                SizeConstants.MENU_POPUP_HEIGHT
                        + SizeConstants.BETWEEN_TEXTURES_PADDING
                        + SizeConstants.MENU_POPUP_BUTTON_HEIGHT,
                TextureOptions.DEFAULT);
        TextureRegionHolder.addElementFromAssets(StringConstants.FILE_MENU_POPUP_BACKGROUND,
                textureAtlas, context, 0, 0);
        TextureRegionHolder.addTiledElementFromAssets(StringConstants.FILE_MENU_POPUP_BUTTON,
                textureAtlas, context,
                0, SizeConstants.MENU_POPUP_HEIGHT + SizeConstants.BETWEEN_TEXTURES_PADDING,
                1, 2);
        textureAtlas.load();
    }

    // ===========================================================
    // Inner and Anonymous Classes
    // ===========================================================
}
