package com.gmail.yaroslavlancelot.spaceinvaders.popups.objectdescription.updater.unit;

import com.gmail.yaroslavlancelot.spaceinvaders.R;
import com.gmail.yaroslavlancelot.spaceinvaders.alliances.AllianceHolder;
import com.gmail.yaroslavlancelot.spaceinvaders.alliances.IAlliance;
import com.gmail.yaroslavlancelot.spaceinvaders.objects.equipment.armor.Armor;
import com.gmail.yaroslavlancelot.spaceinvaders.objects.equipment.weapons.Damage;
import com.gmail.yaroslavlancelot.spaceinvaders.objects.objects.buildings.BuildingId;
import com.gmail.yaroslavlancelot.spaceinvaders.objects.objects.dummies.CreepBuildingDummy;
import com.gmail.yaroslavlancelot.spaceinvaders.objects.objects.units.UnitDummy;
import com.gmail.yaroslavlancelot.spaceinvaders.objects.objects.units.dynamic.MovableUnitDummy;
import com.gmail.yaroslavlancelot.spaceinvaders.popups.objectdescription.updater.BaseDescriptionAreaUpdater;
import com.gmail.yaroslavlancelot.spaceinvaders.visualelements.text.Link;

import org.andengine.entity.scene.Scene;
import org.andengine.entity.shape.RectangularShape;
import org.andengine.entity.text.Text;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import java.util.ArrayList;

/**  */
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
    public void updateDescription(RectangularShape drawArea, Object objectId, String raceName, String teamName) {
        final BuildingId buildingId = (BuildingId) objectId;
        IAlliance race = AllianceHolder.getInstance().getElement(raceName);
        int unitId = ((CreepBuildingDummy) race.getBuildingDummy(buildingId)).getUnitId(buildingId.getUpgrade());

        attach(drawArea);

        UnitDummy dummy = race.getUnitDummy(unitId);
        Damage damage = dummy.getDamage();
        mAttack.setText(damage.getDamageValue() + " " + damage.getDamageType());
        Armor defence = dummy.getArmor();
        mDefence.setText(defence.getArmorValue() + " " + damage.getDamageType());
    }
}
