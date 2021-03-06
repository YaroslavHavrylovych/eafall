package com.yaroslavlancelot.eafall.game.entity.gameobject.building.buildings;

import com.yaroslavlancelot.eafall.game.entity.gameobject.building.Building;
import com.yaroslavlancelot.eafall.game.entity.gameobject.building.dummy.SpecialBuildingDummy;
import com.yaroslavlancelot.eafall.game.player.PlayersHolder;

import org.andengine.entity.IEntity;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

/** building which give specific to each alliance bonus */
public class SpecialBuilding extends Building {
    public SpecialBuilding(final SpecialBuildingDummy dummy, VertexBufferObjectManager objectManager, String playerName) {
        super(dummy, objectManager, playerName);
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
            PlayersHolder.getPlayer(mPlayerName).addBonus(((SpecialBuildingDummy) mDummy).getBonus());
        }
        return buildingBought;
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
