package com.yaroslavlancelot.eafall.game.popup.rolling.description.updater.unit;

import com.yaroslavlancelot.eafall.R;
import com.yaroslavlancelot.eafall.game.entity.gameobject.building.BuildingId;
import com.yaroslavlancelot.eafall.game.events.aperiodic.ingame.description.BuildingDescriptionShowEvent;
import com.yaroslavlancelot.eafall.general.locale.LocaleImpl;

import org.andengine.entity.scene.Scene;
import org.andengine.entity.shape.Shape;
import org.andengine.entity.sprite.ButtonSprite;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import de.greenrobot.event.EventBus;

/**
 * In-game unit description popup updater. Sets base button handler to return
 * to the unit building popup.
 *
 * @author Yaroslav Havrylovych
 */
public class UnitPopupUpdater extends BaseUnitPopupUpdater {
    // ===========================================================
    // Constants
    // ===========================================================

    // ===========================================================
    // Fields
    // ===========================================================

    // ===========================================================
    // Constructors
    // ===========================================================
    public UnitPopupUpdater(final VertexBufferObjectManager vertexBufferObjectManager, final Scene scene) {
        super(vertexBufferObjectManager, scene);
    }

    // ===========================================================
    // Getter & Setter
    // ===========================================================

    // ===========================================================
    // Methods for/from SuperClass/Interfaces
    // ===========================================================
    @Override
    protected void updateBaseButton(Shape drawArea, final Object objectId, final String playerName) {
        //build button
        mBaseButton.setText(LocaleImpl.getInstance().getStringById(R.string.description_back_button));
        mBaseButton.setPosition(mBaseButton.getWidth() / 2, mBaseButton.getHeight() / 2);
        drawArea.attachChild(mBaseButton);
        final BuildingId buildingId = (BuildingId) objectId;
        mBaseButton.setOnClickListener(new ButtonSprite.OnClickListener() {
            @Override
            public void onClick(ButtonSprite pButtonSprite, float pTouchAreaLocalX, float pTouchAreaLocalY) {
                EventBus.getDefault().post(new BuildingDescriptionShowEvent(buildingId, playerName));
            }
        });
    }

    // ===========================================================
    // Methods
    // ===========================================================

    // ===========================================================
    // Inner and Anonymous Classes
    // ===========================================================
}
