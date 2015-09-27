package com.gmail.yaroslavlancelot.eafall.game.popup.rolling.construction;

import android.content.Context;
import android.util.SparseArray;

import com.gmail.yaroslavlancelot.eafall.android.LoggerHelper;
import com.gmail.yaroslavlancelot.eafall.game.constant.SizeConstants;
import com.gmail.yaroslavlancelot.eafall.game.constant.StringConstants;
import com.gmail.yaroslavlancelot.eafall.game.entity.TextureRegionHolder;
import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.building.BuildingId;
import com.gmail.yaroslavlancelot.eafall.game.events.aperiodic.ingame.description.BuildingDescriptionShowEvent;
import com.gmail.yaroslavlancelot.eafall.game.player.IPlayer;
import com.gmail.yaroslavlancelot.eafall.game.player.PlayersHolder;
import com.gmail.yaroslavlancelot.eafall.game.popup.rolling.RollingPopup;
import com.gmail.yaroslavlancelot.eafall.game.popup.rolling.construction.item.ConstructionsPopupItemFactory;
import com.gmail.yaroslavlancelot.eafall.game.touch.TouchHelper;

import org.andengine.engine.camera.Camera;
import org.andengine.entity.IEntity;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.texture.TextureManager;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import de.greenrobot.event.EventBus;

/** Display list of available player buildings */
public class ConstructionsPopup extends RollingPopup {
    /** for logs */
    public static final String TAG = ConstructionsPopup.class.getCanonicalName();
    /** for popup manager */
    public static final String KEY = TAG;
    /** building of this player popup is showing */
    private final String mPlayerName;
    /** used to invoke building description after constructions popup was hided */
    private final CustomStateListener mStateChangingListener = new CustomStateListener();
    /** The key is the serial number of the building in the list of the buildings */
    private SparseArray<ConstructionsPopupItemFactory.BuildingPopupItem> mItems;

    public ConstructionsPopup(String playerName, Scene scene, Camera camera,
                              VertexBufferObjectManager vertexBufferObjectManager) {
        super(scene, camera);
        IPlayer player = PlayersHolder.getInstance().getElement(playerName);

        int width = SizeConstants.CONSTRUCTIONS_POPUP_WIDTH;
        int height = SizeConstants.CONSTRUCTIONS_POPUP_HEIGHT;
        mBackgroundSprite = new Sprite(SizeConstants.HALF_FIELD_WIDTH, -height / 2,
                width, height,
                TextureRegionHolder.getRegion(StringConstants.FILE_CONSTRUCTIONS_POPUP),
                vertexBufferObjectManager);
        mBackgroundSprite.setTouchCallback(new TouchHelper.EntityTouchToChildren(mBackgroundSprite));
        attachChild(mBackgroundSprite);
        mPlayerName = player.getName();
        initBuildingPopupForPlayer(mPlayerName);
        init();
    }

    @Override
    public void showPopup() {
        syncBuildingsWithPlayer(mPlayerName);
        super.showPopup();
    }

    private void initBuildingPopupForPlayer(String playerName) {
        IPlayer player = PlayersHolder.getInstance().getElement(playerName);
        mItems = new SparseArray<>(player.getAlliance().getBuildingsAmount());

        syncBuildingsWithPlayer(playerName);
    }

    private void syncBuildingsWithPlayer(String playerName) {
        final IPlayer player = PlayersHolder.getPlayer(playerName);
        BuildingId[] buildings = player.getBuildingsIds();
        String allianceName = player.getAlliance().getAllianceName();
        int buildingsCount = buildings.length;
        for (int id = 0; id < buildingsCount; id++) {
            //construction on popup initialization
            if (mItems.indexOfKey(id) < 0) {
                IEntity item = constructPopupItem(id, buildings[id], allianceName);
                mBackgroundSprite.attachChild(item);
                continue;
            }
            //the construction was upgraded and we have to replace an item
            if (mItems.get(id).getBuildingId().getUpgrade() != buildings[id].getUpgrade()) {
                IEntity item = mItems.get(id).getItemEntity();
                mBackgroundSprite.detachChild(item);
                item = constructPopupItem(id, buildings[id], allianceName);
                mBackgroundSprite.attachChild(item);
            }
        }
    }

    private IEntity constructPopupItem(int id, final BuildingId buildingId,
                                       String allianceName) {
        int x = SizeConstants.CONSTRUCTIONS_POPUP_ELEMENT_WIDTH / 2 +
                (0 == id / SizeConstants.CONSTRUCTIONS_POPUP_ROWS
                        ? SizeConstants.CONSTRUCTIONS_POPUP_FIRST_COLUMN_X
                        : SizeConstants.CONSTRUCTIONS_POPUP_SECOND_COLUMN_X);
        int y = SizeConstants.CONSTRUCTIONS_POPUP_HEIGHT -
                SizeConstants.CONSTRUCTIONS_POPUP_FIRST_ROW_Y -
                SizeConstants.CONSTRUCTIONS_POPUP_ELEMENT_HEIGHT
                        * (id % SizeConstants.CONSTRUCTIONS_POPUP_ROWS);
        ConstructionsPopupItemFactory.BuildingPopupItem item =
                ConstructionsPopupItemFactory.createConstructionPopupItem(
                        x, y, mBackgroundSprite.getVertexBufferObjectManager());
        item.setBuildingId(buildingId, allianceName);
        item.setOnClickListener(new TouchHelper.OnClickListener() {
            @Override
            public void onClick() {
                LoggerHelper.printDebugMessage(TAG, "showPopup building description");
                mStateChangingListener.setBuildingId(buildingId);
                setStateChangeListener(mStateChangingListener);
                hidePopup();
            }
        });
        mItems.put(id, item);
        return item.getItemEntity();
    }

    public static void loadResource(Context context, TextureManager textureManager) {
        BitmapTextureAtlas textureAtlas = new BitmapTextureAtlas(textureManager,
                SizeConstants.CONSTRUCTIONS_POPUP_WIDTH,
                SizeConstants.CONSTRUCTIONS_POPUP_HEIGHT
                        + SizeConstants.BETWEEN_TEXTURES_PADDING
                        + 2 * SizeConstants.CONSTRUCTIONS_POPUP_ELEMENT_HEIGHT,
                TextureOptions.DEFAULT);
        TextureRegionHolder.addElementFromAssets(StringConstants.FILE_CONSTRUCTIONS_POPUP,
                textureAtlas, context, 0, 0);
        TextureRegionHolder.addTiledElementFromAssets(StringConstants.FILE_CONSTRUCTIONS_POPUP_ITEM,
                textureAtlas, context,
                0, SizeConstants.CONSTRUCTIONS_POPUP_HEIGHT + SizeConstants.BETWEEN_TEXTURES_PADDING,
                1, 2);
        textureAtlas.load();
    }

    /**
     * This custom state change listener invokes building description popup after tracked popup
     * was hidden.
     * <br/>
     * Before using you have to set building it manually invoking
     * {@link ConstructionsPopup.CustomStateListener#setBuildingId(BuildingId)}
     */
    private class CustomStateListener implements StateChangingListener {
        private BuildingId mBuildingId;

        private void setBuildingId(BuildingId buildingId) {
            mBuildingId = buildingId;
        }

        @Override
        public void onShowed() {
        }

        @Override
        public void onHided() {
            removeStateChangeListener();
            EventBus.getDefault().post(new BuildingDescriptionShowEvent(mBuildingId, mPlayerName));
        }
    }
}
