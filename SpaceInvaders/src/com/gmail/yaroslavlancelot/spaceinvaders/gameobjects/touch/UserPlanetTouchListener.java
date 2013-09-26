package com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.touch;

import com.gmail.yaroslavlancelot.spaceinvaders.constants.GameObjectsConstants;
import com.gmail.yaroslavlancelot.spaceinvaders.popups.CreateBuildingPopup;
import com.gmail.yaroslavlancelot.spaceinvaders.teams.ITeam;
import com.gmail.yaroslavlancelot.spaceinvaders.utils.TextureRegionHolderUtils;
import org.andengine.entity.scene.Scene;
import org.andengine.input.touch.TouchEvent;
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
        List<CreateBuildingPopup.PopupItem> items = new ArrayList<CreateBuildingPopup.PopupItem>(2);
        items.add(new CreateBuildingPopup.PopupItem(mTextureRegionHolderUtils.getRegion(GameObjectsConstants.KEY_FIRST_BUILDING), ""));
        items.add(new CreateBuildingPopup.PopupItem(mTextureRegionHolderUtils.getRegion(GameObjectsConstants.KEY_SECOND_BUILDING), ""));
        return true;
    }
}
