package com.yaroslavlancelot.eafall.game.entity.health;

import com.yaroslavlancelot.eafall.game.batching.BatchingKeys;
import com.yaroslavlancelot.eafall.game.constant.SizeConstants;
import com.yaroslavlancelot.eafall.game.entity.BatchedSprite;
import com.yaroslavlancelot.eafall.game.entity.TextureRegionHolder;

import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

/**
 * Represent object health bar (color/TextureRegion depends on player)
 *
 * @author Yaroslav Havrylovych
 */
public class UnitHealthBar implements IHealthBar {

    private final BatchedSprite mHealthBarRectangle;
    private float mHealthBarWidth = SizeConstants.UNIT_SIZE;

    public UnitHealthBar(String player, float healthBarWidth, VertexBufferObjectManager vboManager) {
        this(healthBarWidth, TextureRegionHolder.getInstance().getElement(
                getHealthBarTextureRegionKey(player)), vboManager);
    }

    public UnitHealthBar(float healthBarWidth, ITextureRegion textureRegion,
                         VertexBufferObjectManager vboManager) {
        mHealthBarWidth = healthBarWidth;
        // create health bar sprite
        mHealthBarRectangle = new BatchedSprite(0, 0,
                mHealthBarWidth, SizeConstants.UNIT_HEALTH_BAR_HEIGHT, textureRegion,
                vboManager);
        mHealthBarRectangle.setSpriteGroupName(BatchingKeys.BULLET_AND_HEALTH);
    }

    @Override
    public boolean isVisible() {
        return mHealthBarRectangle.isVisible();
    }

    @Override
    public void setVisible(boolean visible) {
        mHealthBarRectangle.setVisible(visible);
    }

    @Override
    public void addToParent(final BatchedSprite parent) {
        parent.addChild(mHealthBarRectangle);
    }

    @Override
    public void setPosition(float x, float y) {
        mHealthBarRectangle.setPosition(
                x + mHealthBarRectangle.getWidth() / 2,
                y + mHealthBarRectangle.getHeight() / 2
        );
    }

    @Override
    public void redrawHealthBar(int healthMax, int actualHealth) {
        mHealthBarRectangle.setWidth(mHealthBarWidth * actualHealth / healthMax);
    }

    public static String getHealthBarTextureRegionKey(String player) {
        return "health_bar_" + player;
    }
}
