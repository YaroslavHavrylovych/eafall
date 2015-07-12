package com.gmail.yaroslavlancelot.eafall.game.popup.construction;

import android.content.Context;
import android.util.SparseArray;

import com.gmail.yaroslavlancelot.eafall.android.LoggerHelper;
import com.gmail.yaroslavlancelot.eafall.game.constant.SizeConstants;
import com.gmail.yaroslavlancelot.eafall.game.constant.StringConstants;
import com.gmail.yaroslavlancelot.eafall.game.entity.TextureRegionHolder;
import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.building.BuildingId;
import com.gmail.yaroslavlancelot.eafall.game.eventbus.description.BuildingDescriptionShowEvent;
import com.gmail.yaroslavlancelot.eafall.game.player.IPlayer;
import com.gmail.yaroslavlancelot.eafall.game.player.PlayersHolder;
import com.gmail.yaroslavlancelot.eafall.game.popup.PopupHud;
import com.gmail.yaroslavlancelot.eafall.game.popup.construction.item.ConstructionsPopupItemFactory;
import com.gmail.yaroslavlancelot.eafall.game.touch.StaticHelper;

import org.andengine.entity.IEntity;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.texture.TextureManager;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.adt.color.Color;

import de.greenrobot.event.EventBus;

public class ConstructionsPopupHud extends PopupHud {
    /** for logs */
    public static final String TAG = ConstructionsPopupHud.class.getCanonicalName();
    /** for popup manager */
    public static final String KEY = TAG;
    /** building of this player popup is showing */
    private final String mPlayerName;

    /** The key is the serial number of the building in the list of the buildings */
    private SparseArray<ConstructionsPopupItemFactory.BuildingPopupItem> mItems;

    public ConstructionsPopupHud(String playerName, Scene scene, VertexBufferObjectManager vertexBufferObjectManager) {
        super(scene);
        IPlayer player = PlayersHolder.getInstance().getElement(playerName);

        mPopupRectangle = new Rectangle(0, 0,
                SizeConstants.CONSTRUCTIONS_POPUP_WIDTH,
                SizeConstants.CONSTRUCTIONS_POPUP_HEIGHT,
                vertexBufferObjectManager);
        mPopupRectangle.setColor(Color.TRANSPARENT);
        mPopupRectangle.attachChild(
                new Sprite(mPopupRectangle.getWidth() / 2, mPopupRectangle.getHeight() / 2,
                        TextureRegionHolder.getRegion(StringConstants.FILE_CONSTRUCTIONS_POPUP),
                        vertexBufferObjectManager));

        mPlayerName = player.getName();
        initBuildingPopupForPlayer(mPlayerName);
        mPopupRectangle.setX(SizeConstants.HALF_FIELD_WIDTH);
        mPopupRectangle.setY(mPopupRectangle.getHeight() / 2);
    }

    @Override
    public synchronized void showPopup() {
        syncBuildingsWithPlayer(mPlayerName);
        super.showPopup();
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

    private void initBuildingPopupForPlayer(String playerName) {
        IPlayer player = PlayersHolder.getInstance().getElement(playerName);
        mItems = new SparseArray<ConstructionsPopupItemFactory.BuildingPopupItem>(player.getAlliance().getBuildingsAmount());

        syncBuildingsWithPlayer(playerName);
    }

    private void syncBuildingsWithPlayer(String playerName) {
        final IPlayer player = PlayersHolder.getPlayer(playerName);
        BuildingId[] buildings = player.getBuildingsIds();
        String allianceName = player.getAlliance().getAllianceName();
        int buildingsCount = buildings.length;
        for (int id = 0; id < buildingsCount; id++) {
            // if building which should be on this position is not created at all
            if (mItems.indexOfKey(id) < 0) {
                IEntity item = constructPopupItem(id, buildings[id], allianceName);
                mPopupRectangle.attachChild(item);
                continue;
            }

            // if building on position exist, we check it's upgrade to be sure that it's needed building
            if (mItems.get(id).getBuildingId().getUpgrade() != buildings[id].getUpgrade()) {
                mPopupRectangle.detachChild(mItems.get(id).getItemEntity());
                IEntity item = constructPopupItem(id, buildings[id], allianceName);
                mPopupRectangle.attachChild(item);
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
                        x, y, mPopupRectangle.getVertexBufferObjectManager());
        item.setBuildingId(buildingId, allianceName);
        item.setOnClickListener(new StaticHelper.OnClickListener() {
            @Override
            public void onClick() {
                LoggerHelper.printDebugMessage(TAG, "showPopup building description");
                EventBus.getDefault().post(new BuildingDescriptionShowEvent(buildingId, mPlayerName));
            }
        });
        mItems.put(id, item);
        return item.getItemEntity();
    }
}
