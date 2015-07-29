package com.gmail.yaroslavlancelot.eafall.game.campaign.intents;

import com.gmail.yaroslavlancelot.eafall.EaFallApplication;
import com.gmail.yaroslavlancelot.eafall.game.campaign.loader.mission.MissionDataLoader;
import com.gmail.yaroslavlancelot.eafall.game.client.thick.single.SinglePlayerGameActivity;
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

    // ===========================================================
    // Fields
    // ===========================================================

    // ===========================================================
    // Constructors
    // ===========================================================
    public MissionIntent(MissionDataLoader missionData) {
        super(EaFallApplication.getContext(), SinglePlayerGameActivity.class);
        putExtra(ResourceFactory.RESOURCE_LOADER, ResourceFactory.TypeResourceLoader.CLIENT);
        putExtra(StringConstants.FIRST_PLAYER_CONTROL_BEHAVIOUR_TYPE, IPlayer.ControlType.USER_CONTROL_ON_CLIENT_SIDE.toString());
        putExtra(StringConstants.FIRST_PLAYER_ALLIANCE, missionData.player_alliance);
        putExtra(StringConstants.SECOND_PLAYER_CONTROL_BEHAVIOUR_TYPE, IPlayer.ControlType.BOT_CONTROL_ON_SERVER_SIDE.toString());
        putExtra(StringConstants.SECOND_PLAYER_ALLIANCE, missionData.opponent_alliance);
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
