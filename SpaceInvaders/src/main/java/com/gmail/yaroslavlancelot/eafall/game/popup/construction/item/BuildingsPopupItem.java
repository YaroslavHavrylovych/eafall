package com.gmail.yaroslavlancelot.eafall.game.popup.construction.item;

import com.gmail.yaroslavlancelot.eafall.game.alliance.AllianceHolder;
import com.gmail.yaroslavlancelot.eafall.game.constant.Sizes;
import com.gmail.yaroslavlancelot.eafall.game.constant.StringsAndPath;
import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.building.BuildingId;
import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.building.dummy.BuildingDummy;
import com.gmail.yaroslavlancelot.eafall.game.touch.StaticHelper;
import com.gmail.yaroslavlancelot.eafall.game.visual.font.FontHolder;
import com.gmail.yaroslavlancelot.eafall.general.locale.LocaleImpl;
import com.gmail.yaroslavlancelot.eafall.game.entity.TextureRegionHolder;

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
    private static final int POPUP_ELEMENT_WIDTH = Sizes.BUILDING_POPUP_BACKGROUND_ITEM_WIDTH;
    /** buildings popup element width */
    private static final int POPUP_ELEMENT_HEIGHT = Sizes.BUILDING_POPUP_BACKGROUND_ITEM_HEIGHT;


    /** popup image height */
    private static final int ITEM_IMAGE_HEIGHT = Sizes.BUILDING_POPUP_ELEMENT_HEIGHT;
    /** popup image height */
    private static final int ITEM_IMAGE_WIDTH = ITEM_IMAGE_HEIGHT;
    /* for the text displaying */
    private static final String sNumberFormatTemplate = "####' : '";
    private static final NumberFormat sNumberFormat = new DecimalFormat(sNumberFormatTemplate);
    /** popup text font */
    private static String FONT = StringsAndPath.KEY_FONT_MONEY;

    /** displayed object id */
    private BuildingId mBuildingId;

    /* popup elements */
    private Sprite mStaticObject;
    private Text mText;

    BuildingsPopupItem(float x, float y, VertexBufferObjectManager objectManager) {
        super(x, y,
                (ITiledTextureRegion) TextureRegionHolder.getInstance().getElement(StringsAndPath.FILE_POPUP_BACKGROUND_ITEM),
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
        mStaticObject = new Sprite(Sizes.BUILDING_POPUP_IMAGE_PADDING, Sizes.BUILDING_POPUP_IMAGE_PADDING,
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
        IFont font = FontHolder.getInstance().getElement(FONT);
        mText = new Text(Sizes.BUILDING_POPUP_BACKGROUND_ITEM_HEIGHT,
                Sizes.BUILDING_POPUP_ELEMENT_HEIGHT + Sizes.BUILDING_POPUP_IMAGE_PADDING - font.getLineHeight(),
                font, textString, getVertexBufferObjectManager());
    }

    @Override
    public BuildingId getBuildingId() {
        return mBuildingId;
    }

    @Override
    public void setOnClickListener(final StaticHelper.OnClickListener clickListener) {
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
