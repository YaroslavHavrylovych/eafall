package com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.staticobject;

import com.gmail.yaroslavlancelot.eafall.game.batching.BatchingKeys;
import com.gmail.yaroslavlancelot.eafall.game.constant.SizeConstants;
import com.gmail.yaroslavlancelot.eafall.game.constant.StringConstants;
import com.gmail.yaroslavlancelot.eafall.game.engine.InstantRotationModifier;
import com.gmail.yaroslavlancelot.eafall.game.entity.BatchedSprite;
import com.gmail.yaroslavlancelot.eafall.game.entity.TextureRegionHolder;
import com.gmail.yaroslavlancelot.eafall.game.entity.health.IHealthBar;

import org.andengine.entity.modifier.IEntityModifier;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import java.util.Random;

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

}
