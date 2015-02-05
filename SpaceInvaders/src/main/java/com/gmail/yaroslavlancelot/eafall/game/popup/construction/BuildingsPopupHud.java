package com.gmail.yaroslavlancelot.eafall.game.popup.construction;

import android.content.Context;

import com.gmail.yaroslavlancelot.eafall.game.constant.Sizes;
import com.gmail.yaroslavlancelot.eafall.game.constant.StringsAndPath;
import com.gmail.yaroslavlancelot.eafall.game.eventbus.description.BuildingDescriptionShowEvent;
import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.building.BuildingId;
import com.gmail.yaroslavlancelot.eafall.game.popup.PopupHud;
import com.gmail.yaroslavlancelot.eafall.game.popup.construction.item.PopupItemFactory;
import com.gmail.yaroslavlancelot.eafall.game.team.ITeam;
import com.gmail.yaroslavlancelot.eafall.game.team.TeamsHolder;
import com.gmail.yaroslavlancelot.eafall.game.touch.StaticHelper;
import com.gmail.yaroslavlancelot.eafall.android.LoggerHelper;
import com.gmail.yaroslavlancelot.eafall.game.entity.TextureRegionHolder;

import org.andengine.entity.IEntity;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.scene.Scene;
import org.andengine.opengl.texture.TextureManager;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import java.util.HashMap;
import java.util.Map;

import de.greenrobot.event.EventBus;

public class BuildingsPopupHud extends PopupHud {
    /** for logs */
    public static final String TAG = BuildingsPopupHud.class.getCanonicalName();
    /** for popup manager */
    public static final String KEY = TAG;
    /** building of this team popup is showing */
    private final String mTeamName;

    /** The key is the serial number of the building in the list of the buildings */
    private Map<Integer, PopupItemFactory.BuildingPopupItem> mItems;

    public BuildingsPopupHud(String teamName, Scene scene, VertexBufferObjectManager vertexBufferObjectManager) {
        super(scene);
        ITeam team = TeamsHolder.getInstance().getElement(teamName);
        int buildingsAmount = team.getTeamRace().getBuildingsAmount();

        mPopupRectangle = new Rectangle(0, 0,
                Sizes.BUILDING_POPUP_BACKGROUND_ITEM_WIDTH,
                Sizes.BUILDING_POPUP_BACKGROUND_ITEM_HEIGHT * buildingsAmount,
                vertexBufferObjectManager);

        mTeamName = team.getTeamName();
        initBuildingPopupForTeam(mTeamName);
        mPopupRectangle.setX(Sizes.GAME_FIELD_WIDTH / 2 - mPopupRectangle.getWidth() / 2);
        mPopupRectangle.setY(Sizes.GAME_FIELD_HEIGHT - mPopupRectangle.getHeight());
    }

    private void initBuildingPopupForTeam(String teamName) {
        ITeam team = TeamsHolder.getInstance().getElement(teamName);
        mItems = new HashMap<Integer, PopupItemFactory.BuildingPopupItem>(team.getTeamRace().getBuildingsAmount());

        syncBuildingsWithTeam(teamName);
    }

    private void syncBuildingsWithTeam(String teamName) {
        final ITeam team = TeamsHolder.getTeam(teamName);
        BuildingId[] buildings = team.getBuildingsIds();
        String raceName = team.getTeamRace().getAllianceName();
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
                0, serialNumber * Sizes.BUILDING_POPUP_BACKGROUND_ITEM_HEIGHT,
                mPopupRectangle.getVertexBufferObjectManager());
        item.setBuildingId(buildingId, raceName);
        item.setOnClickListener(new StaticHelper.OnClickListener() {
            @Override
            public void onClick() {
                LoggerHelper.printDebugMessage(TAG, "showPopup building description");
                EventBus.getDefault().post(new BuildingDescriptionShowEvent(buildingId, mTeamName));
            }
        });
        mItems.put(serialNumber, item);
        return item.getItemEntity();
    }

    public static void loadResource(Context context, TextureManager textureManager) {
        BitmapTextureAtlas smallObjectTexture = new BitmapTextureAtlas(textureManager, 1200, 100, TextureOptions.BILINEAR);
        TextureRegionHolder.addTiledElementFromAssets(
                StringsAndPath.FILE_POPUP_BACKGROUND_ITEM, smallObjectTexture, context, 0, 0, 2, 1);
        smallObjectTexture.load();
    }

    @Override
    public synchronized void showPopup() {
        syncBuildingsWithTeam(mTeamName);
        super.showPopup();
    }
}
