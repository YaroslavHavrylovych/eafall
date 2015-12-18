package com.gmail.yaroslavlancelot.eafall.game.popup.rolling;

import android.content.Context;

import com.gmail.yaroslavlancelot.eafall.game.popup.rolling.menu.MenuPopup;
import com.gmail.yaroslavlancelot.eafall.general.SelfCleanable;

import org.andengine.engine.camera.Camera;
import org.andengine.entity.scene.Scene;
import org.andengine.opengl.font.FontManager;
import org.andengine.opengl.texture.TextureManager;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import java.util.HashMap;
import java.util.Map;

/** init and handel all application popups logic */
public class RollingPopupManager extends SelfCleanable {
    private static RollingPopupManager sRollingPopupManager;
    private Map<String, IRollingPopup> mPopups = new HashMap<>(3);

    private RollingPopupManager(Scene scene, Camera camera, VertexBufferObjectManager vertexManager) {
        IRollingPopup popup = new MenuPopup(scene, camera, vertexManager);
        mPopups.put(MenuPopup.KEY, popup);
    }

    public static RollingPopupManager getInstance() {
        return sRollingPopupManager;
    }

    /** return visible/open popup or null if such doesn't exist now */
    public static IRollingPopup getOpen() {
        for (IRollingPopup popup : getInstance().mPopups.values()) {
            if (popup.isShowing()) {
                return popup;
            }
        }
        return null;
    }

    @Override
    public void clear() {
        mPopups.clear();
        sRollingPopupManager = null;
    }

    public void addPopup(String key, IRollingPopup popup) {
        mPopups.put(key, popup);
    }

    /** return popup instance */
    public IRollingPopup getPopup(String key) {
        return mPopups.get(key);
    }

    public static void loadResource(Context context, TextureManager textureManager) {
        MenuPopup.loadResource(context, textureManager);
    }

    public static void loadFonts(FontManager fontManager, TextureManager textureManager) {
    }

    /**
     * Create an instance of RollingPopupManager. You can use it as singleton with {@link RollingPopupManager#getInstance()}
     *
     * @param scene         in general it's a Hud. Most of popups you will see in the Hud.
     * @param camera        for setting camera to popup
     * @param objectManager VertexBufferObjectManager
     * @return initiated popup with using {@link RollingPopupManager#getInstance()}
     */
    public static RollingPopupManager init(Scene scene, Camera camera, VertexBufferObjectManager objectManager) {
        sRollingPopupManager = new RollingPopupManager(scene, camera, objectManager);
        return getInstance();
    }
}
