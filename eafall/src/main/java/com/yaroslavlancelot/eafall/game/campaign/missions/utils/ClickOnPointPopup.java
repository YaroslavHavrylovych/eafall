package com.yaroslavlancelot.eafall.game.campaign.missions.utils;

import android.content.Context;
import android.graphics.Color;

import com.yaroslavlancelot.eafall.EaFallApplication;
import com.yaroslavlancelot.eafall.game.constant.SizeConstants;
import com.yaroslavlancelot.eafall.game.constant.StringConstants;
import com.yaroslavlancelot.eafall.game.entity.TextureRegionHolder;
import com.yaroslavlancelot.eafall.game.popup.PopupScene;
import com.yaroslavlancelot.eafall.game.scene.hud.HudGameValue;
import com.yaroslavlancelot.eafall.game.touch.TouchHelper;
import com.yaroslavlancelot.eafall.game.visual.font.FontHolder;
import com.yaroslavlancelot.eafall.general.locale.LocaleImpl;

import org.andengine.engine.camera.Camera;
import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.font.FontFactory;
import org.andengine.opengl.font.FontManager;
import org.andengine.opengl.font.IFont;
import org.andengine.opengl.texture.ITexture;
import org.andengine.opengl.texture.TextureManager;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.math.MathUtils;

/**
 * Used to display hints to the user
 *
 * @author Yaroslav Havrylovych
 */
public class ClickOnPointPopup extends PopupScene {
    // ===========================================================
    // Constants
    // ===========================================================
    public final static String sFontKey = "popup_clik_on_point_text_font_key";
    public final static int MAXIMUM_HUD_TEXT_CHARACTERS = 100;

    // ===========================================================
    // Fields
    // ===========================================================
    private Text mText1;
    private Sprite mPointer1;
    private Sprite mPointer2;

    // ===========================================================
    // Constructors
    // ===========================================================
    public ClickOnPointPopup(Scene scene, Camera camera, VertexBufferObjectManager vboManager) {
        super(scene, camera);
        mText1 = new Text(SizeConstants.HALF_FIELD_WIDTH, SizeConstants.HALF_FIELD_HEIGHT / 3,
                FontHolder.getInstance().getElement(sFontKey), "",
                MAXIMUM_HUD_TEXT_CHARACTERS, vboManager);
        attachChild(mText1);
        mPointer1 = new Sprite(0, 0,
                TextureRegionHolder.getRegion(StringConstants.FILE_TUTORIAL_POINTER), vboManager);
        attachChild(mPointer1);
        mPointer2 = new Sprite(0, 0,
                TextureRegionHolder.getRegion(StringConstants.FILE_TUTORIAL_POINTER), vboManager);
        attachChild(mPointer2);
        hideAll();
    }

    // ===========================================================
    // Getter & Setter
    // ===========================================================

    // ===========================================================
    // Methods for/from SuperClass/Interfaces
    // ===========================================================
    @Override
    public void showPopup() {
        super.showPopup();
    }

    @Override
    public void hidePopup() {
        hideAll();
        super.hidePopup();
    }

    // ===========================================================
    // Methods
    // ===========================================================
    private void hideAll() {
        mPointer1.setVisible(false);
        mPointer2.setVisible(false);
        mText1.setVisible(false);
    }

    public void initText1(float x, float y, int stringId) {
        initText1(x, y, LocaleImpl.getInstance().getStringById(stringId));
    }

    public void initText1(float x, float y, String text) {
        mText1.setVisible(true);
        mText1.setPosition(x, y);
        mText1.setText(text);
    }

    public void initPointer1(float x, float y, float angle) {
        mPointer1.setVisible(true);
        mPointer1.setPosition(x, y);
        mPointer1.setRotation(angle);
    }

    public void initPointer2(float x, float y, float angle) {
        mPointer2.setVisible(true);
        mPointer2.setPosition(x, y);
        mPointer2.setRotation(angle);
    }

    public void setClickableArea(final float x_c, final float y_c, final float half_width, final float half_height) {
        setOnSceneTouchListener(new TouchHelper.SceneTouchListener() {
            @Override
            public void click(final float x, final float y) {
                super.click(x, y);
                if (MathUtils.isInBounds(x_c - half_width, x_c + half_width, x) &&
                        MathUtils.isInBounds(y_c - half_height, y_c + half_height, y)) {
                    hidePopup();
                }
            }
        });
    }

    public void passClickEvent(final float x_c, final float y_c, final float half_width, final float half_height) {
        setOnSceneTouchListener(new IOnSceneTouchListener() {
            @Override
            public boolean onSceneTouchEvent(final Scene pScene, final TouchEvent pSceneTouchEvent) {
                float x = pSceneTouchEvent.getX();
                float y = pSceneTouchEvent.getY();
                if (MathUtils.isInBounds(x_c - half_width, x_c + half_width, x) &&
                        MathUtils.isInBounds(y_c - half_height, y_c + half_height, y)) {
                    return false;
                }
                return true;
            }
        });
    }

    public static void loadResources(Context context, TextureManager textureManager) {
        BitmapTextureAtlas textureAtlas = new BitmapTextureAtlas(textureManager,
                SizeConstants.TUTORIAL_POINTER_WIDTH,
                SizeConstants.TUTORIAL_POINTER_HEIGHT, TextureOptions.BILINEAR);
        TextureRegionHolder.addElementFromAssets(
                StringConstants.FILE_TUTORIAL_POINTER, textureAtlas, context, 0, 0);
        textureAtlas.load();
    }


    public static void loadFonts(FontManager fontManager, TextureManager textureManager) {
        HudGameValue.loadFonts(fontManager, textureManager);
        final ITexture fontTexture = new BitmapTextureAtlas(textureManager, 512, 1024);
        IFont font = FontFactory.createFromAsset(fontManager, fontTexture,
                EaFallApplication.getContext().getAssets(), "fonts/MyriadPro-Regular.ttf",
                SizeConstants.TUTORIAL_TEXT_FONT_SIZE,
                true, Color.WHITE);
        font.load();
        FontHolder.getInstance().addElement(sFontKey, font);
    }

    // ===========================================================
    // Inner and Anonymous Classes
    // ===========================================================
}
