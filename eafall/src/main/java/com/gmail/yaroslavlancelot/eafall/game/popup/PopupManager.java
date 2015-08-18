package com.gmail.yaroslavlancelot.eafall.game.popup;

import android.content.Context;

import com.gmail.yaroslavlancelot.eafall.game.popup.construction.ConstructionsPopupHud;
import com.gmail.yaroslavlancelot.eafall.game.popup.description.DescriptionPopupHud;
import com.gmail.yaroslavlancelot.eafall.game.popup.information.GameOverPopup;

import org.andengine.engine.camera.Camera;
import org.andengine.entity.scene.Scene;
import org.andengine.opengl.font.FontManager;
import org.andengine.opengl.texture.TextureManager;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import java.util.HashMap;
import java.util.Map;

/** init and handel all application popups logic */
public class PopupManager {
    private static PopupManager sPopupManager;
    private Map<String, PopupHud> mPopups = new HashMap<>(3);


    private PopupManager(String playerName, Scene scene, Camera camera, VertexBufferObjectManager vertexManager) {
        //description
        PopupHud popup = new DescriptionPopupHud(scene, vertexManager);
        popup.setCamera(camera);
        mPopups.put(DescriptionPopupHud.KEY, popup);
        // buildings
        popup = new ConstructionsPopupHud(playerName, scene, vertexManager);
        popup.setCamera(camera);
        mPopups.put(ConstructionsPopupHud.KEY, popup);
        //game over
        popup = new GameOverPopup(scene, vertexManager);
        popup.setCamera(camera);
        mPopups.put(GameOverPopup.KEY, popup);
    }

    public static PopupManager getInstance() {
        return sPopupManager;
    }

    public static void loadResource(Context context, TextureManager textureManager) {
        ConstructionsPopupHud.loadResource(context, textureManager);
        DescriptionPopupHud.loadResources(context, textureManager);
    }

    public static void loadFonts(FontManager fontManager, TextureManager textureManager) {
        DescriptionPopupHud.loadFonts(fontManager, textureManager);
        GameOverPopup.loadFonts(fontManager, textureManager);
    }

    /**
     * Create an instance of PopupManager. You can use it as singleton with {@link PopupManager#getInstance()}
     *
     * @param playerName    popup is used by some user. User related to its player. This player name you need to pass.
     * @param scene         in general it's a Hud. Most of popups you will see in the Hud.
     * @param camera        for setting camera to popup
     * @param objectManager VertexBufferObjectManager
     * @return initiated popup with using {@link PopupManager#getInstance()}
     */
    public static PopupManager init(String playerName, Scene scene, Camera camera, VertexBufferObjectManager objectManager) {
        sPopupManager = new PopupManager(playerName, scene, camera, objectManager);
        return getInstance();
    }

    /** return needed popup instance */
    public static IPopup getPopup(String key) {
        PopupManager popupManager = getInstance();
        return getInstance() == null ? null : popupManager.mPopups.get(key);
    }
}
