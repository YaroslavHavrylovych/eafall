package com.gmail.yaroslavlancelot.eafall.game.sandbox.activity;

import com.gmail.yaroslavlancelot.eafall.android.LoggerHelper;
import com.gmail.yaroslavlancelot.eafall.game.BaseGameObjectsActivity;
import com.gmail.yaroslavlancelot.eafall.game.constant.CollisionCategories;
import com.gmail.yaroslavlancelot.eafall.game.constant.SizeConstants;
import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.unit.offence.path.IUnitPath;
import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.unit.offence.path.implementation.MoveToCenterPath;
import com.gmail.yaroslavlancelot.eafall.game.player.IPlayer;
import com.gmail.yaroslavlancelot.eafall.game.scene.scenes.EaFallScene;

import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.input.touch.detector.ClickDetector;

/**
 * Game sandbox. You can create any unit to play with.
 *
 * @author Yaroslav Havrylovych
 */
public class SandboxActivity extends BaseGameObjectsActivity {
    // ===========================================================
    // Constants
    // ===========================================================

    // ===========================================================
    // Fields
    // ===========================================================
    private IPlayer mCurrentPlayer;
    private int mCurrentUnitKey;

    // ===========================================================
    // Constructors
    // ===========================================================

    // ===========================================================
    // Getter & Setter
    // ===========================================================

    @Override
    public void onResourcesLoaded() {
        super.onResourcesLoaded();
        hideSplash();
    }

    // ===========================================================
    // Methods for/from SuperClass/Interfaces
    // ===========================================================
    @Override
    protected void hideSplash() {
        mCurrentPlayer = mFirstPlayer;
        mCurrentUnitKey = mFirstPlayer.getAlliance().getUnitsIds().first();
        createBounds();
        super.hideSplash();
    }

    @Override
    protected void initWorkingScene() {
        super.initWorkingScene();
        EaFallScene scene = mSceneManager.getWorkingScene();
        scene.setClickListener(new ClickDetector.IClickDetectorListener() {
            @Override
            public void onClick(final ClickDetector pClickDetector, final int pPointerID, final float pSceneX, final float pSceneY) {
                IUnitPath path = new MoveToCenterPath();
                createMovableUnit(mCurrentPlayer, mCurrentUnitKey, (int) pSceneX, (int) pSceneY, path);
            }
        });
    }

    // ===========================================================
    // Methods
    // ===========================================================

    /** bound for objects so they can't get out of the screen */
    private void createBounds() {
        LoggerHelper.methodInvocation(TAG, "createBounds");
        PhysicsFactory.createLineBody(
                mPhysicsWorld, -1, -1, -1, SizeConstants.GAME_FIELD_HEIGHT + 1,
                CollisionCategories.STATIC_BODY_FIXTURE_DEF);
        PhysicsFactory.createLineBody(
                mPhysicsWorld, -1, -1, SizeConstants.GAME_FIELD_WIDTH + 1, -1,
                CollisionCategories.STATIC_BODY_FIXTURE_DEF);
        PhysicsFactory.createLineBody(
                mPhysicsWorld, SizeConstants.GAME_FIELD_WIDTH + 1, -1, SizeConstants.GAME_FIELD_WIDTH + 1,
                SizeConstants.GAME_FIELD_WIDTH + 1, CollisionCategories.STATIC_BODY_FIXTURE_DEF);
        PhysicsFactory.createLineBody(mPhysicsWorld, SizeConstants.GAME_FIELD_WIDTH + 1,
                SizeConstants.GAME_FIELD_HEIGHT + 1, -1, SizeConstants.GAME_FIELD_HEIGHT + 1,
                CollisionCategories.STATIC_BODY_FIXTURE_DEF);
    }

    // ===========================================================
    // Inner and Anonymous Classes
    // ===========================================================
}
