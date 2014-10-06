package com.gmail.yaroslavlancelot.spaceinvaders.popups.buildings;

import android.content.Context;

import com.gmail.yaroslavlancelot.spaceinvaders.constants.GameStringsConstantsAndUtils;
import com.gmail.yaroslavlancelot.spaceinvaders.constants.SizeConstants;
import com.gmail.yaroslavlancelot.spaceinvaders.eventbus.description.BuildingDescriptionShowEvent;
import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.objects.buildings.CreepBuildingDummy;
import com.gmail.yaroslavlancelot.spaceinvaders.races.IRace;
import com.gmail.yaroslavlancelot.spaceinvaders.teams.ITeam;
import com.gmail.yaroslavlancelot.spaceinvaders.utils.FontHolderUtils;
import com.gmail.yaroslavlancelot.spaceinvaders.utils.LocaleImpl;
import com.gmail.yaroslavlancelot.spaceinvaders.utils.LoggerHelper;
import com.gmail.yaroslavlancelot.spaceinvaders.utils.TextureRegionHolderUtils;
import com.gmail.yaroslavlancelot.spaceinvaders.utils.TouchUtils;

import org.andengine.engine.camera.hud.HUD;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.scene.ITouchArea;
import org.andengine.entity.sprite.ButtonSprite;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.opengl.font.FontUtils;
import org.andengine.opengl.font.IFont;
import org.andengine.opengl.texture.TextureManager;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

public abstract class BuildingsPopup extends HUD {
    public static final String TAG = BuildingsPopup.class.getCanonicalName();
    /** buildings popup element height */
    private static final int POPUP_ELEMENT_HEIGHT = SizeConstants.BUILDING_POPUP_BACKGROUND_ITEM_HEIGHT;
    /** popup image height */
    private static final int ITEM_IMAGE_HEIGHT = SizeConstants.BUILDING_POPUP_ELEMENT_HEIGHT;
    /** popup image height */
    private static final int ITEM_IMAGE_WIDTH = ITEM_IMAGE_HEIGHT;
    private static final String sNumberFormatTemplate = "####' : '";
    private static final NumberFormat sNumberFormat = new DecimalFormat(sNumberFormatTemplate);
    /** popup text font */
    private static String FONT = GameStringsConstantsAndUtils.KEY_FONT_MONEY;
    /** represent boolean value which true if popup is showing now and false in other way */
    private boolean mIsPopupShowing;
    /** */
    private ITeam mTeam;
    /** */
    private List<BuildingsPopupItem> mItems;
    /** area for popup (all other area is transparent) */
    private Rectangle mPopupRectangle;

    public BuildingsPopup(ITeam team, VertexBufferObjectManager vertexBufferObjectManager) {
        setBackgroundEnabled(false);
        setTouchAreaBindingOnActionDownEnabled(true);
        setOnAreaTouchTraversalFrontToBack();

        mPopupRectangle = new Rectangle(0, 0, calculatePopupWidth(team),
                SizeConstants.BUILDING_POPUP_BACKGROUND_ITEM_HEIGHT * team.getTeamRace().getBuildingsAmount(),
                vertexBufferObjectManager);
        initBuildingPopupForTeam(mTeam = team);
        mPopupRectangle.setX(SizeConstants.GAME_FIELD_WIDTH / 2 - mPopupRectangle.getWidth() / 2);
        mPopupRectangle.setY(SizeConstants.GAME_FIELD_HEIGHT - mPopupRectangle.getHeight());

        setOnSceneTouchListener(new TouchUtils.SceneTouchListener() {
            @Override
            public void click() {
                super.click();
                hide();
            }
        });
    }

    public static float calculatePopupWidth(ITeam team) {
        float lengthWithoutText = SizeConstants.BUILDING_POPUP_BACKGROUND_ITEM_HEIGHT
                + SizeConstants.BUILDING_POPUP_AFTER_TEXT_PADDING;

        IFont font = FontHolderUtils.getInstance().getElement(FONT);

        float maxTextLength = 0f;
        for (String buildingName : team.getTeamRace().getBuildingsNames())
            maxTextLength = Math.max(FontUtils.measureText(font, buildingName), maxTextLength);

        float additionLength = FontUtils.measureText(font, sNumberFormatTemplate.replaceAll("'", ""));
        return lengthWithoutText + maxTextLength + additionLength;
    }

