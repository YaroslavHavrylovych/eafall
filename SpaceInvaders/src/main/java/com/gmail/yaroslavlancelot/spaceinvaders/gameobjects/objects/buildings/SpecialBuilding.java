package com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.objects.buildings;

import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.objects.dummies.SpecialBuildingDummy;
import com.gmail.yaroslavlancelot.spaceinvaders.teams.TeamsHolder;

import org.andengine.opengl.vbo.VertexBufferObjectManager;

/** building which give specific to each race bonus */
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
            TeamsHolder.getTeam(mTeamName).addTeamBonus(((SpecialBuildingDummy) mDummy).getBonus());
        }
        return buildingBought;
    }
}
