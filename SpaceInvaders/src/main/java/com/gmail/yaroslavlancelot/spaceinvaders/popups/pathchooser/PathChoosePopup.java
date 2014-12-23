package com.gmail.yaroslavlancelot.spaceinvaders.popups.pathchooser;

import android.content.Context;

import com.gmail.yaroslavlancelot.spaceinvaders.constants.StringsAndPathUtils;
import com.gmail.yaroslavlancelot.spaceinvaders.constants.SizeConstants;
import com.gmail.yaroslavlancelot.spaceinvaders.eventbus.unitpath.HideUnitPathChooser;
import com.gmail.yaroslavlancelot.spaceinvaders.eventbus.unitpath.ShowUnitPathChooser;
import com.gmail.yaroslavlancelot.spaceinvaders.popups.PopupHud;
import com.gmail.yaroslavlancelot.spaceinvaders.teams.TeamsHolder;
import com.gmail.yaroslavlancelot.spaceinvaders.utils.TextureRegionHolderUtils;
import com.gmail.yaroslavlancelot.spaceinvaders.utils.TouchUtils;
import com.gmail.yaroslavlancelot.spaceinvaders.visualelements.buttons.CirclePointButton;

import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.scene.Scene;
import org.andengine.opengl.texture.TextureManager;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.color.Color;

import de.greenrobot.event.EventBus;

/** */
public class PathChoosePopup extends PopupHud {
    public static final String KEY = PathChoosePopup.class.getCanonicalName();
    private CirclePointButton mTopCircle;
    private CirclePointButton mBottomCircle;
    private String mTeamName;
    private int mBuildingId;

    public PathChoosePopup(Scene scene, VertexBufferObjectManager vertexBufferObjectManager) {
        super(scene);
        mPopupRectangle = new Rectangle(0, 0, SizeConstants.GAME_FIELD_WIDTH, SizeConstants.GAME_FIELD_HEIGHT,
                vertexBufferObjectManager);
        mPopupRectangle.setColor(Color.TRANSPARENT);

        initCircles(vertexBufferObjectManager);
        mPopupRectangle.attachChild(mTopCircle);
        mPopupRectangle.attachChild(mBottomCircle);

        EventBus.getDefault().register(this);
    }

    private void initCircles(VertexBufferObjectManager vertexBufferObjectManager) {
        //positions
        mTopCircle = createCircle(SizeConstants.GAME_FIELD_HEIGHT / 10, vertexBufferObjectManager);
        mBottomCircle = createCircle(SizeConstants.GAME_FIELD_HEIGHT * 9 / 10, vertexBufferObjectManager);
        mTopCircle.setX(mTopCircle.getX() - mTopCircle.getWidth() / 2);
        mTopCircle.setY(mTopCircle.getY() - mTopCircle.getHeight() / 2);
        mBottomCircle.setX(mBottomCircle.getX() - mBottomCircle.getWidth() / 2);
        mBottomCircle.setY(mBottomCircle.getY() - mBottomCircle.getHeight() / 2);
        //touch;
        mTopCircle.setTouchCallback(new CircleCustomTouch(mTopCircle, mBottomCircle));
        mBottomCircle.setTouchCallback(new CircleCustomTouch(mBottomCircle, mTopCircle));
    }

    private CirclePointButton createCircle(float y, VertexBufferObjectManager vertexBufferObjectManager) {
        return new CirclePointButton(SizeConstants.GAME_FIELD_WIDTH / 2, y,
                (ITiledTextureRegion) TextureRegionHolderUtils.getInstance().getElement(StringsAndPathUtils.FILE_CIRCLE_POINT),
                vertexBufferObjectManager);
    }

    public static void loadResources(Context context, TextureManager textureManager) {
        CirclePointButton.loadResources(context, textureManager);
    }

    @SuppressWarnings("unused")
    /** really used by {@link de.greenrobot.event.EventBus} */
    public void onEvent(final ShowUnitPathChooser showUnitPathChooser) {
        mTeamName = showUnitPathChooser.getTeamName();
        mBuildingId = showUnitPathChooser.getBuildingId();
        boolean isTop = TeamsHolder.getTeam(mTeamName).getTeamPlanet().getBuilding(mBuildingId)
                .isTopPath();
        if (isTop) {
            mTopCircle.setActive();
            mBottomCircle.setDeactivated();
        } else {
            mTopCircle.setDeactivated();
            mBottomCircle.setActive();
        }
        showPopup();
    }

    @Override
    public synchronized void hidePopup() {
        TeamsHolder.getTeam(mTeamName).getTeamPlanet().getBuilding(mBuildingId)
                .setPath(mTopCircle.isActive());
        super.hidePopup();
        EventBus.getDefault().post(new HideUnitPathChooser());
    }

    private static class CircleCustomTouch extends TouchUtils.CustomTouchListener {
        private CirclePointButton mButton;
        private CirclePointButton mOppositeButton;

        public CircleCustomTouch(CirclePointButton button, CirclePointButton oppositeButton) {
            super(button);
            mButton = button;
            mOppositeButton = oppositeButton;
        }

        @Override
        public void click() {
            super.click();
            swapStates(mButton, mOppositeButton);
        }

        /** it's static and synchronized to sync all click between mult buttons */
        private synchronized static void swapStates(CirclePointButton button1, CirclePointButton button2) {
            button1.setActive();
            button2.setDeactivated();
        }
    }
}
