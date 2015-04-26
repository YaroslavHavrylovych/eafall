package com.gmail.yaroslavlancelot.eafall.game.popup.path;

import android.content.Context;

import com.gmail.yaroslavlancelot.eafall.game.constant.SizeConstants;
import com.gmail.yaroslavlancelot.eafall.game.constant.StringConstants;
import com.gmail.yaroslavlancelot.eafall.game.entity.TextureRegionHolder;
import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.building.buildings.ICreepBuilding;
import com.gmail.yaroslavlancelot.eafall.game.eventbus.path.HideUnitPathChooser;
import com.gmail.yaroslavlancelot.eafall.game.eventbus.path.ShowUnitPathChooser;
import com.gmail.yaroslavlancelot.eafall.game.popup.PopupHud;
import com.gmail.yaroslavlancelot.eafall.game.team.TeamsHolder;
import com.gmail.yaroslavlancelot.eafall.game.touch.StaticHelper;
import com.gmail.yaroslavlancelot.eafall.game.visual.buttons.CirclePointButton;

import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.scene.Scene;
import org.andengine.opengl.texture.TextureManager;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.adt.color.Color;

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
        mPopupRectangle = new Rectangle(SizeConstants.HALF_FIELD_WIDTH, SizeConstants.HALF_FIELD_HEIGHT, SizeConstants.GAME_FIELD_WIDTH, SizeConstants.GAME_FIELD_HEIGHT,
                vertexBufferObjectManager);
        mPopupRectangle.setColor(Color.TRANSPARENT);

        initCircles(vertexBufferObjectManager);
        mPopupRectangle.attachChild(mTopCircle);
        mPopupRectangle.attachChild(mBottomCircle);

        EventBus.getDefault().register(this);
    }

    private void initCircles(VertexBufferObjectManager vertexBufferObjectManager) {
        //positions
        mBottomCircle = createCircle(SizeConstants.GAME_FIELD_HEIGHT / 10, vertexBufferObjectManager);
        mTopCircle = createCircle(SizeConstants.GAME_FIELD_HEIGHT * 9 / 10, vertexBufferObjectManager);
        //touch;
        mTopCircle.setTouchCallback(new CircleCustomTouch(mTopCircle, mBottomCircle));
        mBottomCircle.setTouchCallback(new CircleCustomTouch(mBottomCircle, mTopCircle));
    }

    private CirclePointButton createCircle(float y, VertexBufferObjectManager vertexBufferObjectManager) {
        return new CirclePointButton(SizeConstants.GAME_FIELD_WIDTH / 2, y,
                (ITiledTextureRegion) TextureRegionHolder.getInstance().getElement(StringConstants.FILE_CIRCLE_POINT),
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
        boolean isTop =
                ((ICreepBuilding) TeamsHolder.getTeam(mTeamName).getPlanet().getBuilding(mBuildingId))
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
        ((ICreepBuilding) TeamsHolder.getTeam(mTeamName).getPlanet().getBuilding(mBuildingId))
                .setPath(mTopCircle.isActive());
        super.hidePopup();
        EventBus.getDefault().post(new HideUnitPathChooser());
    }

    private static class CircleCustomTouch extends StaticHelper.CustomTouchListener {
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
