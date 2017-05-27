package com.yaroslavlancelot.eafall.game.sandbox.activity;

import android.widget.Toast;

import com.yaroslavlancelot.eafall.R;
import com.yaroslavlancelot.eafall.game.BaseGameObjectsActivity;
import com.yaroslavlancelot.eafall.game.alliance.IAlliance;
import com.yaroslavlancelot.eafall.game.audio.GeneralSoundKeys;
import com.yaroslavlancelot.eafall.game.audio.SoundFactory;
import com.yaroslavlancelot.eafall.game.configuration.mission.MissionConfig;
import com.yaroslavlancelot.eafall.game.constant.CollisionCategories;
import com.yaroslavlancelot.eafall.game.constant.SizeConstants;
import com.yaroslavlancelot.eafall.game.entity.gameobject.unit.offence.path.IUnitPath;
import com.yaroslavlancelot.eafall.game.entity.gameobject.unit.offence.path.implementation.MoveToPointThroughTheCenterPath;
import com.yaroslavlancelot.eafall.game.events.SharedEvents;
import com.yaroslavlancelot.eafall.game.events.aperiodic.ingame.ShowToastEvent;
import com.yaroslavlancelot.eafall.game.player.IPlayer;
import com.yaroslavlancelot.eafall.game.player.Player;
import com.yaroslavlancelot.eafall.game.sandbox.popup.SandboxDescriptionPopup;
import com.yaroslavlancelot.eafall.game.scene.hud.BaseGameHud;
import com.yaroslavlancelot.eafall.game.scene.hud.SandboxGameHud;
import com.yaroslavlancelot.eafall.game.scene.scenes.EaFallScene;

import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.input.touch.detector.ClickDetector;

import de.greenrobot.event.EventBus;

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
    private int mCurrentUnitKey;

    // ===========================================================
    // Constructors
    // ===========================================================

    // ===========================================================
    // Getter & Setter
    // ===========================================================

    @Override
    protected BaseGameHud createHud() {
        return new SandboxGameHud() {
            @Override
            protected int getCurrentUnitId() {
                return mCurrentUnitKey;
            }
        };
    }

    @Override
    public void onResourcesLoaded() {
        super.onResourcesLoaded();
        hideSplash();
    }

    // ===========================================================
    // Methods for/from SuperClass/Interfaces
    // ===========================================================

    @Override
    protected void initWorkingScene() {
        super.initWorkingScene();
        SharedEvents.addCallback(new SharedEvents.DataChangedCallback(SandboxDescriptionPopup.SANDBOX_UNIT_CHANGED_KEY) {
            @Override
            public void callback(final String key, final Object value) {
                mCurrentUnitKey = (Integer) value;
            }
        });
        EaFallScene scene = mSceneManager.getWorkingScene();
        final IUnitPath path1 = new MoveToPointThroughTheCenterPath(
                SizeConstants.HALF_FIELD_WIDTH + SizeConstants.HALF_FIELD_WIDTH / 2,
                SizeConstants.HALF_FIELD_HEIGHT);
        final IUnitPath path2 = new MoveToPointThroughTheCenterPath(
                SizeConstants.HALF_FIELD_WIDTH / 2, SizeConstants.HALF_FIELD_HEIGHT);
        scene.setClickListener(new ClickDetector.IClickDetectorListener() {
            @Override
            public void onClick(final ClickDetector pClickDetector, final int pPointerID, final float pSceneX, final float pSceneY) {
                IPlayer player;
                IUnitPath path;
                if (pSceneX < SizeConstants.HALF_FIELD_WIDTH) {
                    player = mFirstPlayer;
                    path = path1;
                } else {
                    player = mSecondPlayer;
                    path = path2;
                }
                if (player.getUnitsAmount() < player.getUnitsLimit()) {
                    SoundFactory.getInstance().playSound(GeneralSoundKeys.TICK);
                    createMovableUnit(player, mCurrentUnitKey, (int) pSceneX, (int) pSceneY, path);
                } else {
                    SoundFactory.getInstance().playSound(GeneralSoundKeys.DENIED);
                    EventBus.getDefault().post(
                            new ShowToastEvent(false, R.string.units_capacity_reached));
                }
            }
        });
    }

    @Override
    protected void hideSplash() {
        mCurrentUnitKey = mFirstPlayer.getAlliance().getUnitsIds().first();
        createBounds();
        super.hideSplash();
    }

    @Override
    protected void onShowWorkingScene() {
        super.onShowWorkingScene();
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(SandboxActivity.this, R.string.sandbox_hint, Toast.LENGTH_LONG).show();
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
