package com.gmail.yaroslavlancelot.eafall.game.entity.gameobject;

import com.gmail.yaroslavlancelot.eafall.game.batching.BatchingKeys;
import com.gmail.yaroslavlancelot.eafall.game.constant.SizeConstants;
import com.gmail.yaroslavlancelot.eafall.game.entity.BatchedSprite;
import com.gmail.yaroslavlancelot.eafall.game.entity.TextureRegionHolder;

import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

/**
 * Represent object health bar (color/TextureRegion depends on player)
 *
 * @author Yaroslav Havrylovych
 */
public class HealthBar {
    private BatchedSprite mHealthBarRectangle;
    private float mHealthBarWidth = SizeConstants.UNIT_SIZE;

    public HealthBar(String player, float healthBarWidth, VertexBufferObjectManager
            vertexBufferObjectManager) {
        setHealthBarWidth(healthBarWidth);
        ITextureRegion textureRegion = TextureRegionHolder.getInstance().getElement(
                getHealthBarTextureRegionKey(player));
        //create health bar sprite
        mHealthBarRectangle = new BatchedSprite(0, 0,
                mHealthBarWidth, SizeConstants.HEALTH_BAR_HEIGHT, textureRegion,
                vertexBufferObjectManager);
        mHealthBarRectangle.setSpriteGroupName(BatchingKeys.BULLET_AND_HEALTH);
    }

    public Sprite getHealthBarSprite() {
        return mHealthBarRectangle;
    }

    private void setHealthBarWidth(float healthBarWidth) {
        mHealthBarWidth = healthBarWidth;
    }

    public static String getHealthBarTextureRegionKey(String player) {
        return "health_bar_" + player;
    }

    public void setPosition(float x, float y) {
        mHealthBarRectangle.setPosition(x + mHealthBarRectangle.getWidth() / 2,
                y + mHealthBarRectangle.getHeight() / 2);
    }

    public void redrawHealthBar(int healthMax, int actualHealth) {
        redrawHealthBar(healthMax, actualHealth, mHealthBarWidth);
    }

    private void redrawHealthBar(int healthMax, int actualHealth, float healthBarWidth) {
        mHealthBarRectangle.setWidth(healthBarWidth * actualHealth / healthMax);
    }
}