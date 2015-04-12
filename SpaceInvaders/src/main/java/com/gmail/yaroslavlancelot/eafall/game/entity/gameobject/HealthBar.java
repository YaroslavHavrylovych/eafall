package com.gmail.yaroslavlancelot.eafall.game.entity.gameobject;

import com.gmail.yaroslavlancelot.eafall.game.batching.BatchingKeys;
import com.gmail.yaroslavlancelot.eafall.game.constant.SizeConstants;
import com.gmail.yaroslavlancelot.eafall.game.constant.StringConstants;
import com.gmail.yaroslavlancelot.eafall.game.entity.BatchedSprite;
import com.gmail.yaroslavlancelot.eafall.game.entity.TextureRegionHolder;

import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

public class HealthBar {
    private BatchedSprite mHealthBarRectangle;
    private float mHealthBarWidth = SizeConstants.UNIT_SIZE;

    public HealthBar(VertexBufferObjectManager vertexBufferObjectManager, float healthBarWidth) {
        setHealthBarWidth(healthBarWidth);
        //create health bar sprite
        mHealthBarRectangle = new BatchedSprite(0, 0,
                mHealthBarWidth, SizeConstants.HEALTH_BAR_HEIGHT,
                TextureRegionHolder.getInstance().getElement(StringConstants.FILE_HEALTH_BAR),
                vertexBufferObjectManager);
        mHealthBarRectangle.setSpriteGroupName(BatchingKeys.BULLET_HEALTH_TEAM_COLOR);
    }

    private void setHealthBarWidth(float healthBarWidth) {
        mHealthBarWidth = healthBarWidth;
    }

    public void setPosition(float x, float y) {
        mHealthBarRectangle.setPosition(x + mHealthBarRectangle.getWidth() / 2,
                y + mHealthBarRectangle.getHeight() / 2);
    }

    public Sprite getHealthBarSprite() {
        return mHealthBarRectangle;
    }

    public void redrawHealthBar(int healthMax, int actualHealth) {
        redrawHealthBar(healthMax, actualHealth, mHealthBarWidth);
    }

    private void redrawHealthBar(int healthMax, int actualHealth, float healthBarWidth) {
        mHealthBarRectangle.setWidth(healthBarWidth * actualHealth / healthMax);
    }
}