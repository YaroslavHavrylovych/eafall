package com.yaroslavlancelot.eafall.game.campaign.missions;

import android.widget.Toast;

import com.yaroslavlancelot.eafall.R;
import com.yaroslavlancelot.eafall.game.BaseTutorialActivity;
import com.yaroslavlancelot.eafall.game.constant.SizeConstants;
import com.yaroslavlancelot.eafall.game.constant.StringConstants;
import com.yaroslavlancelot.eafall.game.entity.gameobject.building.BuildingId;
import com.yaroslavlancelot.eafall.game.entity.gameobject.building.BuildingType;
import com.yaroslavlancelot.eafall.game.entity.gameobject.unit.offence.OffenceUnit;
import com.yaroslavlancelot.eafall.game.entity.gameobject.unit.offence.path.IUnitPath;
import com.yaroslavlancelot.eafall.game.entity.gameobject.unit.offence.path.implementation.TwoWaysUnitPath;
import com.yaroslavlancelot.eafall.game.events.SharedEvents;
import com.yaroslavlancelot.eafall.game.events.periodic.time.GameTime;
import com.yaroslavlancelot.eafall.game.player.IPlayer;
import com.yaroslavlancelot.eafall.game.player.PlayersHolder;
import com.yaroslavlancelot.eafall.game.scene.scenes.EaFallScene;

import java.util.Random;
import java.util.SortedSet;

/** Ninth mission include waves user need to leave */
public class NinthMissionActivity extends BaseTutorialActivity {
    @Override
    protected void onPopulateWorkingScene(final EaFallScene scene) {
        super.onPopulateWorkingScene(scene);
        String callbackKey = GameTime.GAME_TIMER_TICK_KEY;
        SharedEvents.addCallback(new SharedEvents.DataChangedCallback(callbackKey) {
            @Override
            public void callback(final String key, final Object value) {
                Integer intVal = (Integer) value;
                switch (intVal) {
                    case 280: {
                        defenceWave();
                        break;
                    }
                    case 100: {
                        showInfoToast(R.string.ninth_mission_pre_support);
                        break;
                    }
                    case 60: {
                        wave(5);
                        break;
                    }
                    case 10: {
                        wave(10);
                        break;
                    }
                }
            }
        });
    }

    private IPlayer getBotPlayer() {
        IPlayer player = PlayersHolder.getPlayer(StringConstants.FIRST_PLAYER_CONTROL_BEHAVIOUR_TYPE);
        if (player.getControlType().user()) {
            player = player.getEnemyPlayer();
        }
        return player;
    }

    private void defenceWave() {
        IPlayer player = getBotPlayer();
        BuildingId defenceBuilding = null;
        for (BuildingId buildingId : player.getBuildingsIds()) {
            if (player.getAlliance().getBuildingDummy(buildingId).getBuildingType() == BuildingType.DEFENCE_BUILDING) {
                defenceBuilding = buildingId;
                break;
            }
        }
        if (defenceBuilding == null) {
            throw new IllegalStateException("defence building shouldn't be null");
        }
        player.getPlanet().forceBuildingCreate(defenceBuilding);
        showInfoToast(R.string.ninth_mission_defence_building);
    }

    private void wave(int amountOfUnitPerPath) {
        IPlayer player = getBotPlayer();
        SortedSet<Integer> unitIds = player.getAlliance().getUnitsIds();
        Random random = new Random();
        //top support
        for (int i = 0; i < amountOfUnitPerPath; i++) {
            int id = unitIds.tailSet(random.nextInt(unitIds.size())).first();
            OffenceUnit unit = createMovableUnit(player, id,
                    SizeConstants.GAME_FIELD_WIDTH / 2 + ((i - 2) * SizeConstants.UNIT_SIZE + 5),
                    SizeConstants.GAME_FIELD_HEIGHT * 9 / 10, new TwoWaysUnitPath(false, true));
            IUnitPath path = unit.getUnitPath();
            path.setCurrentPathPoint(path.getTotalPathPoints() - 1);
        }
        //bottom support
        for (int i = 0; i < amountOfUnitPerPath; i++) {
            int id = unitIds.tailSet(random.nextInt(unitIds.size())).first();
            OffenceUnit unit = createMovableUnit(player, id,
                    SizeConstants.GAME_FIELD_WIDTH / 2 + ((i - 2) * SizeConstants.UNIT_SIZE + 5),
                    SizeConstants.GAME_FIELD_HEIGHT / 10, new TwoWaysUnitPath(false, false));
            IUnitPath path = unit.getUnitPath();
            path.setCurrentPathPoint(path.getTotalPathPoints() - 1);
        }
        showInfoToast(R.string.ninth_mission_support);
    }

    private void showInfoToast(final int strId) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(NinthMissionActivity.this, strId, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
