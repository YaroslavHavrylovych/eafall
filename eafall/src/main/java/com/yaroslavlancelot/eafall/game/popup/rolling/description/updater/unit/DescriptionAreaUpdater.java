package com.yaroslavlancelot.eafall.game.popup.rolling.description.updater.unit;

import com.yaroslavlancelot.eafall.R;
import com.yaroslavlancelot.eafall.game.alliance.AllianceHolder;
import com.yaroslavlancelot.eafall.game.alliance.IAlliance;
import com.yaroslavlancelot.eafall.game.entity.gameobject.building.BuildingId;
import com.yaroslavlancelot.eafall.game.entity.gameobject.building.dummy.BuildingDummy;
import com.yaroslavlancelot.eafall.game.entity.gameobject.building.dummy.UnitBuildingDummy;
import com.yaroslavlancelot.eafall.game.entity.gameobject.equipment.armor.Armor;
import com.yaroslavlancelot.eafall.game.entity.gameobject.equipment.damage.Damage;
import com.yaroslavlancelot.eafall.game.entity.gameobject.unit.UnitDummy;
import com.yaroslavlancelot.eafall.game.events.aperiodic.ingame.ShowToastEvent;
import com.yaroslavlancelot.eafall.game.popup.rolling.description.updater.BaseDescriptionAreaUpdater;
import com.yaroslavlancelot.eafall.game.touch.TouchHelper;
import com.yaroslavlancelot.eafall.game.visual.text.DescriptionText;
import com.yaroslavlancelot.eafall.game.visual.text.Link;

import org.andengine.entity.scene.Scene;
import org.andengine.entity.shape.Shape;
import org.andengine.entity.text.Text;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import java.util.ArrayList;

import de.greenrobot.event.EventBus;

/** Update unit description (and only description area) on given area. */
public class DescriptionAreaUpdater extends BaseDescriptionAreaUpdater {
    /* values changed with each #updateDescription call */
    private DescriptionText mUnitHealth;
    private Link mDamageValue;
    private Link mArmorValue;
    private DescriptionText mFireRate;

    public DescriptionAreaUpdater(VertexBufferObjectManager vboManager, Scene scene) {
        //health
        Text text = createDescriptionText(0, R.string.description_health, vboManager);
        mUnitHealth = descriptionText(text, vboManager);
        //damage
        text = createDescriptionText(1, R.string.description_damage, vboManager);
        mDamageValue = createLink(text.getWidth(), text.getY(), vboManager);
        //armor
        text = createDescriptionText(2, R.string.description_armor, vboManager);
        mArmorValue = createLink(text.getWidth(), text.getY(), vboManager);
        //fire-rate
        text = createDescriptionText(3, R.string.description_reload_time, vboManager);
        mFireRate = descriptionText(text, vboManager);
    }

    @Override
    public void updateDescription(Shape drawArea, Object objectId, String allianceName, final String playerName) {
        final BuildingId buildingId = (BuildingId) objectId;
        IAlliance alliance = AllianceHolder.getInstance().getElement(allianceName);
        BuildingDummy buildingDummy = alliance.getBuildingDummy(buildingId);
        int unitId = ((UnitBuildingDummy) buildingDummy).getUnitId(buildingId.getUpgrade());
        attach(drawArea);
        UnitDummy unitDummy = alliance.getUnitDummy(unitId);
        //health
        mUnitHealth.setText("" + unitDummy.getHealth());
        //damage
        Damage damage = unitDummy.getDamage();
        mDamageValue.setText("" + damage.getString() + " " + damage.getDamageValue());
        mDamageValue.setOnClickListener(new TouchHelper.OnClickListener() {
            @Override
            public void onClick() {
                EventBus.getDefault().post(new ShowToastEvent(
                        false, false, R.string.not_implemented));
            }
        });
        //armor
        Armor armor = unitDummy.getArmor();
        mArmorValue.setText("" + armor.getString() + " " + armor.getArmorValue());
        mArmorValue.setOnClickListener(new TouchHelper.OnClickListener() {
            @Override
            public void onClick() {
                EventBus.getDefault().post(new ShowToastEvent(
                        false, false, R.string.not_implemented));
            }
        });
        //fire rate
        mFireRate.setText(FireRate.getFireRate().getString(unitDummy.getReloadTime()));
    }

    @Override
    protected void iniDescriptionTextList() {
        mDescriptionTextList = new ArrayList<>(10);
    }

    /** creates and returns {@link DescriptionText} and init it's position */
    private DescriptionText descriptionText(Text text, VertexBufferObjectManager vboManager) {
        return createDescriptionText(text.getWidth() + mSpace, text.getY(), vboManager);
    }
}
