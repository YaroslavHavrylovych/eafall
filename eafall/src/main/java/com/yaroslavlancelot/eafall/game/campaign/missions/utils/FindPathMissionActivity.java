package com.yaroslavlancelot.eafall.game.campaign.missions.utils;

import android.widget.Toast;

import com.yaroslavlancelot.eafall.R;
import com.yaroslavlancelot.eafall.game.BaseGameObjectsActivity;
import com.yaroslavlancelot.eafall.game.alliance.IAlliance;
import com.yaroslavlancelot.eafall.game.configuration.mission.MissionConfig;
import com.yaroslavlancelot.eafall.game.constant.CollisionCategories;
import com.yaroslavlancelot.eafall.game.constant.SizeConstants;
import com.yaroslavlancelot.eafall.game.entity.gameobject.unit.offence.OffenceUnit;
import com.yaroslavlancelot.eafall.game.entity.gameobject.unit.offence.path.IUnitPath;
import com.yaroslavlancelot.eafall.game.entity.gameobject.unit.offence.path.implementation.MoveByClickUnitPath;
import com.yaroslavlancelot.eafall.game.entity.gameobject.unit.offence.path.implementation.SingleWayUnitPath;
import com.yaroslavlancelot.eafall.game.player.IPlayer;
import com.yaroslavlancelot.eafall.game.player.Player;
import com.yaroslavlancelot.eafall.game.scene.hud.BaseGameHud;
import com.yaroslavlancelot.eafall.game.scene.hud.FindPathGameHud;
import com.yaroslavlancelot.eafall.game.scene.scenes.EaFallScene;

import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.input.touch.detector.ClickDetector;

/** basic activity to create obstacles for the unit, which has to go from point A to point B */
public abstract class FindPathMissionActivity extends BaseGameObjectsActivity {
    // ===========================================================
    // Constants
    // ===========================================================

    // ===========================================================
    // Fields
    // ===========================================================
    private OffenceUnit mMainUnit;
    private MoveByClickUnitPath mMainUnitPath;

    // ===========================================================
    // Constructors
    // ===========================================================

    // ===========================================================
    // Getter & Setter
    // ===========================================================

    @Override
    protected BaseGameHud createHud() {
        return new FindPathGameHud();
    }

    @Override
    public void onResourcesLoaded() {
        super.onResourcesLoaded();
        hideSplash();
    }

    // ===========================================================
    // Methods for/from SuperClass/Interfaces
    // ===========================================================

    protected abstract int[] getMainUnitCoords();

    protected abstract int getMainUnitId();

    @Override
    protected void initWorkingScene() {
        super.initWorkingScene();
        EaFallScene scene = mSceneManager.getWorkingScene();
        int[] position = getMainUnitCoords();
        mMainUnitPath = new MoveByClickUnitPath(position[0], position[1]);
        mMainUnit = createMovableUnit(mFirstPlayer, getMainUnitId(),
                position[0], position[1], mMainUnitPath);
        scene.setClickListener(new ClickDetector.IClickDetectorListener() {
            @Override
            public void onClick(final ClickDetector pClickDetector, final int pPointerID,
                                final float pSceneX, final float pSceneY) {
                mMainUnitPath.setNewPoint(pSceneX, pSceneY);
            }
        });
    }

    @Override
    protected void hideSplash() {
        createBounds();
        super.hideSplash();
    }

    @Override
    protected void onShowWorkingScene() {
        super.onShowWorkingScene();
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(FindPathMissionActivity.this, R.string.sandbox_hint,
                        Toast.LENGTH_LONG)
                        .show();
            }
        });
    }

    @Override
    protected IPlayer createPlayer(String name, IAlliance alliance, IPlayer.ControlType playerType,
                                   int startMoney, int buildingsLimit, MissionConfig missionConfig) {
        return new Player(name, alliance, playerType, 0, -1, buildingsLimit, missionConfig);
    }

    // ===========================================================
    // Methods
    // ===========================================================

    /** bound for objects so they can't get out of the screen */
    private void createBounds() {
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
