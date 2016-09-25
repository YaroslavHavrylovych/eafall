package com.yaroslavlancelot.eafall.game.mission;

import com.yaroslavlancelot.eafall.EaFallApplication;
import com.yaroslavlancelot.eafall.android.StartableIntent;
import com.yaroslavlancelot.eafall.game.configuration.mission.MissionConfig;
import com.yaroslavlancelot.eafall.game.constant.StringConstants;
import com.yaroslavlancelot.eafall.game.player.IPlayer;
import com.yaroslavlancelot.eafall.game.resources.ResourceFactory;

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
        this(activityClass, ResourceFactory.TypeResourceLoader.CLIENT);
    }

    public MissionIntent(Class activityClass, ResourceFactory.TypeResourceLoader resourceLoaderType) {
        super(EaFallApplication.getContext(), activityClass);
        putExtra(ResourceFactory.RESOURCE_LOADER, resourceLoaderType);
    }

    /**
     * Initialized with mission data loader
     *
     * @param activityClass custom class to start mission
     * @param missionData   mission data
     */
    public MissionIntent(Class activityClass, MissionDetailsLoader missionData) {
        this(activityClass);
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
