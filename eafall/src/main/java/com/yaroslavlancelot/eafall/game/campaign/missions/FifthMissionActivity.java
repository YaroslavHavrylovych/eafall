package com.yaroslavlancelot.eafall.game.campaign.missions;

import android.widget.Toast;

import com.yaroslavlancelot.eafall.R;
import com.yaroslavlancelot.eafall.game.BaseTutorialActivity;
import com.yaroslavlancelot.eafall.game.constant.SizeConstants;
import com.yaroslavlancelot.eafall.game.constant.StringConstants;
import com.yaroslavlancelot.eafall.game.entity.gameobject.unit.offence.OffenceUnit;
import com.yaroslavlancelot.eafall.game.entity.gameobject.unit.offence.path.IUnitPath;
import com.yaroslavlancelot.eafall.game.entity.gameobject.unit.offence.path.implementation.SingleWayThoughCenterUnitPath;
import com.yaroslavlancelot.eafall.game.events.SharedEvents;
import com.yaroslavlancelot.eafall.game.events.periodic.time.GameTime;
import com.yaroslavlancelot.eafall.game.player.IPlayer;
import com.yaroslavlancelot.eafall.game.player.PlayersHolder;
import com.yaroslavlancelot.eafall.game.scene.scenes.EaFallScene;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/** Fifth mission include waves user need to leave */
public class FifthMissionActivity extends BaseTutorialActivity {
    @Override
    protected void onPopulateWorkingScene(final EaFallScene scene) {
        super.onPopulateWorkingScene(scene);
        String callbackKey = GameTime.GAME_TIMER_TICK_KEY;
        SharedEvents.addCallback(new SharedEvents.DataChangedCallback(callbackKey) {
            @Override
            public void callback(final String key, final Object value) {
                Integer intVal = (Integer) value;
                switch (intVal) {
                    case 180: {
                        showInfoToast(R.string.ninth_mission_pre_support);
                        break;
                    }
                    case 170: {
                        wave(3);
                        break;
                    }
                    case 30: {
                        wave(5);
                        break;
                    }
                }
            }
        });
    }

    private void wave(int amountOfUnitPerPath) {
        IPlayer player = getBotPlayer();
        List<Integer> unitsIds = new ArrayList<>(player.getAlliance().getMovableUnitsIds());
        unitsIds.remove(unitsIds.size() - 1);// removing defence unit
        Random random = new Random();
        //top support
        for (int i = 0; i < amountOfUnitPerPath; i++) {
            int id = unitsIds.get(random.nextInt(unitsIds.size()));
            int y = SizeConstants.GAME_FIELD_HEIGHT * 9 / 10 + i / 10 * SizeConstants.UNIT_SIZE;
            int x = SizeConstants.GAME_FIELD_WIDTH / 2
                    + (((i % 10) - 2) * SizeConstants.UNIT_SIZE + 5);
            OffenceUnit unit = createMovableUnit(player, id, x, y,
                    new SingleWayThoughCenterUnitPath(false));
            IUnitPath path = unit.getUnitPath();
            path.setCurrentPathPoint(path.getTotalPathPoints() / 2);
        }
        //bottom support
        for (int i = 0; i < amountOfUnitPerPath; i++) {
            int id = unitsIds.get(random.nextInt(unitsIds.size()));
            int x = SizeConstants.GAME_FIELD_WIDTH / 2 +
                    (((i % 10) - 2) * SizeConstants.UNIT_SIZE + 5);
            int y  = SizeConstants.GAME_FIELD_HEIGHT / 10 + i / 10 * SizeConstants.UNIT_SIZE;
            OffenceUnit unit = createMovableUnit(player, id, x, y,
                    new SingleWayThoughCenterUnitPath(false));
            IUnitPath path = unit.getUnitPath();
            path.setCurrentPathPoint(path.getTotalPathPoints() / 2);
        }
        showInfoToast(R.string.ninth_mission_support);
    }

    private IPlayer getBotPlayer() {
        IPlayer player = PlayersHolder.getPlayer(StringConstants.FIRST_PLAYER_CONTROL_BEHAVIOUR_TYPE);
        if (player.getControlType().user()) {
            player = player.getEnemyPlayer();
        }
        return player;
    }

    private void showInfoToast(final int strId) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(FifthMissionActivity.this, strId, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
