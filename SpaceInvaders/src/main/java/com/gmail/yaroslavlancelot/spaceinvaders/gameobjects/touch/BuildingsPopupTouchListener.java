package com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.touch;

import android.view.MotionEvent;

import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.objects.GameObject;
import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.objects.staticobjects.StaticObject;
import com.gmail.yaroslavlancelot.spaceinvaders.popups.ImageDescriptionPopup;
import com.gmail.yaroslavlancelot.spaceinvaders.popups.PopupItemBackgroundSprite;
import com.gmail.yaroslavlancelot.spaceinvaders.races.IRace;
import com.gmail.yaroslavlancelot.spaceinvaders.teams.ITeam;
import com.gmail.yaroslavlancelot.spaceinvaders.utils.Area;
import com.gmail.yaroslavlancelot.spaceinvaders.utils.interfaces.EntityOperations;
import com.gmail.yaroslavlancelot.spaceinvaders.utils.interfaces.Localizable;

import org.andengine.input.touch.TouchEvent;

import java.util.ArrayList;
import java.util.List;

/** screen was touched (not move or scroll) and popup should appear */
public class BuildingsPopupTouchListener implements ITouchListener {
    private ITeam mUserTeam;
    private EntityOperations mEntityOperations;
    private Localizable mLocalizable;
    /** is first touch */
    private boolean mIsFirstTouch = true;
    private ImageDescriptionPopup mPopup;
    private float mActionDownX, mActionDownY;
    private int mBuildingCostStringMaxCharacters = 5;
    private IItemPickListener mItemPickListener;

    public BuildingsPopupTouchListener(ITeam userTeam, EntityOperations entityOperations, Localizable localizable, IItemPickListener itemPickListener) {
        mUserTeam = userTeam;
        mEntityOperations = entityOperations;
        mLocalizable = localizable;
        mItemPickListener = itemPickListener;
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
            item = createPopupItem(buildingId, race.getBuildingById(buildingId));
            items.add(item);
        }
        mPopup = new ImageDescriptionPopup(mEntityOperations, buildingPopupRect);
        mPopup.attachMenuItems(items);
    }

    private ImageDescriptionPopup.PopupItem createPopupItem(int id, StaticObject staticObject) {
        String prefix = Integer.toString(staticObject.getObjectCost()) + " : ";
        return createPopupItem(id, staticObject, prefix +
                mLocalizable.getStringById(staticObject.getObjectStringId()));
    }

    private ImageDescriptionPopup.PopupItem createPopupItem(int id, GameObject gameObject, String name) {
        IItemPickListener spriteTouchListener = new IItemPickListener() {
            @Override
            public void itemPicked(final int buildingId) {
                mItemPickListener.itemPicked(buildingId);
            }
        };
        return new ImageDescriptionPopup.PopupItem(id, gameObject, name, spriteTouchListener,
                new PopupItemBackgroundSprite(mEntityOperations.getObjectManager()));
    }

    private Area getBuildingPopupRectForTeam(ITeam team) {
        return new Area(0, 0, 0, 0);
    }
}
