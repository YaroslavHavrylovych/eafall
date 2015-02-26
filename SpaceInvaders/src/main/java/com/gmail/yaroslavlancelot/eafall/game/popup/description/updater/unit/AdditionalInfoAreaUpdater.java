package com.gmail.yaroslavlancelot.eafall.game.popup.description.updater.unit;

import com.gmail.yaroslavlancelot.eafall.R;
import com.gmail.yaroslavlancelot.eafall.game.alliance.AllianceHolder;
import com.gmail.yaroslavlancelot.eafall.game.alliance.IAlliance;
import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.equipment.armor.Armor;
import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.equipment.damage.Damage;
import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.building.BuildingId;
import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.building.dummy.BuildingDummy;
import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.building.dummy.CreepBuildingDummy;
import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.building.dummy.DefenceBuildingDummy;
import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.unit.UnitDummy;
import com.gmail.yaroslavlancelot.eafall.game.popup.description.updater.BaseDescriptionAreaUpdater;
import com.gmail.yaroslavlancelot.eafall.game.visual.text.Link;

import org.andengine.entity.scene.Scene;
import org.andengine.entity.shape.Shape;
import org.andengine.entity.text.Text;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import java.util.ArrayList;

/** unit additional area (in general we have attack and defence value here) */
public class AdditionalInfoAreaUpdater extends BaseDescriptionAreaUpdater {
    private Link mAttack;
    private Link mDefence;

    public AdditionalInfoAreaUpdater(VertexBufferObjectManager vertexBufferObjectManager, Scene scene) {
        //attack
        Text text = createDescriptionText(0, R.string.description_attack, vertexBufferObjectManager);
        mAttack = createLink(text.getWidth(), text.getY(), vertexBufferObjectManager);
        //defence
        text = createDescriptionText(1, R.string.description_defence, vertexBufferObjectManager);
        mDefence = createLink(text.getWidth(), text.getY(), vertexBufferObjectManager);
    }

    @Override
    protected void iniDescriptionTextList() {
        mDescriptionTextList = new ArrayList<Text>(4);
    }

    @Override
    public void updateDescription(Shape drawArea, Object objectId, String raceName, String teamName) {
        final BuildingId buildingId = (BuildingId) objectId;
        IAlliance race = AllianceHolder.getInstance().getElement(raceName);
        BuildingDummy buildingDummy = race.getBuildingDummy(buildingId);
        int unitId;
        if (buildingDummy instanceof CreepBuildingDummy) {
            unitId = ((CreepBuildingDummy) buildingDummy).getMovableUnitId(buildingId.getUpgrade());
        } else if (buildingDummy instanceof DefenceBuildingDummy) {
            unitId = ((DefenceBuildingDummy) buildingDummy).getOrbitalStationUnitId();
        } else {
            return;
        }

        attach(drawArea);

        UnitDummy dummy = race.getUnitDummy(unitId);
        Damage damage = dummy.getDamage();
        mAttack.setText(damage.getDamageValue() + " " + damage.getDamageType());
        Armor defence = dummy.getArmor();
        mDefence.setText(defence.getArmorValue() + " " + damage.getDamageType());
    }
}
