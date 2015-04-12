package com.gmail.yaroslavlancelot.eafall.game.entity;

import com.badlogic.gdx.physics.box2d.Body;

import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

/** extends rectangle to have physic body */
public abstract class BodiedSprite extends BatchedSprite {
    /** physics body associated with current object {@link org.andengine.entity.sprite.Sprite} */
    protected volatile Body mPhysicBody;

    protected BodiedSprite(float pX, float pY, float pWidth, float pHeight,
                           ITextureRegion textureRegion,
                           VertexBufferObjectManager pVertexBufferObjectManager) {
        super(pX, pY, pWidth, pHeight, textureRegion, pVertexBufferObjectManager);
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

    private Body removeBody() {
        if (mPhysicBody == null) return null;
        Body body = mPhysicBody;
        mPhysicBody = null;
        body.setUserData(null);
        return body;
    }
}
