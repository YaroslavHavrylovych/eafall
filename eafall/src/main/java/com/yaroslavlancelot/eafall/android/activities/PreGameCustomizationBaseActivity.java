package com.yaroslavlancelot.eafall.android.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;

import com.yaroslavlancelot.eafall.R;
import com.yaroslavlancelot.eafall.android.StartableIntent;
import com.yaroslavlancelot.eafall.game.alliance.imperials.Imperials;
import com.yaroslavlancelot.eafall.game.alliance.mutants.Mutants;
import com.yaroslavlancelot.eafall.game.alliance.rebels.Rebels;
import com.yaroslavlancelot.eafall.game.configuration.mission.MissionConfig;
import com.yaroslavlancelot.eafall.game.constant.StringConstants;
import com.yaroslavlancelot.eafall.game.mission.MissionIntent;
import com.yaroslavlancelot.eafall.game.player.IPlayer;

/**
 * Base class for pre game customization.
 * Extends from the {@link BaseNonGameActivity}
 * and contains common features for all pre game customizations children.
 */
public abstract class PreGameCustomizationBaseActivity extends BaseNonGameActivity {
    protected Button mStartTheGame;
    protected Spinner mAlliance1;
    ;
    protected Spinner mAlliance2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pre_game_cutomization);

        mStartTheGame = (Button) findViewById(R.id.start_game);
        mAlliance1 = (Spinner) findViewById(R.id.choose_alliance_1);
        mAlliance2 = (Spinner) findViewById(R.id.choose_alliance_2);
    }

    /**
     * Creates click listener for the game button. Click listener set needed information to
     * intent and start an activity class of which is passed with the params.
     *
     * @param activityToStartClass activity to start class
     * @param player1ControlType   first player control type
     * @param player2ControlType   second player control type
     * @return initialized click listener or null if activityToStartClass is null
     */
    protected View.OnClickListener getStartButtonOnClick(final Class activityToStartClass,
                                                         final IPlayer.ControlType player1ControlType,
                                                         final IPlayer.ControlType player2ControlType) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent singleGameIntent = getSinglePlayerIntent(activityToStartClass,
                        getAllianceName(mAlliance1), getAllianceName(mAlliance2),
                        player1ControlType, player2ControlType);
                startActivity(singleGameIntent);
            }
        };
    }

    /**
     * Return alliance name (as English version in R.string.choose_alliance) based
     * on current selected id in spinner passed in params.
     *
     * @param spinner spinner to search for alliance in
     * @return Selected alliance name in English.
     */
    private String getAllianceName(Spinner spinner) {
        long selectedId = spinner.getSelectedItemId();
        switch ((int) selectedId) {
            default:
            case 0:
                return Imperials.ALLIANCE_NAME;
            case 1:
                return Rebels.ALLIANCE_NAME;
            case 2:
                return Mutants.ALLIANCE_NAME;
        }
    }

    /** @return intent initialized as a game to start */
    public static StartableIntent getSinglePlayerIntent(Class activityToStartClass,
                                                        String alliance1, String alliance2,
                                                        IPlayer.ControlType player1ControlType,
                                                        IPlayer.ControlType player2ControlType) {
        StartableIntent intent = new MissionIntent(activityToStartClass);
        intent.putExtra(MissionIntent.MISSION_CONFIG, new MissionConfig());
        intent.putExtra(StringConstants.FIRST_PLAYER_CONTROL_BEHAVIOUR_TYPE,
                player1ControlType.toString()).
                putExtra(StringConstants.FIRST_PLAYER_ALLIANCE, alliance1).
                putExtra(StringConstants.SECOND_PLAYER_CONTROL_BEHAVIOUR_TYPE,
                        player2ControlType.toString()).
                putExtra(StringConstants.SECOND_PLAYER_ALLIANCE, alliance2);
        return intent;
    }
}
