package com.gmail.yaroslavlancelot.eafall.game.entity.gameobject;

import org.andengine.entity.IEntity;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.adt.color.Color;

public class HealthBar {
    public static final int HEALTH_BAR_HEIGHT = 3;
    private Rectangle mHealthBarRectangle;
    private float mHealthBarWidth = 16;

    public HealthBar(VertexBufferObjectManager vertexBufferObjectManager, float healthBarWidth) {
        setHealthBarWidth(healthBarWidth);
        mHealthBarRectangle = new Rectangle(0, 0, mHealthBarWidth, HEALTH_BAR_HEIGHT, vertexBufferObjectManager);
        mHealthBarRectangle.setColor(Color.GREEN);
    }

    private void setHealthBarWidth(float healthBarWidth) {
        mHealthBarWidth = healthBarWidth;
    }

    public void setPosition(float x, float y) {
        mHealthBarRectangle.setPosition(x, y);
    }

    public IEntity getHealthBar() {
        return mHealthBarRectangle;
    }

    public void redrawHealthBar(int healthMax, int actualHealth) {
        redrawHealthBar(healthMax, actualHealth, mHealthBarWidth);
    }

    private void redrawHealthBar(int healthMax, int actualHealth, float healthBarWidth) {
        mHealthBarRectangle.setWidth(healthBarWidth * actualHealth / healthMax);
        mHealthBarRectangle.setX(mHealthBarRectangle.getWidth() / 2);
    }
}