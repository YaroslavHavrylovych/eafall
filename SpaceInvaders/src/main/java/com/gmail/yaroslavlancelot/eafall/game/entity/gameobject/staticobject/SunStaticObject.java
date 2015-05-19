package com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.staticobject;

import com.gmail.yaroslavlancelot.eafall.game.batching.BatchingKeys;
import com.gmail.yaroslavlancelot.eafall.game.constant.SizeConstants;

import org.andengine.entity.modifier.LoopEntityModifier;
import org.andengine.entity.modifier.RotationModifier;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

public class SunStaticObject extends StaticObject {
    LoopEntityModifier mConstantRotationModifier =
            new LoopEntityModifier(new RotationModifier(135, 0, 360));

    public SunStaticObject(float x, float y, ITextureRegion textureRegion, VertexBufferObjectManager vertexBufferObjectManager) {
        super(x, y, SizeConstants.SUN_DIAMETER, SizeConstants.SUN_DIAMETER,
                textureRegion, vertexBufferObjectManager);
        mIncomeIncreasingValue = 0;
        setSpriteGroupName(BatchingKeys.SUN_PLANET);
        registerEntityModifier(mConstantRotationModifier);
    }
}
