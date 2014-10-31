package com.gmail.yaroslavlancelot.spaceinvaders.popups.buildings;

import android.content.Context;

import com.gmail.yaroslavlancelot.spaceinvaders.constants.GameStringsConstantsAndUtils;
import com.gmail.yaroslavlancelot.spaceinvaders.constants.SizeConstants;
import com.gmail.yaroslavlancelot.spaceinvaders.eventbus.description.BuildingDescriptionShowEvent;
import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.objects.staticobjects.BuildingId;
import com.gmail.yaroslavlancelot.spaceinvaders.popups.PopupHud;
import com.gmail.yaroslavlancelot.spaceinvaders.popups.buildings.item.PopupItemFactory;
import com.gmail.yaroslavlancelot.spaceinvaders.teams.ITeam;
import com.gmail.yaroslavlancelot.spaceinvaders.teams.TeamsHolder;
import com.gmail.yaroslavlancelot.spaceinvaders.utils.LoggerHelper;
import com.gmail.yaroslavlancelot.spaceinvaders.utils.TextureRegionHolderUtils;
import com.gmail.yaroslavlancelot.spaceinvaders.utils.TouchUtils;

import org.andengine.entity.IEntity;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.opengl.texture.TextureManager;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import java.util.HashMap;
import java.util.Map;

import de.greenrobot.event.EventBus;

public abstract class BuildingsPopupHud extends PopupHud {
    public static final String TAG = BuildingsPopupHud.class.getCanonicalName();
    /** building of this team popup is showing */
    private final String mTeamName;

    /** The key is the serial number of the building in the list of the buildings */
    private Map<Integer, PopupItemFactory.BuildingPopupItem> mItems;

    public BuildingsPopupHud(String teamName, VertexBufferObjectManager vertexBufferObjectManager) {
        ITeam team = TeamsHolder.getInstance().getElement(teamName);
        int buildingsAmount = team.getTeamRace().getBuildingsAmount();

        mPopupRectangle = new Rectangle(0, 0,
                SizeConstants.BUILDING_POPUP_BACKGROUND_ITEM_WIDTH,
                SizeConstants.BUILDING_POPUP_BACKGROUND_ITEM_HEIGHT * buildingsAmount,
                vertexBufferObjectManager);

        mTeamName = team.getTeamName();
        initBuildingPopupForTeam(mTeamName);
        mPopupRectangle.setX(SizeConstants.GAME_FIELD_WIDTH / 2 - mPopupRectangle.getWidth() / 2);
        mPopupRectangle.setY(SizeConstants.GAME_FIELD_HEIGHT - mPopupRectangle.getHeight());
    }

    private void initBuildingPopupForTeam(String teamName) {
        ITeam team = TeamsHolder.getInstance().getElement(teamName);
        mItems = new HashMap<Integer, PopupItemFactory.BuildingPopupItem>(team.getTeamRace().getBuildingsAmount());

        syncBuildingsWithTeam(teamName);
    }

    private void syncBuildingsWithTeam(String teamName) {
        final ITeam team = TeamsHolder.getTeam(teamName);
        BuildingId[] buildings = team.getBuildingsIds();
        String raceName = team.getTeamRace().getRaceName();
        for (int i = 0; i < buildings.length; i++) {
            // if building which should be on this position is not created at all
            if (!mItems.containsKey(i)) {
                IEntity item = constructPopupItem(i, buildings[i], raceName);
                mPopupRectangle.attachChild(item);
                continue;
            }

            // if building on position exist, we check it's upgrade to be sure that it's needed building
            if (mItems.get(i).getBuildingId().getUpgrade() != buildings[i].getUpgrade()) {
                mPopupRectangle.detachChild(mItems.get(i).getItemEntity());
                IEntity item = constructPopupItem(i, buildings[i], raceName);
                mPopupRectangle.attachChild(item);
            }
        }
    }

    private IEntity constructPopupItem(final int serialNumber, final BuildingId buildingId, String raceName) {
        PopupItemFactory.BuildingPopupItem item = PopupItemFactory.createBuildingPopupItem(
                0, serialNumber * SizeConstants.BUILDING_POPUP_BACKGROUND_ITEM_HEIGHT,
                mPopupRectangle.getVertexBufferObjectManager());
        item.setBuildingId(buildingId, raceName);
        item.setOnClickListener(new TouchUtils.OnClickListener() {
            @Override
            public void onClick() {
                LoggerHelper.printDebugMessage(TAG, "showPopup building description");
                if (mIsPopupShowing) {
                    hidePopup();
                }
                EventBus.getDefault().post(new BuildingDescriptionShowEvent(buildingId, mTeamName));
            }
        });
        mItems.put(serialNumber, item);
        return item.getItemEntity();
    }

    public static void loadResource(Context context, TextureManager textureManager) {
        BitmapTextureAtlas smallObjectTexture = new BitmapTextureAtlas(textureManager, 1200, 100, TextureOptions.BILINEAR);
        TextureRegionHolderUtils.addTiledElementFromAssets(GameStringsConstantsAndUtils.FILE_POPUP_BACKGROUND_ITEM,
                TextureRegionHolderUtils.getInstance(), smallObjectTexture, context, 0, 0, 2, 1);
        smallObjectTexture.load();
    }

    /** will showPopup or hidePopup popup depending on current state */
    public synchronized void triggerPopup() {
        LoggerHelper.printDebugMessage(TAG, "showPopup popup = " + !mIsPopupShowing);
        if (mIsPopupShowing) {
            hidePopup();
        } else {
            showPopup();
        }
    }

    @Override
    protected synchronized void showPopup() {
        syncBuildingsWithTeam(mTeamName);
        super.showPopup();
    }
}
