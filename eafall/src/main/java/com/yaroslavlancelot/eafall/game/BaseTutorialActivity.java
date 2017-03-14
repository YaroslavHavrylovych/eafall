package com.yaroslavlancelot.eafall.game;

import android.view.MotionEvent;

import com.yaroslavlancelot.eafall.game.client.thick.single.SinglePlayerGameActivity;
import com.yaroslavlancelot.eafall.game.scene.scenes.EaFallScene;
import com.yaroslavlancelot.eafall.game.campaign.missions.ClickOnPointPopup;

import org.andengine.entity.IEntity;
import org.andengine.input.touch.TouchEvent;

/**
 * Used as base activity which contains helper to show game hints.
 *
 * @author Yaroslav Havrylovych
 */
public class BaseTutorialActivity extends SinglePlayerGameActivity {
    // ===========================================================
    // Constants
    // ===========================================================

    // ===========================================================
    // Fields
    // ===========================================================
    /**
     * used to show hints
     */
    protected ClickOnPointPopup mClickOnPointPopup;

    // ===========================================================
    // Constructors
    // ===========================================================

    // ===========================================================
    // Getter & Setter
    // ===========================================================

    // ===========================================================
    // Methods for/from SuperClass/Interfaces
    // ===========================================================


    @Override
    protected void startAsyncResourceLoading() {
        super.startAsyncResourceLoading();
        ClickOnPointPopup.loadResources(this, getTextureManager());
        ClickOnPointPopup.loadFonts(getFontManager(), getTextureManager());
    }

    @Override
    protected void onPopulateWorkingScene(final EaFallScene scene) {
        if (mEngine == null) {
            finish();
            return;
        }
        super.onPopulateWorkingScene(scene);
        mClickOnPointPopup = new ClickOnPointPopup(mHud, mCamera, getVertexBufferObjectManager());
    }


    // ===========================================================
    // Methods
    // ===========================================================

    /**
     * emulate click on {@link IEntity}
     *
     * @param entity entity to be clicked
     */
    protected void emulateClick(IEntity entity) {
        long time = System.currentTimeMillis();
        entity.onAreaTouched(createTouch(entity, MotionEvent.ACTION_DOWN, time), 0, 0);
        entity.onAreaTouched(createTouch(entity, MotionEvent.ACTION_UP, time), 0, 0);
    }

    /**
     * Emulates touch on entity
     *
     * @param entity entity to be touched
     * @param action {@link MotionEvent} action
     * @param time   action time (for long clicks for example)
     * @return newly create {@link TouchEvent}
     */
    protected TouchEvent createTouch(IEntity entity, int action, long time) {
        long eventTime = time + 100;
        int metaState = 0;
        float x = entity.getX();
        float y = entity.getY();

        MotionEvent motionEvent = MotionEvent.obtain(time, eventTime, action,
                entity.getX(), entity.getY(), metaState);
        return TouchEvent.obtain(x, y, action, 0, motionEvent);
    }
}
