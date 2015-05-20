package com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.staticobject;

import com.gmail.yaroslavlancelot.eafall.game.batching.BatchingKeys;
import com.gmail.yaroslavlancelot.eafall.game.constant.SizeConstants;
import com.gmail.yaroslavlancelot.eafall.game.constant.StringConstants;
import com.gmail.yaroslavlancelot.eafall.game.entity.BatchedSprite;
import com.gmail.yaroslavlancelot.eafall.game.entity.TextureRegionHolder;

import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.IEntityModifier;
import org.andengine.entity.modifier.RotationModifier;
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
        attachChild(sunHaze);
        IEntityModifier topSunRotation = new InstantRotationModifier(200);
        sunHaze.registerEntityModifier(topSunRotation);
    }

    /**
     * Custom RotationModifier implementation which {@link RotationModifier#reset()} the
     * animation as soon as it completed.
     * <br/>
     * Always rotate from 0 till 360
     */
    private class InstantRotationModifier extends RotationModifier {
        public InstantRotationModifier(float pDuration) {
            super(pDuration, 0, 360);
        }

        @Override
        protected void onModifierFinished(IEntity pItem) {
            super.onModifierFinished(pItem);
            reset();
        }
    }
}
