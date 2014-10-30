package com.gmail.yaroslavlancelot.spaceinvaders.popups.objectdescription.updater.unit;

import com.gmail.yaroslavlancelot.spaceinvaders.R;
import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.equipment.armor.Armor;
import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.equipment.weapons.Damage;
import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.objects.staticobjects.BuildingId;
import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.objects.units.UnitDummy;
import com.gmail.yaroslavlancelot.spaceinvaders.popups.objectdescription.updater.BaseDescriptionAreaUpdater;
import com.gmail.yaroslavlancelot.spaceinvaders.races.IRace;
import com.gmail.yaroslavlancelot.spaceinvaders.races.RacesHolder;
import com.gmail.yaroslavlancelot.spaceinvaders.visualelements.text.Link;

import org.andengine.entity.scene.Scene;
import org.andengine.entity.shape.RectangularShape;
import org.andengine.entity.text.Text;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import java.util.ArrayList;

/**  */
public class UnitAdditionalAreaUpdater extends BaseDescriptionAreaUpdater {
    private Link mAttack;
    private Link mDefence;

    public UnitAdditionalAreaUpdater(VertexBufferObjectManager vertexBufferObjectManager, Scene scene) {
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
        IRace race = RacesHolder.getInstance().getElement(raceName);
        int unitId = race.getBuildingDummy(buildingId).getUnitId(buildingId.getUpgrade());

        attach(drawArea);

        UnitDummy dummy = race.getUnitDummy(unitId);
        Damage damage = dummy.getDamage();
        mAttack.setText(damage.getDamageValue() + " " + damage.getDamageType());
        Armor defence = dummy.getUnitArmor();
        mDefence.setText(defence.getArmorValue() + " " + damage.getDamageType());
    }
}
