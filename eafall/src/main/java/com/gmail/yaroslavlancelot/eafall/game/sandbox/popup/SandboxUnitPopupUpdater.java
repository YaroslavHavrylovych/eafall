package com.gmail.yaroslavlancelot.eafall.game.sandbox.popup;

import com.gmail.yaroslavlancelot.eafall.R;
import com.gmail.yaroslavlancelot.eafall.game.alliance.IAlliance;
import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.building.BuildingId;
import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.building.dummy.BuildingDummy;
import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.building.dummy.DefenceBuildingDummy;
import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.building.dummy.SpecialBuildingDummy;
import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.building.dummy.WealthBuildingDummy;
import com.gmail.yaroslavlancelot.eafall.game.events.SharedEvents;
import com.gmail.yaroslavlancelot.eafall.game.events.aperiodic.ingame.description.UnitByBuildingDescriptionShowEvent;
import com.gmail.yaroslavlancelot.eafall.game.player.IPlayer;
import com.gmail.yaroslavlancelot.eafall.game.player.PlayersHolder;
import com.gmail.yaroslavlancelot.eafall.game.popup.rolling.description.updater.unit.BaseUnitPopupUpdater;
import com.gmail.yaroslavlancelot.eafall.general.locale.LocaleImpl;

import org.andengine.entity.scene.Scene;
import org.andengine.entity.shape.Shape;
import org.andengine.entity.sprite.ButtonSprite;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import de.greenrobot.event.EventBus;

/**
 * Custom unit popup updater used in the sandbox. Doesn't have building image,
 * button to return to the unit building. Has arrows to switch between units to build.
 *
 * @author Yaroslav Havrylovych
 */
public class SandboxUnitPopupUpdater extends BaseUnitPopupUpdater {
    // ===========================================================
    // Constants
    // ===========================================================

    // ===========================================================
    // Fields
    // ===========================================================

    // ===========================================================
    // Constructors
    // ===========================================================
    public SandboxUnitPopupUpdater(final VertexBufferObjectManager vertexBufferObjectManager, final Scene scene) {
        super(vertexBufferObjectManager, scene);
    }

    // ===========================================================
    // Getter & Setter
    // ===========================================================

    // ===========================================================
    // Methods for/from SuperClass/Interfaces
    // ===========================================================

    @Override
    public void updateAdditionInfo(final Shape drawArea, final Object objectId, final String allianceName, final String playerName) {
    }

    @Override
    protected void updateBaseButton(Shape drawArea, final Object objectId, final String playerName) {
        //build button
        final BuildingId buildingId = (BuildingId) objectId;
        mBaseButton.setText(LocaleImpl.getInstance().getStringById(R.string.sandbox_select));
        mBaseButton.setPosition(mBaseButton.getWidth() / 2, mBaseButton.getHeight() / 2);
        drawArea.attachChild(mBaseButton);
        mBaseButton.setOnClickListener(new ButtonSprite.OnClickListener() {
            @Override
            public void onClick(ButtonSprite pButtonSprite, float pTouchAreaLocalX, float pTouchAreaLocalY) {
                SharedEvents.valueChanged(SandboxDescriptionPopup.SANDBOX_UNIT_CHANGED_KEY,
                        buildingId.getId() + buildingId.getUpgrade());
            }
        });
    }

    //TODO too complex and can cause problems later, sorry, demo in 2 days
    @Override
    public void updateHeaderButtons(final ButtonSprite leftArrow, final ButtonSprite rightArrow, final Object objectId, final String playerName) {
        super.updateHeaderButtons(leftArrow, rightArrow, objectId, playerName);
        BuildingId buildingId = (BuildingId) objectId;
        final IPlayer player = PlayersHolder.getPlayer(playerName);
        final int id = buildingId.getId();
        //searching for arrows to the upgrade
        boolean searchForLeftArrow = true;
        if (buildingId.getUpgrade() > 0) {
            initArrow(leftArrow, BuildingId.makeId(buildingId.getId(), buildingId.getUpgrade() - 1),
                    playerName);
            searchForLeftArrow = false;
        }
        boolean searchForRightArrow = true;
        IAlliance alliance = PlayersHolder.getPlayer(playerName).getAlliance();
        if (alliance.isUpgradeAvailable(buildingId)) {
            initArrow(rightArrow, buildingId.getNextUpgrade(), playerName);
            searchForRightArrow = false;
        }
        //searching for arrows to buildings without upgrades
        int before = -1;
        int after = Integer.MAX_VALUE;
        for (Integer ind : player.getAlliance().getBuildingsIds()) {
            if (searchForLeftArrow && id > ind && before < ind) {
                before = ind;
            }
            if (searchForRightArrow
                    && id < ind && after > ind
                    && ind != WealthBuildingDummy.BUILDING_ID
                    && ind != SpecialBuildingDummy.BUILDING_ID) {
                after = ind;
            }
        }
        if (before != -1) {
            //last upgrade out of the previous building
            BuildingDummy buildingDummy = alliance.getBuildingDummy(BuildingId.makeId(before, 0));
            BuildingId newBuildingId = BuildingId.makeId(
                    buildingDummy.getBuildingId(), buildingDummy.getUpgrades() - 1);
            initArrow(leftArrow, newBuildingId, playerName);
        }
        if (after != Integer.MAX_VALUE) {
            //first upgrade of the next building
            BuildingId newBuildingId = BuildingId.makeId(after, 0);

            if (!(alliance.getBuildingDummy(newBuildingId) instanceof DefenceBuildingDummy)) {
                initArrow(rightArrow, newBuildingId, playerName);
            }
        }
    }

    // ===========================================================
    // Methods
    // ===========================================================
    private void initArrow(ButtonSprite arrowButton, final BuildingId buildingId, final String playerName) {
        arrowButton.setVisible(true);
        arrowButton.setOnClickListener(new ButtonSprite.OnClickListener() {
            @Override
            public void onClick(final ButtonSprite pButtonSprite, final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
                EventBus.getDefault().post(new UnitByBuildingDescriptionShowEvent(buildingId, playerName));
            }
        });
    }


    // ===========================================================
    // Inner and Anonymous Classes
    // ===========================================================
}
