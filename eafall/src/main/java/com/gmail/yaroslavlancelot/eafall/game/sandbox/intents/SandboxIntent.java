package com.gmail.yaroslavlancelot.eafall.game.sandbox.intents;

import com.gmail.yaroslavlancelot.eafall.game.alliance.mutants.Mutants;
import com.gmail.yaroslavlancelot.eafall.game.configuration.mission.MissionConfig;
import com.gmail.yaroslavlancelot.eafall.game.constant.StringConstants;
import com.gmail.yaroslavlancelot.eafall.game.mission.MissionIntent;
import com.gmail.yaroslavlancelot.eafall.game.player.IPlayer;
import com.gmail.yaroslavlancelot.eafall.game.resources.ResourceFactory;
import com.gmail.yaroslavlancelot.eafall.game.sandbox.activity.SandboxActivity;

/**
 * Intent to correctly start the {@link SandboxActivity}
 *
 * @author Yaroslav Havrylovych
 */
public class SandboxIntent extends MissionIntent {
    public static final String FIRST_PLAYER_NAME = StringConstants.FIRST_PLAYER_CONTROL_BEHAVIOUR_TYPE;
    public static final String SECOND_PLAYER_NAME = StringConstants.SECOND_PLAYER_CONTROL_BEHAVIOUR_TYPE;

    public SandboxIntent(Class activityClass) {
        super(activityClass);
        putExtra(ResourceFactory.RESOURCE_LOADER, ResourceFactory.TypeResourceLoader.SANDBOX);
    }


    public SandboxIntent() {
        this(SandboxActivity.class);
        putExtra(StringConstants.FIRST_PLAYER_CONTROL_BEHAVIOUR_TYPE,
                IPlayer.ControlType.USER_CONTROL_ON_SERVER_SIDE.toString());
        putExtra(StringConstants.FIRST_PLAYER_ALLIANCE,
                Mutants.ALLIANCE_NAME);
        putExtra(StringConstants.SECOND_PLAYER_CONTROL_BEHAVIOUR_TYPE,
                IPlayer.ControlType.USER_CONTROL_ON_SERVER_SIDE.toString());
        putExtra(StringConstants.SECOND_PLAYER_ALLIANCE, Mutants.ALLIANCE_NAME);
        putExtra(MISSION_CONFIG, new MissionConfig());
    }
}
