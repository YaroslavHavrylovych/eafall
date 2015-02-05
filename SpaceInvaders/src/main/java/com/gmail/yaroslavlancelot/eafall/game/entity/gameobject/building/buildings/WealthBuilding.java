package com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.building.buildings;

import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.building.Building;
import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.building.dummy.WealthBuildingDummy;

import org.andengine.opengl.vbo.VertexBufferObjectManager;

/** building for increasing existing wealth in percents */
public class WealthBuilding extends Building {
    public WealthBuilding(final WealthBuildingDummy dummy, VertexBufferObjectManager objectManager, String teamName) {
        super(dummy, objectManager, teamName);
    }

    @Override
    public boolean upgradeBuilding() {
        throw new UnsupportedOperationException("trying to upgrade wealth building (no upgrades)");
    }

    @Override
    public boolean buyBuilding() {
        boolean result = super.buyBuilding();
        WealthBuildingDummy wealthBuildingDummy = ((WealthBuildingDummy) mDummy);
        if (getAmount() <= 0) {
            setIncome(0);
        } else if (getAmount() == 1) {
            setIncome(wealthBuildingDummy.getFirstBuildingIncome());
        } else {
            setIncome(getIncome() + wealthBuildingDummy.getNextBuildingsIncome());
        }
        return result;
    }
}
