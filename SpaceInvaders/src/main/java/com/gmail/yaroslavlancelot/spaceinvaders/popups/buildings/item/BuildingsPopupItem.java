package com.gmail.yaroslavlancelot.spaceinvaders.popups.buildings.item;

import com.gmail.yaroslavlancelot.spaceinvaders.alliances.AllianceHolder;
import com.gmail.yaroslavlancelot.spaceinvaders.constants.SizeConstants;
import com.gmail.yaroslavlancelot.spaceinvaders.constants.StringsAndPathUtils;
import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.objects.buildings.BuildingId;
import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.objects.dummies.BuildingDummy;
import com.gmail.yaroslavlancelot.spaceinvaders.utils.FontHolderUtils;
import com.gmail.yaroslavlancelot.spaceinvaders.utils.LocaleImpl;
import com.gmail.yaroslavlancelot.spaceinvaders.utils.TextureRegionHolderUtils;
import com.gmail.yaroslavlancelot.spaceinvaders.utils.TouchUtils;

import org.andengine.entity.IEntity;
import org.andengine.entity.sprite.ButtonSprite;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.opengl.font.IFont;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import java.text.DecimalFormat;
import java.text.NumberFormat;

public class BuildingsPopupItem extends ButtonSprite implements PopupItemFactory.BuildingPopupItem {
    /** buildings popup element height */
    private static final int POPUP_ELEMENT_WIDTH = SizeConstants.BUILDING_POPUP_BACKGROUND_ITEM_WIDTH;
    /** buildings popup element width */
    private static final int POPUP_ELEMENT_HEIGHT = SizeConstants.BUILDING_POPUP_BACKGROUND_ITEM_HEIGHT;


    /** popup image height */
    private static final int ITEM_IMAGE_HEIGHT = SizeConstants.BUILDING_POPUP_ELEMENT_HEIGHT;
    /** popup image height */
    private static final int ITEM_IMAGE_WIDTH = ITEM_IMAGE_HEIGHT;
    /* for the text displaying */
    private static final String sNumberFormatTemplate = "####' : '";
    private static final NumberFormat sNumberFormat = new DecimalFormat(sNumberFormatTemplate);
    /** popup text font */
    private static String FONT = StringsAndPathUtils.KEY_FONT_MONEY;

    /** displayed object id */
    private BuildingId mBuildingId;

    /* popup elements */
    private Sprite mStaticObject;
    private Text mText;

    BuildingsPopupItem(float x, float y, VertexBufferObjectManager objectManager) {
        super(x, y,
                (ITiledTextureRegion) TextureRegionHolderUtils.getInstance().getElement(StringsAndPathUtils.FILE_POPUP_BACKGROUND_ITEM),
                objectManager);
        setWidth(POPUP_ELEMENT_WIDTH);
        setHeight(POPUP_ELEMENT_HEIGHT);
    }

    @Override
    public void setBuildingId(BuildingId buildingId, String raceName) {
        // object id
        if (mBuildingId != null && mBuildingId.equals(buildingId)) {
            return;
        }
        mBuildingId = buildingId;

        // image
        BuildingDummy dummy = AllianceHolder.getRace(raceName).getBuildingDummy(mBuildingId);
        mStaticObject = new Sprite(SizeConstants.BUILDING_POPUP_IMAGE_PADDING, SizeConstants.BUILDING_POPUP_IMAGE_PADDING,
                ITEM_IMAGE_WIDTH, ITEM_IMAGE_HEIGHT,
                dummy.getTextureRegionArray(mBuildingId.getUpgrade()),
                getVertexBufferObjectManager());
        attachChild(mStaticObject);

        // text
        initText(dummy.getStringId(), dummy.getCost(mBuildingId.getUpgrade()));
        attachChild(mText);
    }

    private void initText(int objectNameId, int cost) {
        String textString = sNumberFormat.format(cost) + LocaleImpl.getInstance().getStringById(objectNameId);
        IFont font = FontHolderUtils.getInstance().getElement(FONT);
        mText = new Text(SizeConstants.BUILDING_POPUP_BACKGROUND_ITEM_HEIGHT,
                SizeConstants.BUILDING_POPUP_ELEMENT_HEIGHT + SizeConstants.BUILDING_POPUP_IMAGE_PADDING - font.getLineHeight(),
                font, textString, getVertexBufferObjectManager());
    }

    @Override
    public BuildingId getBuildingId() {
        return mBuildingId;
    }

    @Override
    public void setOnClickListener(final TouchUtils.OnClickListener clickListener) {
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(ButtonSprite pButtonSprite, float pTouchAreaLocalX, float pTouchAreaLocalY) {
                clickListener.onClick();
            }
        });
    }

    @Override
    public IEntity getItemEntity() {
        return this;
    }
}
