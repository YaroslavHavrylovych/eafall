package com.yaroslavlancelot.eafall.game.entity.gameobject.staticobject;

import com.yaroslavlancelot.eafall.R;
import com.yaroslavlancelot.eafall.game.batching.BatchingKeys;
import com.yaroslavlancelot.eafall.game.constant.SizeConstants;
import com.yaroslavlancelot.eafall.game.constant.StringConstants;
import com.yaroslavlancelot.eafall.game.engine.InstantRotationModifier;
import com.yaroslavlancelot.eafall.game.entity.BatchedSprite;
import com.yaroslavlancelot.eafall.game.entity.TextureRegionHolder;
import com.yaroslavlancelot.eafall.game.entity.health.IHealthBar;
import com.yaroslavlancelot.eafall.game.events.aperiodic.ShowHudTextEvent;
import com.yaroslavlancelot.eafall.game.touch.TouchHelper;

import org.andengine.entity.modifier.IEntityModifier;
import org.andengine.input.touch.detector.ClickDetector;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import java.util.Random;

import de.greenrobot.event.EventBus;

/**
 * Sun represents the sun entity on the game field.
 * <br/>
 * Sun is animated (but simply). It contains children entity(sun_haze) which rotates slower than
 * sun which give small animation effects and doest cost tons of GPU and etc.
 */
public class SunStaticObject extends StaticObject {
    public SunStaticObject(float x, float y, VertexBufferObjectManager vertexBufferObjectManager) {
        super(x, y, SizeConstants.SUN_DIAMETER, SizeConstants.SUN_DIAMETER,
                TextureRegionHolder.getRegion(StringConstants.KEY_SUN), vertexBufferObjectManager);
        mIncomeIncreasingValue = 0;
        setSpriteGroupName(BatchingKeys.SUN_PLANET);
        setRotation(new Random().nextInt(360));
        IEntityModifier bottomSunRotation = new InstantRotationModifier(135);
        registerEntityModifier(bottomSunRotation);
        //haze
        initChildren();
        BatchedSprite sunHaze = new BatchedSprite(x, y, getWidth(), getHeight(),
                TextureRegionHolder.getRegion(StringConstants.KEY_SUN_HAZE), vertexBufferObjectManager);
        sunHaze.setSpriteGroupName(BatchingKeys.SUN_PLANET);
        sunHaze.attachSelf();
        IEntityModifier topSunRotation = new InstantRotationModifier(200);
        sunHaze.registerEntityModifier(topSunRotation);
    }

    @Override
    protected IHealthBar createHealthBar() {
        return null;
    }

    public void initDescription(final int starName, final int constellationName) {
        setTouchCallback(new TouchHelper.UnboundedClickListener(new ClickDetector.IClickDetectorListener() {
            @Override
            public void onClick(final ClickDetector pClickDetector, final int pPointerID, final float pSceneX, final float pSceneY) {
                EventBus.getDefault().post(new ShowHudTextEvent(
                        R.string.planet_system_star, constellationName, starName));
            }
        }));
    }
}
