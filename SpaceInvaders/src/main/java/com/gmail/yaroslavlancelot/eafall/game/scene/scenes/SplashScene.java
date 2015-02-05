package com.gmail.yaroslavlancelot.eafall.game.scene.scenes;

import android.content.Context;

import com.gmail.yaroslavlancelot.eafall.game.constant.Sizes;
import com.gmail.yaroslavlancelot.eafall.game.constant.StringsAndPath;
import com.gmail.yaroslavlancelot.eafall.game.entity.TextureRegionHolder;

import org.andengine.engine.Engine;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.texture.TextureManager;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;

/**
 * Game scene
 * <br/>
 * Shown while preparing main scene and waiting other players.
 */
public class SplashScene extends Scene {
    private static final String TAG = GameScene.class.getCanonicalName();

    public SplashScene(Engine engine) {
        Sprite splash = new Sprite(0, 0, TextureRegionHolder.getInstance()
                .getElement(StringsAndPath.KEY_SPLASH_SCREEN), engine.getVertexBufferObjectManager());
        splash.setScale(4f);
        splash.setPosition((Sizes.GAME_FIELD_WIDTH - splash.getWidth()) * 0.5f,
                (Sizes.GAME_FIELD_HEIGHT - splash.getHeight()) * 0.5f);
        attachChild(splash);
    }

    public static void loadResources(Context context, TextureManager textureManager) {
        BitmapTextureAtlas splashTextureAtlas =
                new BitmapTextureAtlas(textureManager, 128, 32, TextureOptions.DEFAULT);
        TextureRegionHolder.getInstance().addElement(
                StringsAndPath.KEY_SPLASH_SCREEN, BitmapTextureAtlasTextureRegionFactory.createFromAsset(
                        splashTextureAtlas, context, StringsAndPath.FILE_SPLASH_SCREEN, 0, 0)
        );
        splashTextureAtlas.load();
    }

}
