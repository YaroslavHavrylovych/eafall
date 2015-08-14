package com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.unit.dynamic;

import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.unit.UnitBuilder;
import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.unit.UnitDummy;
import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.unit.loader.UnitLoader;

import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

/**
 * extends unit with speed for moving
 */
public class MovableUnitDummy extends UnitDummy {
    /** speed of the unit */
    private final float mUnitSpeed;

    public MovableUnitDummy(UnitLoader unitLoader, String allianceName) {
        super(unitLoader, allianceName);
        mUnitSpeed = ((float) mUnitLoader.speed) / 100;
    }

    public float getSpeed() {
        return mUnitSpeed;
    }

    /** create and return movable unit builder */
    public UnitBuilder createBuilder(final ITextureRegion spriteTextureRegion,
                                     VertexBufferObjectManager objectManager) {
        return ((MovableUnitBuilder) super.createBuilder(spriteTextureRegion, objectManager))
                .setSpeed(getSpeed());
    }

    @Override
    protected UnitBuilder createUnitBuilder(ITextureRegion textureRegion,
                                            VertexBufferObjectManager objectManager) {
        return new MovableUnitBuilder(textureRegion, objectManager);
    }
}
