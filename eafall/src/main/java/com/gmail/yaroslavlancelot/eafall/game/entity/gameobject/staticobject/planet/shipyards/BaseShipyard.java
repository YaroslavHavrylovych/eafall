package com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.staticobject.planet.shipyards;

import com.gmail.yaroslavlancelot.eafall.game.client.IUnitCreator;
import com.gmail.yaroslavlancelot.eafall.game.constant.SizeConstants;
import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.building.buildings.IUnitBuilding;
import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.unit.offence.path.PathHelper;
import com.gmail.yaroslavlancelot.eafall.game.player.IPlayer;
import com.gmail.yaroslavlancelot.eafall.game.player.PlayersHolder;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Base shipyard functionality as:
 * <p/>
 * <ul>
 * <il>port positioning initialization</il>
 * <il>check available units in buildings</il>
 * <il>check available ports (which are on the cooldown)</il>
 * <il>check units limit</il>
 * <il>spawn offence unit</il>
 * </ul>
 *
 * @author Yaroslav Havrylovych
 */
public abstract class BaseShipyard implements IShipyard {
    // ===========================================================
    // Constants
    // ===========================================================
    /** port cooldown between spawn */
    public static final int COOLDOWN = 3;
    /** ports amount. in genera it's hardcoded and not for change at all */
    protected static final int PORTS = 5;
    /** units limit */
    private final int mLimit;

    // ===========================================================
    // Fields
    // ===========================================================
    /** currently available ports */
    protected Set<Integer> mAvailablePorts = new HashSet<>(Arrays.asList(0, 1, 2, 3, 4));
    /** Ports on cooldown. Array Index == Port Index. Value == cooldown time. */
    protected int[] mCooldownPorts = new int[PORTS];
    /** Ports positions on the scene. Array Index == Port Index. Value == coordinates. */
    protected Position[] mPortsPositions = new Position[PORTS];
    /** player which this shipyard belongs to */
    protected IPlayer mPlayer;
    /** used to spawn offence units */
    private IUnitCreator mCreator;

    // ===========================================================
    // Constructors
    // ===========================================================
    public BaseShipyard(int x, int y, String playerName, IUnitCreator creator) {
        IPlayer player = PlayersHolder.getPlayer(playerName);
        mLimit = player.getUnitsLimit();
        mPlayer = player;
        mCreator = creator;
        initPortsPositions(x, y);
    }

    // ===========================================================
    // Getter & Setter
    // ===========================================================

    // ===========================================================
    // Methods for/from SuperClass/Interfaces
    // ===========================================================
    @Override
    public void update(List<IUnitBuilding> unitBuildings) {
        for (int i = 0; i < unitBuildings.size(); i++) {
            unitBuildings.get(i).tickUpdate();
        }
        updateCooldown();
        if (checkLimit() && checkAvailableUnits(unitBuildings) && checkPorts()) {
            checkedUpdate(unitBuildings);
        }
    }

    // ===========================================================
    // Methods
    // ===========================================================

    /**
     * Used to update ports and spawn units. Will be triggered
     * once per second but only if units limit isn't reached and
     * at least one port available and at least one building has units
     */
    protected abstract void checkedUpdate(List<IUnitBuilding> unitBuildings);

    /**
     * check available units in given buildings
     *
     * @param unitBuildings unit buildings to check
     * @return true if at least one building has units
     */
    protected boolean checkAvailableUnits(List<IUnitBuilding> unitBuildings) {
        for (int i = 0; i < unitBuildings.size(); i++) {
            if (unitBuildings.get(i).getAvailableUnits() > 0) {
                return true;
            }
        }
        return false;
    }

    /**
     * approximate points positioning. angle changed by 23 from (x0,y0) 46
     * <p/>
     * ............(x0,.y0)
     * ......................
     * ...................(x1,y1)
     * ....(x,y).................(x2,.y2)
     * ......................
     * ...................(x3,y3)
     * ............(x4,.y4)
     */
    protected void initPortsPositions(int x, int y) {
        boolean left = PathHelper.isLeftSide(x);
        int c = SizeConstants.SHIPYARD_SPAWN_LENGTH;
        int x0 = (int) (c * 0.71); //sin 46
        int y0 = (int) (c * 0.69); //cos 46
        int x1 = (int) (c * 0.39); //sin 23
        int y1 = (int) (c * 0.92); //cos 23

        if (left) {
            mPortsPositions[0] = new Position(x + x0, y + y0);
            mPortsPositions[1] = new Position(x + x1, y + y1);
            mPortsPositions[2] = new Position(x + c, y);
            mPortsPositions[3] = new Position(x + x1, y - y1);
            mPortsPositions[4] = new Position(x + x0, y - y0);
        } else {
            mPortsPositions[0] = new Position(x - x0, y + y0);
            mPortsPositions[1] = new Position(x - x1, y + y1);
            mPortsPositions[2] = new Position(x - c, y);
            mPortsPositions[3] = new Position(x - x1, y - y1);
            mPortsPositions[4] = new Position(x - x0, y - y0);
        }
    }

    /**
     * check player units limit
     *
     * @return true if player has at least one place for a unit
     */
    protected boolean checkLimit() {
        return mPlayer.getUnitsAmount() < mLimit;
    }

    /**
     * reduce each cooldown value by 1 (from mCooldownPorts array)
     */
    protected void updateCooldown() {
        for (int i = 0; i < mCooldownPorts.length; i++) {
            if (--mCooldownPorts[i] < 0 && !mAvailablePorts.contains(i)) {
                mAvailablePorts.add(i);
            }
        }
    }

    /**
     * check for available ports for spawn
     *
     * @return true if at least one port is available
     */
    protected boolean checkPorts() {
        for (int mCooldownPort : mCooldownPorts) {
            if (mCooldownPort <= 0) {
                return true;
            }
        }
        return false;
    }

    /**
     * spawn unit from unitBuilding at particular port
     *
     * @param port          where the unit will be spawned
     * @param unitBuilding used to get unit key
     */
    protected void spawn(int port, IUnitBuilding unitBuilding) {
        int unitKey = unitBuilding.getUnit();
        Position position = mPortsPositions[port];
        mCreator.createMovableUnit(mPlayer, unitKey, position.mX, position.mY, unitBuilding.isTopPath());
        mAvailablePorts.remove(port);
        mCooldownPorts[port] = COOLDOWN;
    }

    // ===========================================================
    // Inner and Anonymous Classes
    // ===========================================================

    /** used to store spawn point position */
    class Position {
        final int mX;
        final int mY;

        public Position(int x, int y) {
            mX = x;
            mY = y;
        }
    }
}
