package com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.building.buildings;

import com.gmail.yaroslavlancelot.eafall.game.constant.Sizes;
import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.building.Building;
import com.gmail.yaroslavlancelot.eafall.game.team.TeamControlBehaviourType;
import com.gmail.yaroslavlancelot.eafall.game.eventbus.unit.CreateStationaryUnitEvent;
import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.building.dummy.DefenceBuildingDummy;
import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.staticobject.PlanetStaticObject;
import com.gmail.yaroslavlancelot.eafall.game.team.ITeam;
import com.gmail.yaroslavlancelot.eafall.game.team.TeamsHolder;

import org.andengine.opengl.vbo.VertexBufferObjectManager;

import de.greenrobot.event.EventBus;

/** building which creates orbital stations */
public class DefenceBuilding extends Building {
    /** amount of orbital stations produced by this building */
    public static final int ORBITAL_STATIONS_AMOUNT = 3;
    /** abscissa of the game-field center point */
    private static final float sGameFieldCenterX = Sizes.GAME_FIELD_WIDTH / 2;
    private static final float sPlanetCenterOrbitalStationDistance =
            (float) (Sizes.PLANET_DIAMETER * 1.5);
    /** Triangle leg with hypotenuse equivalent to planet diameter */
    private static final float sLeg = (float) 0.7071 * sPlanetCenterOrbitalStationDistance;
    /**
     * Each orbital station coordinates assuming that planet center is [x = 0,y = 0].
     * Ordinate is increasing with going bottom.
     */
    public static final float[][] sOrbitalStationsPositions = new float[][]{
            new float[]{sLeg, -sLeg},
            new float[]{sPlanetCenterOrbitalStationDistance, 0},
            new float[]{sLeg, sLeg}
    };
    /** building dummy link */
    private DefenceBuildingDummy mDefenceBuildingDummy;

    public DefenceBuilding(final DefenceBuildingDummy dummy, VertexBufferObjectManager objectManager, String teamName) {
        super(dummy, objectManager, teamName);
        mDefenceBuildingDummy = dummy;
    }

    @Override
    public boolean upgradeBuilding() {
        throw new UnsupportedOperationException("trying to upgrade special building (no upgrades)");
    }

    @Override
    public boolean buyBuilding() {
        if (mBuildingsAmount > 0) {
            return false;
        }
        boolean buildingBought = super.buyBuilding();
        ITeam team = TeamsHolder.getTeam(mTeamName);
        boolean isFakePlanet = TeamControlBehaviourType.isClientSide(team.getTeamControlType());
        //building was created
        if (isFakePlanet || buildingBought) {
            if (isFakePlanet) {
                return true;
            }
        }
        //building wasn't created
        if (!buildingBought) {
            return false;
        }
        //create orbital stations
        PlanetStaticObject planet = team.getTeamPlanet();
        float planetX = planet.getX(), planetY = planet.getY();
        float x, y;
        for (int i = 0; i < ORBITAL_STATIONS_AMOUNT; i++) {
            x = sOrbitalStationsPositions[i][0];
            x = planetX + ((planetX < sGameFieldCenterX) ? x : -x);
            y = planetY + sOrbitalStationsPositions[i][1];
            EventBus.getDefault().post(new CreateStationaryUnitEvent(mDefenceBuildingDummy.getBuildingId(),
                    mTeamName, x, y));
        }
        return true;
    }
}
