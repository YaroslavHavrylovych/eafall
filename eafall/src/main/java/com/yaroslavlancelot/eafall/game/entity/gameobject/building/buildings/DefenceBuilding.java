package com.yaroslavlancelot.eafall.game.entity.gameobject.building.buildings;

import com.yaroslavlancelot.eafall.game.constant.SizeConstants;
import com.yaroslavlancelot.eafall.game.entity.gameobject.building.Building;
import com.yaroslavlancelot.eafall.game.entity.gameobject.building.dummy.DefenceBuildingDummy;
import com.yaroslavlancelot.eafall.game.entity.gameobject.staticobject.planet.PlanetStaticObject;
import com.yaroslavlancelot.eafall.game.events.aperiodic.ingame.unit.CreateDefenceUnitEvent;
import com.yaroslavlancelot.eafall.game.player.IPlayer;
import com.yaroslavlancelot.eafall.game.player.PlayersHolder;

import org.andengine.entity.IEntity;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import de.greenrobot.event.EventBus;

/** building which creates orbital stations */
public class DefenceBuilding extends Building {
    /** amount of orbital stations produced by this building */
    public static final int ORBITAL_STATIONS_AMOUNT = 3;
    /** abscissa of the game-field center point */
    private static final float sGameFieldCenterX = SizeConstants.GAME_FIELD_WIDTH / 2;
    private static final float sPlanetCenterOrbitalStationDistance =
            (float) (SizeConstants.PLANET_DIAMETER * 1.5 * 0.7071);
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

    public DefenceBuilding(final DefenceBuildingDummy dummy, VertexBufferObjectManager objectManager, String playerName) {
        super(dummy, objectManager, playerName);
        mDefenceBuildingDummy = dummy;
    }

    @Override
    public boolean upgradeBuilding() {
        throw new UnsupportedOperationException("trying to upgrade special building (no upgrades)");
    }

    /** HACK FOR CAMPAIGN ONLY */
    public boolean forceBuilding() {
        return buyBuilding(true);
    }

    /**
     * Buying the building for the player.
     * <br/>
     * Operation can fail
     * if not enough amount of money or building limit exceed.
     * <br/>
     * Operation can be forced, in this case buying operation would
     * perform without money, and can't fail if not enough of money.
     *
     * @param force true if it's force operation (read above).
     * @return true if building bought and false in other case.
     */
    protected boolean buyBuilding(boolean force) {
        if (mBuildingsAmount > 0) {
            return false;
        }
        boolean buildingBought = super.buyBuilding(force);
        IPlayer player = PlayersHolder.getPlayer(mPlayerName);
        boolean isFakePlanet = player.getControlType().clientSide();
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
        PlanetStaticObject planet = player.getPlanet();
        float planetX = planet.getX(), planetY = planet.getY();
        float x, y;
        for (int i = 0; i < ORBITAL_STATIONS_AMOUNT; i++) {
            x = sOrbitalStationsPositions[i][0];
            x = planetX + ((planetX < sGameFieldCenterX) ? x : -x);
            y = planetY + sOrbitalStationsPositions[i][1];
            EventBus.getDefault().post(new CreateDefenceUnitEvent(mDefenceBuildingDummy.getBuildingId(),
                    mPlayerName, x, y));
        }
        return true;
    }

    @Override
    public boolean buyBuilding() {
        return buyBuilding(false);
    }

    @Override
    public void registerTouch(final IEntity entity) {
        throw new UnsupportedOperationException("trying to register touch for building where it's prohibited");
    }

    @Override
    public void unregisterTouch(final IEntity entity) {
        throw new UnsupportedOperationException("trying to unregister touch for building where it's prohibited");
    }
}
