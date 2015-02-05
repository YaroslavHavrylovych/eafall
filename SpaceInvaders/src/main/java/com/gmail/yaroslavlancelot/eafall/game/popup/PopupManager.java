package com.gmail.yaroslavlancelot.eafall.game.popup;

import android.content.Context;

import com.gmail.yaroslavlancelot.eafall.game.popup.construction.BuildingsPopupHud;
import com.gmail.yaroslavlancelot.eafall.game.visual.buttons.ConstructionPopupButton;
import com.gmail.yaroslavlancelot.eafall.game.popup.description.DescriptionPopupHud;
import com.gmail.yaroslavlancelot.eafall.game.popup.path.PathChoosePopup;

import org.andengine.engine.camera.Camera;
import org.andengine.entity.scene.Scene;
import org.andengine.opengl.texture.TextureManager;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/** init and handel all application popups logic */
public class PopupManager {
    private static PopupManager sPopupManager;
    private Map<String, PopupHud> mPopups = new HashMap<String, PopupHud>(3);


    private PopupManager(String teamName, Scene scene, Camera camera, VertexBufferObjectManager objectManager) {
        //description
        PopupHud popup = new DescriptionPopupHud(scene, objectManager);
        popup.setCamera(camera);
        mPopups.put(DescriptionPopupHud.KEY, popup);
        // buildings
        popup = new BuildingsPopupHud(teamName, scene, objectManager);
        popup.setCamera(camera);
        mPopups.put(BuildingsPopupHud.KEY, popup);
        //path
        popup = new PathChoosePopup(scene, objectManager);
        popup.setCamera(camera);
        mPopups.put(PathChoosePopup.KEY, popup);

        initStateChangingListeners();
    }

    private void initStateChangingListeners() {
        for (String key : mPopups.keySet()) {
            mPopups.get(key).setStateChangingListener(new StateChangingImpl(key));
        }
    }

    public static void loadResource(Context context, TextureManager textureManager) {
        BuildingsPopupHud.loadResource(context, textureManager);
        ConstructionPopupButton.loadResources(context, textureManager);
        DescriptionPopupHud.loadResources(context, textureManager);
        PathChoosePopup.loadResources(context, textureManager);
    }

    /**
     * Create an instance of PopupManager. You can use it as singleton with {@link PopupManager#getInstance()}
     *
     * @param teamName      popup is used by some user. User related to its team. This team name you need to pass.
     * @param scene         in general it's a Hud. Most of popups you will see in the Hud.
     * @param camera        for setting camera to popup
     * @param objectManager VertexBufferObjectManager
     * @return initiated popup with using {@link PopupManager#getInstance()}
     */
    public static PopupManager init(String teamName, Scene scene, Camera camera, VertexBufferObjectManager objectManager) {
        sPopupManager = new PopupManager(teamName, scene, camera, objectManager);
        return getInstance();
    }

    public static PopupManager getInstance() {
        return sPopupManager;
    }

    /** return needed popup instance */
    public static IPopup getPopup(String key) {
        PopupManager popupManager = getInstance();
        return getInstance() == null ? null : popupManager.mPopups.get(key);
    }

    private class StateChangingImpl implements PopupHud.StateChangingListener {
        private String mPopupKey;
        private List<String> mPopupsForShow = new ArrayList<String>(2);

        public StateChangingImpl(String popupKey) {
            mPopupKey = popupKey;
        }

        @Override
        public synchronized void beforeShowing() {
            mPopupsForShow.clear();
            for (String key : mPopups.keySet()) {
                if (key.equals(mPopupKey)) {
                    continue;
                }
                PopupHud popup = mPopups.get(key);
                if (popup.isShowing()) {
                    popup.hidePopup();
                    mPopupsForShow.add(key);
                }
            }
        }

        @Override
        public synchronized void afterHiding() {
            for (String key : mPopupsForShow) {
                mPopups.get(key).showPopup();
            }
            mPopupsForShow.clear();
        }
    }
}
