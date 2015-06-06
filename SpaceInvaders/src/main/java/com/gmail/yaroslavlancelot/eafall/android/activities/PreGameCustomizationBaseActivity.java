package com.gmail.yaroslavlancelot.eafall.android.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;

import com.gmail.yaroslavlancelot.eafall.R;
import com.gmail.yaroslavlancelot.eafall.game.alliance.mutants.Mutants;
import com.gmail.yaroslavlancelot.eafall.game.alliance.imperials.Imperials;
import com.gmail.yaroslavlancelot.eafall.game.alliance.rebels.Rebels;
import com.gmail.yaroslavlancelot.eafall.game.constant.StringConstants;
import com.gmail.yaroslavlancelot.eafall.game.team.ITeam;

/**
 * Base class for pre game customization.
 * Extends from the {@link BaseNonGameActivity}
 * and contains common features for all pre game customizations children.
 */
public abstract class PreGameCustomizationBaseActivity extends BaseNonGameActivity {
    protected Button mStartTheGame;
    protected Spinner mAlliance1;;
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
     * @param team1                first team control type
     * @param team2                second team control type
     * @return initialized click listener or null if activityToStartClass is null
     */
    protected View.OnClickListener getStartButtonOnClick(final Class activityToStartClass,
                                                         final ITeam.ControlType team1,
                                                         final ITeam.ControlType team2) {
        if (activityToStartClass == null) {
            return null;
        }
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent singleGameIntent = new Intent(PreGameCustomizationBaseActivity.this, activityToStartClass);

                singleGameIntent.
                        putExtra(StringConstants.FIRST_TEAM_CONTROL_BEHAVIOUR_TYPE, team1.toString()).
                        putExtra(StringConstants.FIRST_TEAM_ALLIANCE, getAllianceName(mAlliance1)).
                        putExtra(StringConstants.SECOND_TEAM_CONTROL_BEHAVIOUR_TYPE, team2.toString()).
                        putExtra(StringConstants.SECOND_TEAM_ALLIANCE, getAllianceName(mAlliance2));
                ;
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
}
