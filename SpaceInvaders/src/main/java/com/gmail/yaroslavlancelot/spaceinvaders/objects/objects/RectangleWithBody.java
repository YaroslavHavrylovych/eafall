package com.gmail.yaroslavlancelot.spaceinvaders.objects.objects;

import com.badlogic.gdx.physics.box2d.Body;

import org.andengine.entity.primitive.Rectangle;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

/** extends rectangle to have physic body */
public abstract class RectangleWithBody extends Rectangle {
    /** physics body associated with current object {@link org.andengine.entity.sprite.Sprite} */
    protected volatile Body mPhysicBody;

    protected RectangleWithBody(float pX, float pY, float pWidth, float pHeight, VertexBufferObjectManager pVertexBufferObjectManager) {
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

    private Body removeBody() {
        if (mPhysicBody == null) return null;
        Body body = mPhysicBody;
        mPhysicBody = null;
        body.setUserData(null);
        return body;
    }

    public synchronized void removeBody(PhysicsWorld physicsWorld) {
        Body body = removeBody();
        if (body == null) return;
        final PhysicsConnector pc = physicsWorld.getPhysicsConnectorManager()
                .findPhysicsConnectorByShape(this);
        if (pc != null) {
            physicsWorld.unregisterPhysicsConnector(pc);
        }
        body.setActive(false);
        physicsWorld.destroyBody(body);
    }
}
