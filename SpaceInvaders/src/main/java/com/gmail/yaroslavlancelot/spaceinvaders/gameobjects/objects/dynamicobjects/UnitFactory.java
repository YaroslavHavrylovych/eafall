package com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.objects.dynamicobjects;

import com.gmail.yaroslavlancelot.spaceinvaders.constants.GameStringConstants;
import com.gmail.yaroslavlancelot.spaceinvaders.utils.TextureRegionHolderUtils;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

public class UnitFactory {
    // 11XX - hands
    public static final int HANDS_ATTACKER = 1110;
    private static final TextureRegionHolderUtils sTextureRegionHolderUtils = TextureRegionHolderUtils.getInstance();

    private UnitFactory() {
    }

    public static Unit getInstance(int unitKey, String teamName, float x, float y, VertexBufferObjectManager vertexBufferObjectManager) {
        switch (unitKey) {
            case HANDS_ATTACKER: {
                ITextureRegion textureRegion;
                if (teamName.equals(GameStringConstants.RED_TEAM_NAME))
                    textureRegion = sTextureRegionHolderUtils.getElement(GameStringConstants.KEY_RED_WARRIOR);
                else
                    textureRegion = sTextureRegionHolderUtils.getElement(GameStringConstants.KEY_BLUE_WARRIOR);
                return getInstance(unitKey, textureRegion, x, y, vertexBufferObjectManager);
            }
        }
        throw new IllegalArgumentException("unknown unit type=" + unitKey);
    }

    public static Unit getInstance(int unitKey, ITextureRegion textureRegion, float x, float y, VertexBufferObjectManager vertexBufferObjectManager) {
        Unit unit;
        switch (unitKey) {
            case HANDS_ATTACKER: {
                unit = new WithoutAnimationAttacker(x, y, textureRegion, vertexBufferObjectManager);
                return unit;
            }
        }
        throw new IllegalArgumentException("unknown unit type=" + unitKey);
    }
}