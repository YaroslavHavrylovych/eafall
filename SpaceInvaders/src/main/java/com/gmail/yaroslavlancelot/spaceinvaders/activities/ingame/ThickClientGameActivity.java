package com.gmail.yaroslavlancelot.spaceinvaders.activities.ingame;

import com.gmail.yaroslavlancelot.spaceinvaders.constants.SizeConstants;
import com.gmail.yaroslavlancelot.spaceinvaders.constants.TeamControlBehaviourType;
import com.gmail.yaroslavlancelot.spaceinvaders.gameloop.MoneyUpdateCycle;
import com.gmail.yaroslavlancelot.spaceinvaders.races.IRace;
import com.gmail.yaroslavlancelot.spaceinvaders.teams.ITeam;
import com.gmail.yaroslavlancelot.spaceinvaders.teams.Team;
import com.gmail.yaroslavlancelot.spaceinvaders.utils.LoggerHelper;
import com.gmail.yaroslavlancelot.spaceinvaders.utils.TeamUtils;

import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.extension.physics.box2d.PhysicsFactory;

/**
 * extends {@link MainOperationsBaseGameActivity} with adding world boundaries and
 * registering money update cycles (not used for client because all money logic handles on server)
 */
public abstract class ThickClientGameActivity extends MainOperationsBaseGameActivity {
    /** tag, which is used for debugging purpose */
    public static final String TAG = ThickClientGameActivity.class.getCanonicalName();

    /**
     * initialize physic world. For using in child classes without accessing private fields.
     */
    @Override
    protected void initThickClient() {
        mHud.registerUpdateHandler(new TimerHandler(MONEY_UPDATE_TIME, true, new MoneyUpdateCycle(mTeams)));
        createBounds();
    }

    @Override
    protected ITeam createTeam(String teamNameInExtra, IRace race) {
        final ITeam team = super.createTeam(teamNameInExtra, race);
        if((team instanceof Team) && (team.getTeamControlType() == TeamControlBehaviourType.USER_CONTROL_ON_SERVER_SIDE)) {
            ((Team) team).setMoneyChangedCallback(new Team.IMoneyChangedCallback() {
                @Override
                public void moneyChanged(int delta) {
                    updateMoneyTextOnScreen(TeamUtils.getMoneyString(mMoneyTextPrefixString, team));
                }
            });
        }
        return team;
    }

    /** bound for objects so they can't get out of the screen */
    private void createBounds() {
        LoggerHelper.methodInvocation(TAG, "createBounds");
        PhysicsFactory.createLineBody(
                mPhysicsWorld, -1, -1, -1, SizeConstants.GAME_FIELD_HEIGHT + 1, mStaticBodyFixtureDef);
        PhysicsFactory.createLineBody(
                mPhysicsWorld, -1, -1, SizeConstants.GAME_FIELD_WIDTH + 1, -1, mStaticBodyFixtureDef);
        PhysicsFactory.createLineBody(
                mPhysicsWorld, SizeConstants.GAME_FIELD_WIDTH + 1, -1, SizeConstants.GAME_FIELD_WIDTH + 1,
                SizeConstants.GAME_FIELD_WIDTH + 1, mStaticBodyFixtureDef);
        PhysicsFactory.createLineBody(mPhysicsWorld, SizeConstants.GAME_FIELD_WIDTH + 1,
                SizeConstants.GAME_FIELD_HEIGHT + 1, -1, SizeConstants.GAME_FIELD_HEIGHT + 1, mStaticBodyFixtureDef);
    }
}
