package com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.touch;

import android.graphics.Rect;
import com.gmail.yaroslavlancelot.spaceinvaders.R;
import com.gmail.yaroslavlancelot.spaceinvaders.constants.GameStringConstants;
import com.gmail.yaroslavlancelot.spaceinvaders.game.interfaces.EntityOperations;
import com.gmail.yaroslavlancelot.spaceinvaders.game.interfaces.Localizable;
import com.gmail.yaroslavlancelot.spaceinvaders.popups.ImageDescriptionPopup;
import com.gmail.yaroslavlancelot.spaceinvaders.teams.ITeam;
import com.gmail.yaroslavlancelot.spaceinvaders.utils.TextureRegionHolderUtils;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.texture.region.ITextureRegion;

import java.util.ArrayList;
import java.util.List;

public class UserPlanetTouchListener implements ISpriteTouchListener {
    private ITeam mUserTeam;
    private TextureRegionHolderUtils mTextureRegionHolderUtils = TextureRegionHolderUtils.getInstance();
    private EntityOperations mEntityOperations;
    private Localizable mLocalizable;
    /** is first touch */
    private boolean mIsFirstTouch = true;
    private ImageDescriptionPopup mPopup;

    public UserPlanetTouchListener(ITeam userTeam, EntityOperations entityOperations, Localizable localizable) {
        mUserTeam = userTeam;
        mEntityOperations = entityOperations;
        mLocalizable = localizable;
    }

    @Override
    public boolean onTouch(final TouchEvent pSceneTouchEvent, final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
        if (mIsFirstTouch) {
            mIsFirstTouch = false;
            initBuildingPopupForTeam(mUserTeam);
        }

        if (pSceneTouchEvent.getAction() == TouchEvent.ACTION_UP) {
            if (mPopup.isShowing())
                mPopup.hidePopup();
            else
                mPopup.showPopup();
        }
        return true;
    }

    private void initBuildingPopupForTeam(ITeam team) {
        Rect buildingPopupRect = getBuildingPopupRectForTeam(team);
        // init elements
        List<ImageDescriptionPopup.PopupItem> items = new ArrayList<ImageDescriptionPopup.PopupItem>(2);
        ImageDescriptionPopup.PopupItem item;
        item = createPopupItem(0, mTextureRegionHolderUtils.getElement(GameStringConstants.KEY_FIRST_BUILDING),
                mLocalizable.getStringById(R.string.first_building));
        items.add(item);
        item = createPopupItem(1, mTextureRegionHolderUtils.getElement(GameStringConstants.KEY_SECOND_BUILDING),
                mLocalizable.getStringById(R.string.second_building));
        items.add(item);
        // create popup
        mPopup = new ImageDescriptionPopup(mEntityOperations, buildingPopupRect);
        mPopup.attachMenuItems(items);
    }

    private ImageDescriptionPopup.PopupItem createPopupItem(int id, ITextureRegion textureRegion, String name) {
        IItemPickListener spriteTouchListener = new IItemPickListener() {
            @Override
            public void itemPicked(final int buildingId) {
                switch (buildingId) {
                    case 0:
                        mUserTeam.getTeamPlanet().buildFirstBuilding();
                        break;
                    case 1:
                        mUserTeam.getTeamPlanet().buildSecondBuilding();
                        break;
                }
            }
        };
        return new ImageDescriptionPopup.PopupItem(id, textureRegion, name, spriteTouchListener);
    }

    private Rect getBuildingPopupRectForTeam(ITeam team) {
        int teamPlanetX = (int) team.getTeamPlanet().getX(),
                teamPlanetY = (int) team.getTeamPlanet().getY();
        int buildingPopupHeight = 50, buildingPopupWidth = 60;
        return new Rect(teamPlanetX, teamPlanetY - buildingPopupHeight, teamPlanetX + buildingPopupWidth, teamPlanetY);
    }
}
