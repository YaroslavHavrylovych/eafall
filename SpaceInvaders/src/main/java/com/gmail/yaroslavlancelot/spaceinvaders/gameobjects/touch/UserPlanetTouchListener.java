package com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.touch;

import com.gmail.yaroslavlancelot.spaceinvaders.game.interfaces.EntityOperations;
import com.gmail.yaroslavlancelot.spaceinvaders.game.interfaces.Localizable;
import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.objects.staticobjects.StaticObject;
import com.gmail.yaroslavlancelot.spaceinvaders.popups.ImageDescriptionPopup;
import com.gmail.yaroslavlancelot.spaceinvaders.races.IRace;
import com.gmail.yaroslavlancelot.spaceinvaders.teams.ITeam;
import com.gmail.yaroslavlancelot.spaceinvaders.utils.Area;
import org.andengine.input.touch.TouchEvent;

import java.util.ArrayList;
import java.util.List;

public class UserPlanetTouchListener implements ISpriteTouchListener {
    private ITeam mUserTeam;
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
        Area buildingPopupRect = getBuildingPopupRectForTeam(team);
        IRace race = team.getTeamRace();
        // init elements
        List<ImageDescriptionPopup.PopupItem> items =
                new ArrayList<ImageDescriptionPopup.PopupItem>(race.getBuildingsAmount());
        ImageDescriptionPopup.PopupItem item;
        for (int i = 0; i < race.getBuildingsAmount(); i++) {
            item = createPopupItem(i, race.getBuildingById(i, mEntityOperations.getObjectManager(), team.getTeamColor()), i + "building");
            items.add(item);
        }
        mPopup = new ImageDescriptionPopup(mEntityOperations, buildingPopupRect);
        mPopup.attachMenuItems(items);
    }

    private Area getBuildingPopupRectForTeam(ITeam team) {
        int teamPlanetX = 0, teamPlanetY = 20;
        int buildingPopupHeight = 50, buildingPopupWidth = 60;
        return new Area(teamPlanetX, teamPlanetY - buildingPopupHeight, teamPlanetX + buildingPopupWidth, teamPlanetY);
    }

    private ImageDescriptionPopup.PopupItem createPopupItem(int id, StaticObject staticObject, String name) {
        IItemPickListener spriteTouchListener = new IItemPickListener() {
            @Override
            public void itemPicked(final int buildingId) {
                mUserTeam.getTeamPlanet().createBuildingById(buildingId);
            }
        };
        return new ImageDescriptionPopup.PopupItem(id, staticObject, name, spriteTouchListener);
    }
}
