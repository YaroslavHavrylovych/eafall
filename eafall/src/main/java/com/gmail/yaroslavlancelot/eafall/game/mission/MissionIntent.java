package com.gmail.yaroslavlancelot.eafall.game.mission;

import com.gmail.yaroslavlancelot.eafall.EaFallApplication;
import com.gmail.yaroslavlancelot.eafall.android.StartableIntent;
import com.gmail.yaroslavlancelot.eafall.game.client.thick.single.SinglePlayerGameActivity;
import com.gmail.yaroslavlancelot.eafall.game.configuration.mission.MissionConfig;
import com.gmail.yaroslavlancelot.eafall.game.constant.StringConstants;
import com.gmail.yaroslavlancelot.eafall.game.player.IPlayer;
import com.gmail.yaroslavlancelot.eafall.game.resources.ResourceFactory;

/**
 * Mission start {@link android.content.Intent}. If you want to add some custom stuff
 * you can do it here. Start particular activities for unique missions and etc.
 *
 * @author Yaroslav Havrylovych
 */
public class MissionIntent extends StartableIntent {
    // ===========================================================
    // Constants
    // ===========================================================
    public static final String MISSION_CONFIG = "mission_config";

    // ===========================================================
    // Fields
    // ===========================================================

    // ===========================================================
    // Constructors
    // ===========================================================
    public MissionIntent(Class activityClass) {
        super(EaFallApplication.getContext(), activityClass);
        putExtra(ResourceFactory.RESOURCE_LOADER, ResourceFactory.TypeResourceLoader.CLIENT);
    }

    /**
     * used for campaigns missions
     *
     * @param missionData mission data
     */
    public MissionIntent(MissionDataLoader missionData) {
        this(SinglePlayerGameActivity.class);
        putExtra(StringConstants.FIRST_PLAYER_CONTROL_BEHAVIOUR_TYPE,
                IPlayer.ControlType.USER_CONTROL_ON_SERVER_SIDE.toString());
        putExtra(StringConstants.FIRST_PLAYER_ALLIANCE,
                missionData.player_alliance);
        putExtra(StringConstants.SECOND_PLAYER_CONTROL_BEHAVIOUR_TYPE,
                IPlayer.ControlType.BOT_CONTROL_ON_SERVER_SIDE.toString());
        putExtra(StringConstants.SECOND_PLAYER_ALLIANCE, missionData.opponent_alliance);
        putExtra(MISSION_CONFIG, new MissionConfig(missionData));
    }

    // ===========================================================
    // Getter & Setter
    // ===========================================================

    // ===========================================================
    // Methods for/from SuperClass/Interfaces
    // ===========================================================

    // ===========================================================
    // Methods
    // ===========================================================

    // ===========================================================
    // Inner and Anonymous Classes
    // ===========================================================
}
