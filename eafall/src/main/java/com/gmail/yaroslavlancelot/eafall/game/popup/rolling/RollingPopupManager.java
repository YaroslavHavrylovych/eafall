package com.gmail.yaroslavlancelot.eafall.game.popup.rolling;

import android.content.Context;

import com.gmail.yaroslavlancelot.eafall.game.popup.rolling.construction.ConstructionsPopup;
import com.gmail.yaroslavlancelot.eafall.game.popup.rolling.description.DescriptionPopup;
import com.gmail.yaroslavlancelot.eafall.game.popup.GameOverPopup;

import org.andengine.engine.camera.Camera;
import org.andengine.entity.scene.Scene;
import org.andengine.opengl.font.FontManager;
import org.andengine.opengl.texture.TextureManager;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import java.util.HashMap;
import java.util.Map;

/** init and handel all application popups logic */
public class RollingPopupManager {
    private static RollingPopupManager sRollingPopupManager;
    private Map<String, IRollingPopup> mPopups = new HashMap<>(3);

    private RollingPopupManager(String playerName, Scene scene, Camera camera, VertexBufferObjectManager vertexManager) {
        //description
        IRollingPopup popup = new DescriptionPopup(scene, camera, vertexManager);
        mPopups.put(DescriptionPopup.KEY, popup);
        // buildings
        popup = new ConstructionsPopup(playerName, scene, camera, vertexManager);
        mPopups.put(ConstructionsPopup.KEY, popup);
    }

    public static RollingPopupManager getInstance() {
        return sRollingPopupManager;
    }

    public static void loadResource(Context context, TextureManager textureManager) {
        ConstructionsPopup.loadResource(context, textureManager);
        DescriptionPopup.loadResources(context, textureManager);
    }

    public static void loadFonts(FontManager fontManager, TextureManager textureManager) {
        DescriptionPopup.loadFonts(fontManager, textureManager);
        GameOverPopup.loadFonts(fontManager, textureManager);
    }

    /**
     * Create an instance of RollingPopupManager. You can use it as singleton with {@link RollingPopupManager#getInstance()}
     *
     * @param playerName    popup is used by some user. User related to its player. This player name you need to pass.
     * @param scene         in general it's a Hud. Most of popups you will see in the Hud.
     * @param camera        for setting camera to popup
     * @param objectManager VertexBufferObjectManager
     * @return initiated popup with using {@link RollingPopupManager#getInstance()}
     */
    public static RollingPopupManager init(String playerName, Scene scene, Camera camera, VertexBufferObjectManager objectManager) {
        sRollingPopupManager = new RollingPopupManager(playerName, scene, camera, objectManager);
        return getInstance();
    }

    /** return popup instance */
    public static IRollingPopup getPopup(String key) {
        return getInstance().mPopups.get(key);
    }
}
