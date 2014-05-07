package com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.objects;

import com.badlogic.gdx.physics.box2d.Body;

import org.andengine.entity.primitive.Rectangle;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

public abstract class IGameObject extends Rectangle {
    /** physics body associated with current object {@link org.andengine.entity.sprite.Sprite} */
    protected volatile Body mPhysicBody;

    protected IGameObject(float pX, float pY, float pWidth, float pHeight, VertexBufferObjectManager pVertexBufferObjectManager) {
        super(pX, pY, pWidth, pHeight, pVertexBufferObjectManager);
    }

    public Body getBody() {
        return mPhysicBody;
    }

    /**
     * set physics body associated with current object
     *
     * @param body the physics body
     */
    public void setBody(Body body) {
        mPhysicBody = body;
        mPhysicBody.setUserData(this);
    }

    public synchronized Body removeBody() {
        if (mPhysicBody == null) return null;
        Body body = mPhysicBody;
        mPhysicBody = null;
        body.setUserData(null);
        return body;
    }
}