    private void initBuildingPopupForTeam(ITeam team) {
        IRace race = team.getTeamRace();
        mItems = new ArrayList<BuildingsPopupItem>(team.getTeamRace().getBuildingsAmount());
        // init elements
        float width = mPopupRectangle.getWidth();
        for (int buildingId = 0; buildingId < race.getBuildingsAmount(); buildingId++) {
            BuildingsPopupItem item = new BuildingsPopupItem(mTeam, buildingId,
                    0, buildingId * POPUP_ELEMENT_HEIGHT, width, POPUP_ELEMENT_HEIGHT,
                    mPopupRectangle.getVertexBufferObjectManager());
            mItems.add(item.init());
            mPopupRectangle.attachChild(item);
        }
    }

    private synchronized void hide() {
        if (!mIsPopupShowing) {
            return;
        }
        detachScene();
        for (int i = 0; i < mPopupRectangle.getChildCount(); i++) {
            unregisterTouchArea((ITouchArea) mPopupRectangle.getChildByIndex(i));
        }
        detachChild(mPopupRectangle);
        unregisterTouchArea(mPopupRectangle);
        mIsPopupShowing = false;
    }

    public abstract void detachScene();

    public static void loadResource(Context context, TextureManager textureManager) {
        BitmapTextureAtlas smallObjectTexture = new BitmapTextureAtlas(textureManager, 1200, 100, TextureOptions.BILINEAR);
        TextureRegionHolderUtils.addTiledElementFromAssets(GameStringsConstantsAndUtils.FILE_POPUP_BACKGROUND_ITEM,
                TextureRegionHolderUtils.getInstance(), smallObjectTexture, context, 0, 0, 2, 1);
        smallObjectTexture.load();
    }

    /** will show or hide popup depending on current state */
    public synchronized void triggerPopup() {
        LoggerHelper.printDebugMessage(TAG, "show popup = " + !mIsPopupShowing);
        if (mIsPopupShowing) {
            hide();
        } else {
            show();
        }
    }

    private synchronized void show() {
        if (mIsPopupShowing) {
            return;
        }
        attachScene();
        for (int i = 0; i < mPopupRectangle.getChildCount(); i++) {
            registerTouchArea((ITouchArea) mPopupRectangle.getChildByIndex(i));
        }
        attachChild(mPopupRectangle);
        registerTouchArea(mPopupRectangle);
        mIsPopupShowing = true;
    }

    public abstract void attachScene();

    /** represent popup item */
    private class BuildingsPopupItem extends ButtonSprite {
        private Sprite mStaticObject;
        private Text mText;
        private ITeam mTeam;
        private int mObjectId;

        private BuildingsPopupItem(ITeam team, int objectId, float x, float y, float width, float height, VertexBufferObjectManager vertexBufferObjectManager) {
            super(x, y, (ITiledTextureRegion) TextureRegionHolderUtils.getInstance().getElement(GameStringsConstantsAndUtils.FILE_POPUP_BACKGROUND_ITEM),
                    vertexBufferObjectManager);
            setWidth(width);
            setHeight(height);

            CreepBuildingDummy dummy = (mTeam = team).getTeamRace().getBuildingDummy((mObjectId = objectId));
            mStaticObject = new Sprite(SizeConstants.BUILDING_POPUP_IMAGE_PADDING, SizeConstants.BUILDING_POPUP_IMAGE_PADDING,
                    ITEM_IMAGE_WIDTH, ITEM_IMAGE_HEIGHT, dummy.getTextureRegion(), getVertexBufferObjectManager());

            initText(dummy.getNameId(), dummy.getCost());
        }

        private void initText(int objectNameId, int cost) {
            String textString = sNumberFormat.format(cost) + LocaleImpl.getInstance().getStringById(objectNameId);
            IFont font = FontHolderUtils.getInstance().getElement(FONT);
            mText = new Text(SizeConstants.BUILDING_POPUP_BACKGROUND_ITEM_HEIGHT,
                    SizeConstants.BUILDING_POPUP_ELEMENT_HEIGHT + SizeConstants.BUILDING_POPUP_IMAGE_PADDING - font.getLineHeight(),
                    font, textString, getVertexBufferObjectManager());
        }

        public BuildingsPopupItem init() {
            attachChild(mStaticObject);
            attachChild(mText);

            setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(ButtonSprite pButtonSprite, float pTouchAreaLocalX, float pTouchAreaLocalY) {
                    LoggerHelper.printDebugMessage(TAG, "show building description");
                    if (mIsPopupShowing) {
                        hide();
                    }
                    EventBus.getDefault().post(new BuildingDescriptionShowEvent(mObjectId, mTeam.getTeamName()));
                }
            });

            return this;
        }
    }
}
