package com.gmail.yaroslavlancelot.eafall.game.popup.construction;

import android.content.Context;
import android.util.SparseArray;

import com.gmail.yaroslavlancelot.eafall.android.LoggerHelper;
import com.gmail.yaroslavlancelot.eafall.game.constant.SizeConstants;
import com.gmail.yaroslavlancelot.eafall.game.constant.StringConstants;
import com.gmail.yaroslavlancelot.eafall.game.entity.TextureRegionHolder;
import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.building.BuildingId;
import com.gmail.yaroslavlancelot.eafall.game.eventbus.description.BuildingDescriptionShowEvent;
import com.gmail.yaroslavlancelot.eafall.game.popup.PopupHud;
import com.gmail.yaroslavlancelot.eafall.game.popup.construction.item.PopupItemFactory;
import com.gmail.yaroslavlancelot.eafall.game.team.ITeam;
import com.gmail.yaroslavlancelot.eafall.game.team.TeamsHolder;
import com.gmail.yaroslavlancelot.eafall.game.touch.StaticHelper;

import org.andengine.entity.IEntity;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.scene.Scene;
import org.andengine.opengl.texture.TextureManager;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import de.greenrobot.event.EventBus;

public class BuildingsPopupHud extends PopupHud {
    /** for logs */
    public static final String TAG = BuildingsPopupHud.class.getCanonicalName();
    /** for popup manager */
    public static final String KEY = TAG;
    /** building of this team popup is showing */
    private final String mTeamName;

    /** The key is the serial number of the building in the list of the buildings */
    private SparseArray<PopupItemFactory.BuildingPopupItem> mItems;

    public BuildingsPopupHud(String teamName, Scene scene, VertexBufferObjectManager vertexBufferObjectManager) {
        super(scene);
        ITeam team = TeamsHolder.getInstance().getElement(teamName);
        int buildingsAmount = team.getTeamRace().getBuildingsAmount();

        mPopupRectangle = new Rectangle(0, 0,
                SizeConstants.BUILDING_POPUP_BACKGROUND_ITEM_WIDTH,
                SizeConstants.BUILDING_POPUP_BACKGROUND_ITEM_HEIGHT * buildingsAmount,
                vertexBufferObjectManager);

        mTeamName = team.getTeamName();
        initBuildingPopupForTeam(mTeamName);
        mPopupRectangle.setX(SizeConstants.HALF_FIELD_WIDTH);
        mPopupRectangle.setY(mPopupRectangle.getHeight() / 2);
    }

    private void initBuildingPopupForTeam(String teamName) {
        ITeam team = TeamsHolder.getInstance().getElement(teamName);
        mItems = new SparseArray<PopupItemFactory.BuildingPopupItem>(team.getTeamRace().getBuildingsAmount());

        syncBuildingsWithTeam(teamName);
    }

    private void syncBuildingsWithTeam(String teamName) {
        final ITeam team = TeamsHolder.getTeam(teamName);
        BuildingId[] buildings = team.getBuildingsIds();
        String raceName = team.getTeamRace().getAllianceName();
        int buildingsCount = buildings.length;
        int position;
        for (int id = 0; id < buildingsCount; id++) {
            position = buildingsCount - id - 1;
            // if building which should be on this position is not created at all
            if (mItems.indexOfKey(id) < 0) {
                IEntity item = constructPopupItem(id, position, buildings[id], raceName);
                mPopupRectangle.attachChild(item);
                continue;
            }

            // if building on position exist, we check it's upgrade to be sure that it's needed building
            if (mItems.get(id).getBuildingId().getUpgrade() != buildings[id].getUpgrade()) {
                mPopupRectangle.detachChild(mItems.get(id).getItemEntity());
                IEntity item = constructPopupItem(id, position, buildings[id], raceName);
                mPopupRectangle.attachChild(item);
            }
        }
    }

    private IEntity constructPopupItem(int id, int position, final BuildingId buildingId, String raceName) {
        PopupItemFactory.BuildingPopupItem item = PopupItemFactory.createBuildingPopupItem(
                mPopupRectangle.getWidth() / 2,
                position * SizeConstants.BUILDING_POPUP_BACKGROUND_ITEM_HEIGHT
                        + SizeConstants.BUILDING_POPUP_BACKGROUND_ITEM_HEIGHT / 2,
                mPopupRectangle.getVertexBufferObjectManager());
        item.setBuildingId(buildingId, raceName);
        item.setOnClickListener(new StaticHelper.OnClickListener() {
            @Override
            public void onClick() {
                LoggerHelper.printDebugMessage(TAG, "showPopup building description");
                EventBus.getDefault().post(new BuildingDescriptionShowEvent(buildingId, mTeamName));
            }
        });
        mItems.put(id, item);
        return item.getItemEntity();
    }

    public static void loadResource(Context context, TextureManager textureManager) {
        BitmapTextureAtlas smallObjectTexture = new BitmapTextureAtlas(textureManager, 1200, 100, TextureOptions.BILINEAR);
        TextureRegionHolder.addTiledElementFromAssets(
                StringConstants.FILE_POPUP_BACKGROUND_ITEM, smallObjectTexture, context, 0, 0, 2, 1);
        smallObjectTexture.load();
    }

    @Override
    public synchronized void showPopup() {
        syncBuildingsWithTeam(mTeamName);
        super.showPopup();
    }
}
