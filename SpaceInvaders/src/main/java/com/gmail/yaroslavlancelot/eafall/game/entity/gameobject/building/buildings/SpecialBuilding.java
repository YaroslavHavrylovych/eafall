package com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.building.buildings;

import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.building.Building;
import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.building.dummy.SpecialBuildingDummy;
import com.gmail.yaroslavlancelot.eafall.game.team.TeamsHolder;

import org.andengine.opengl.vbo.VertexBufferObjectManager;

/** building which give specific to each alliance bonus */
public class SpecialBuilding extends Building {
    public SpecialBuilding(final SpecialBuildingDummy dummy, VertexBufferObjectManager objectManager, String teamName) {
        super(dummy, objectManager, teamName);
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
        if (buildingBought) {
            TeamsHolder.getTeam(mTeamName).addBonus(((SpecialBuildingDummy) mDummy).getBonus());
        }
        return buildingBought;
    }
}
