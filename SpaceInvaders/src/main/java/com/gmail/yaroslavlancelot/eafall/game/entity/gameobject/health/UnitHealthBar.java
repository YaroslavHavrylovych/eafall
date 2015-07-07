package com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.health;

import com.gmail.yaroslavlancelot.eafall.game.batching.BatchingKeys;
import com.gmail.yaroslavlancelot.eafall.game.constant.SizeConstants;
import com.gmail.yaroslavlancelot.eafall.game.entity.BatchedSprite;
import com.gmail.yaroslavlancelot.eafall.game.entity.TextureRegionHolder;

import org.andengine.entity.IEntity;
import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

/**
 * Represent object health bar (color/TextureRegion depends on player)
 *
 * @author Yaroslav Havrylovych
 */
public class UnitHealthBar implements IHealthBar {
    private BatchedSprite mHealthBarRectangle;
    private float mHealthBarWidth = SizeConstants.UNIT_SIZE;

    public UnitHealthBar(String player, float healthBarWidth, VertexBufferObjectManager
            vertexBufferObjectManager) {
        setHealthBarWidth(healthBarWidth);
        ITextureRegion textureRegion = TextureRegionHolder.getInstance().getElement(
                getHealthBarTextureRegionKey(player));
        //create health bar sprite
        mHealthBarRectangle = new BatchedSprite(0, 0,
                mHealthBarWidth, SizeConstants.UNIT_HEALTH_BAR_HEIGHT, textureRegion,
                vertexBufferObjectManager);
        mHealthBarRectangle.setSpriteGroupName(BatchingKeys.BULLET_AND_HEALTH);
    }

    private Sprite getHealthBarSprite() {
        return mHealthBarRectangle;
    }

    private void setHealthBarWidth(float healthBarWidth) {
        mHealthBarWidth = healthBarWidth;
    }

    @Override
    public void attachHealthBar(final IEntity parent) {
        parent.attachChild(getHealthBarSprite());
    }

    @Override
    public void setPosition(float x, float y) {
        mHealthBarRectangle.setPosition(x + mHealthBarRectangle.getWidth() / 2,
                y + mHealthBarRectangle.getHeight() / 2);
    }

    public void redrawHealthBar(int healthMax, int actualHealth) {
        redrawHealthBar(healthMax, actualHealth, mHealthBarWidth);
    }

    public static String getHealthBarTextureRegionKey(String player) {
        return "health_bar_" + player;
    }

    private void redrawHealthBar(int healthMax, int actualHealth, float healthBarWidth) {
        mHealthBarRectangle.setWidth(healthBarWidth * actualHealth / healthMax);
    }
}
