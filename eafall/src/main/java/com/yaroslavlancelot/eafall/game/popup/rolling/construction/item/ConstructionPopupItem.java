package com.yaroslavlancelot.eafall.game.popup.rolling.construction.item;

import com.yaroslavlancelot.eafall.game.alliance.AllianceHolder;
import com.yaroslavlancelot.eafall.game.constant.SizeConstants;
import com.yaroslavlancelot.eafall.game.constant.StringConstants;
import com.yaroslavlancelot.eafall.game.entity.TextureRegionHolder;
import com.yaroslavlancelot.eafall.game.entity.gameobject.building.BuildingId;
import com.yaroslavlancelot.eafall.game.entity.gameobject.building.dummy.BuildingDummy;
import com.yaroslavlancelot.eafall.game.touch.TouchHelper;
import com.yaroslavlancelot.eafall.game.visual.font.FontHolder;
import com.yaroslavlancelot.eafall.game.visual.text.DescriptionText;
import com.yaroslavlancelot.eafall.game.visual.text.RecenterText;
import com.yaroslavlancelot.eafall.general.locale.LocaleImpl;

import org.andengine.entity.IEntity;
import org.andengine.entity.sprite.ButtonSprite;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.opengl.font.FontUtils;
import org.andengine.opengl.font.IFont;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

/**
 * The Player`s constructions popup item.
 * In most is just a clickable area with the image and it's description.
 *
 * @author Yaroslav Havrylovych
 */
public class ConstructionPopupItem extends ButtonSprite implements ConstructionsPopupItemFactory.BuildingPopupItem {
    /** buildings popup element height */
    private static final int POPUP_ELEMENT_WIDTH = SizeConstants.CONSTRUCTIONS_POPUP_ELEMENT_WIDTH;
    /** buildings popup element width */
    private static final int POPUP_ELEMENT_HEIGHT = SizeConstants.CONSTRUCTIONS_POPUP_ELEMENT_HEIGHT;


    /** popup image height */
    private static final int ITEM_IMAGE_SIZE = SizeConstants.CONSTRUCTIONS_POPUP_ELEMENT_IMAGE_SIZE;
    /** popup text font */
    private static String FONT = DescriptionText.sFontKey;

    /** displayed object id */
    private BuildingId mBuildingId;

    /* popup elements */
    private Sprite mConstructionSprite;
    private Text mConstructionName;
    private Text mConstructionCost;

    ConstructionPopupItem(float x, float y, VertexBufferObjectManager objectManager) {
        super(x, y,
                (ITiledTextureRegion) TextureRegionHolder.getInstance()
                        .getElement(StringConstants.FILE_CONSTRUCTIONS_POPUP_ITEM),
                objectManager);
        setWidth(POPUP_ELEMENT_WIDTH);
        setHeight(POPUP_ELEMENT_HEIGHT);
    }

    @Override
    public BuildingId getBuildingId() {
        return mBuildingId;
    }

    @Override
    public IEntity getItemEntity() {
        return this;
    }

    @Override
    public void setOnClickListener(final TouchHelper.OnClickListener clickListener) {
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(ButtonSprite pButtonSprite, float pTouchAreaLocalX, float pTouchAreaLocalY) {
                clickListener.onClick();
            }
        });
    }

    @Override
    public void setBuildingId(BuildingId buildingId, String allianceName) {
        // object id
        if (mBuildingId != null && mBuildingId.equals(buildingId)) {
            return;
        }
        mBuildingId = buildingId;

        // image
        BuildingDummy dummy = AllianceHolder.getAlliance(allianceName).getBuildingDummy(mBuildingId);
        mConstructionSprite = new Sprite(
                SizeConstants.CONSTRUCTIONS_POPUP_ELEMENT_IMAGE_X,
                SizeConstants.CONSTRUCTIONS_POPUP_ELEMENT_IMAGE_Y,
                ITEM_IMAGE_SIZE, ITEM_IMAGE_SIZE,
                dummy.getSpriteTextureRegionArray(mBuildingId.getUpgrade()),
                getVertexBufferObjectManager());
        attachChild(mConstructionSprite);

        // text
        attachChild(constructNameText(dummy.getStringId()));
        //oxygen picture
        //TODO this picture loads in another class. Can we move it here? (and create one or two SpriteBatches for handling everything in this popup
        attachChild(new Sprite(SizeConstants.CONSTRUCTIONS_POPUP_ELEMENT_OXYGEN_IMAGE_X,
                SizeConstants.CONSTRUCTIONS_POPUP_ELEMENT_OXYGEN_IMAGE_Y,
                TextureRegionHolder.getRegion(StringConstants.FILE_OXYGEN),
                getVertexBufferObjectManager()));
        //oxygen value
        attachChild(constructCostText(dummy.getCost(buildingId.getUpgrade())));
    }

    private IEntity constructCostText(final int cost) {
        IFont font = FontHolder.getInstance().getElement(FONT);
        mConstructionCost = new RecenterText(
                SizeConstants.CONSTRUCTIONS_POPUP_ELEMENT_OXYGEN_VALUE_X,
                SizeConstants.CONSTRUCTIONS_POPUP_ELEMENT_HEIGHT / 2,
                font, Integer.toString(cost), getVertexBufferObjectManager());
        return mConstructionCost;
    }

    private Text constructNameText(int objectNameId) {
        IFont font = FontHolder.getInstance().getElement(FONT);
        String text = LocaleImpl.getInstance().getStringById(objectNameId);
        float maxWidth = FontUtils.measureText(font, "_12_LETTERS_");
        text = com.yaroslavlancelot.eafall.game.engine.FontUtils
                .splitLinesByMaxWidth(text, font, maxWidth);
        mConstructionName = new RecenterText(
                SizeConstants.CONSTRUCTIONS_POPUP_ELEMENT_NAME_X,
                SizeConstants.CONSTRUCTIONS_POPUP_ELEMENT_HEIGHT / 2,
                font, text, getVertexBufferObjectManager());
        return mConstructionName;
    }
}
