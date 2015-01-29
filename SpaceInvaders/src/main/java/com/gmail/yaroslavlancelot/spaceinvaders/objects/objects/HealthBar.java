package com.gmail.yaroslavlancelot.spaceinvaders.objects.objects;

import org.andengine.entity.IEntity;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.color.Color;

public class HealthBar {
    public static final int HEALTH_BOARD_HEIGHT = 3;
    private Rectangle mHealthBarRectangle;
    private float mHealthBarWidth = 16;

    public HealthBar(VertexBufferObjectManager vertexBufferObjectManager, float healthBarWidth) {
        setHealthBarWidth(healthBarWidth);
        mHealthBarRectangle = new Rectangle(0, -HEALTH_BOARD_HEIGHT - HEALTH_BOARD_HEIGHT / 2,
                mHealthBarWidth, HEALTH_BOARD_HEIGHT, vertexBufferObjectManager);
        mHealthBarRectangle.setColor(Color.GREEN);
    }

    private void setHealthBarWidth(float healthBarWidth) {
        mHealthBarWidth = healthBarWidth;
    }

    public IEntity getHealthBar() {
        return mHealthBarRectangle;
    }

    public void redrawHealthBar(int healthMax, int actualHealth) {
        redrawHealthBar(healthMax, actualHealth, mHealthBarWidth);
    }

    private void redrawHealthBar(int healthMax, int actualHealth, float healthBarWidth) {
        mHealthBarRectangle.setWidth(healthBarWidth * actualHealth / healthMax);
    }
}