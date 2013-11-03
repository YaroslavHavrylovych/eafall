package com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.touch;

import android.view.MotionEvent;
import com.gmail.yaroslavlancelot.spaceinvaders.game.interfaces.EntityOperations;
import com.gmail.yaroslavlancelot.spaceinvaders.game.interfaces.Localizable;
import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.objects.staticobjects.PlanetStaticObject;
import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.objects.staticobjects.StaticObject;
import com.gmail.yaroslavlancelot.spaceinvaders.popups.ImageDescriptionPopup;
import com.gmail.yaroslavlancelot.spaceinvaders.races.IRace;
import com.gmail.yaroslavlancelot.spaceinvaders.teams.ITeam;
import com.gmail.yaroslavlancelot.spaceinvaders.utils.Area;
import org.andengine.input.touch.TouchEvent;

import java.util.ArrayList;
import java.util.List;

public class BuildingsPopupShowListener implements ITouchListener {
    private ITeam mUserTeam;
    private EntityOperations mEntityOperations;
    private Localizable mLocalizable;
    /** is first touch */
    private boolean mIsFirstTouch = true;
    private ImageDescriptionPopup mPopup;
    private float mActionDownX, mActionDownY;

    public BuildingsPopupShowListener(ITeam userTeam, EntityOperations entityOperations, Localizable localizable) {
        mUserTeam = userTeam;
        mEntityOperations = entityOperations;
        mLocalizable = localizable;
    }

    @Override
    public boolean onTouch(final TouchEvent pSceneTouchEvent) {
        float motionEventX = pSceneTouchEvent.getMotionEvent().getX();
        float motionEventY = pSceneTouchEvent.getMotionEvent().getY();
        switch (pSceneTouchEvent.getMotionEvent().getAction()) {
            case MotionEvent.ACTION_UP:
                if (mActionDownX == motionEventX && mActionDownY == motionEventY)
                    break;
                return false;
            case MotionEvent.ACTION_DOWN:
                mActionDownX = motionEventX;
                mActionDownY = motionEventY;
            default:
                return false;
        }

        if (mIsFirstTouch) {
            mIsFirstTouch = false;
            initBuildingPopupForTeam(mUserTeam);
        }

        if (mUserTeam.getTeamPlanet() == null || mPopup.isShowing())
            mPopup.hidePopup();
        else
            mPopup.showPopup();

        return true;
    }

    private void initBuildingPopupForTeam(ITeam team) {
        Area buildingPopupRect = getBuildingPopupRectForTeam(team);
        IRace race = team.getTeamRace();
        // init elements
        List<ImageDescriptionPopup.PopupItem> items =
                new ArrayList<ImageDescriptionPopup.PopupItem>(race.getBuildingsAmount());
        ImageDescriptionPopup.PopupItem item;
        for (int buildingId = 0; buildingId < race.getBuildingsAmount(); buildingId++) {
            item = createPopupItem(buildingId, race.getBuildingById(buildingId

            ), buildingId + "building");
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
                PlanetStaticObject planetStaticObject = mUserTeam.getTeamPlanet();
                if (planetStaticObject != null)
                    mUserTeam.getTeamPlanet().createBuildingById(buildingId);
            }
        };
        return new ImageDescriptionPopup.PopupItem(id, staticObject, name, spriteTouchListener);
    }
}
