package com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.touch;

import android.graphics.Rect;
import com.gmail.yaroslavlancelot.spaceinvaders.constants.GameObjectsConstants;
import com.gmail.yaroslavlancelot.spaceinvaders.popups.CreateBuildingPopup;
import com.gmail.yaroslavlancelot.spaceinvaders.teams.ITeam;
import com.gmail.yaroslavlancelot.spaceinvaders.utils.TextureRegionHolderUtils;
import org.andengine.entity.scene.Scene;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import java.util.ArrayList;
import java.util.List;

public class UserPlanetTouchListener implements ISpriteTouchListener {
    private ITeam mUserTeam;
    private VertexBufferObjectManager mVertexBufferObjectManager;
    private TextureRegionHolderUtils mTextureRegionHolderUtils = TextureRegionHolderUtils.getInstance();
    private Scene mParentScene;

    public UserPlanetTouchListener(ITeam userTeam, VertexBufferObjectManager vertexBufferObjectManager, Scene scene) {
        mUserTeam = userTeam;
        mVertexBufferObjectManager = vertexBufferObjectManager;
        mParentScene = scene;
    }

    @Override
    public boolean onTouch(final TouchEvent pSceneTouchEvent, final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
        // init elements
        List<CreateBuildingPopup.PopupItem> items = new ArrayList<CreateBuildingPopup.PopupItem>(2);
        CreateBuildingPopup.PopupItem item;
        item = createPopupItem(0, mTextureRegionHolderUtils.getElement(GameObjectsConstants.KEY_FIRST_BUILDING), "");
        items.add(item);
        item = createPopupItem(1, mTextureRegionHolderUtils.getElement(GameObjectsConstants.KEY_SECOND_BUILDING), "");
        items.add(item);
        // create popup
        Rect rect = new Rect();
        CreateBuildingPopup popup = new CreateBuildingPopup(mVertexBufferObjectManager, mParentScene, rect);
        popup.attachMenuItems(items);
        popup.showPopup();
        return true;
    }

    private CreateBuildingPopup.PopupItem createPopupItem(int id, ITextureRegion textureRegion, String name) {
        return new CreateBuildingPopup.PopupItem(id, textureRegion, name);
    }
}
