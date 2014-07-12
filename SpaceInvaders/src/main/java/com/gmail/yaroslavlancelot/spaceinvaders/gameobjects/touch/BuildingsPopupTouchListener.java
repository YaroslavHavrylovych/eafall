package com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.touch;

import android.view.MotionEvent;

import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.objects.GameObject;
import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.objects.staticobjects.StaticObject;
import com.gmail.yaroslavlancelot.spaceinvaders.popups.buildings.BuildingsListItemBackgroundSprite;
import com.gmail.yaroslavlancelot.spaceinvaders.popups.buildings.BuildingsListPopup;
import com.gmail.yaroslavlancelot.spaceinvaders.races.IRace;
import com.gmail.yaroslavlancelot.spaceinvaders.teams.ITeam;
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
    private BuildingsListPopup mPopup;
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

    public void hide() {
        if (mUserTeam.getTeamPlanet() == null || mPopup.isShowing())
            mPopup.hidePopup();
    }

    private void initBuildingPopupForTeam(ITeam team) {
        IRace race = team.getTeamRace();
        // init elements
        List<BuildingsListPopup.PopupItem> items =
                new ArrayList<BuildingsListPopup.PopupItem>(race.getBuildingsAmount());
        BuildingsListPopup.PopupItem item;
        for (int buildingId = 0; buildingId < race.getBuildingsAmount(); buildingId++) {
            item = createPopupItem(buildingId, race.getBuildingById(buildingId));
            items.add(item);
        }
        mPopup = new BuildingsListPopup(mEntityOperations);
        mPopup.attachMenuItems(items);
        mPopup.recalculatePopupBoundaries();
    }

    private BuildingsListPopup.PopupItem createPopupItem(int id, StaticObject staticObject) {
        String prefix = Integer.toString(staticObject.getObjectCost()) + " : ";
        return createPopupItem(id, staticObject, prefix +
                mLocalizable.getStringById(staticObject.getObjectStringId()));
    }

    private BuildingsListPopup.PopupItem createPopupItem(int id, GameObject gameObject, String name) {
        IItemPickListener spriteTouchListener = new IItemPickListener() {
            @Override
            public void itemPicked(final int buildingId) {
                mItemPickListener.itemPicked(buildingId);
            }
        };
        return new BuildingsListPopup.PopupItem(id, gameObject, name, spriteTouchListener,
                new BuildingsListItemBackgroundSprite(mEntityOperations.getObjectManager()));
    }
}
