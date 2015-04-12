package com.gmail.yaroslavlancelot.eafall.game.entity;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.gmail.yaroslavlancelot.eafall.game.entity.bullets.Bullet;
import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.GameObject;

public class ContactListener implements com.badlogic.gdx.physics.box2d.ContactListener {
    private ContactCallback mContactCallback;

    public void setContactCallback(ContactCallback contactCallback) {
        mContactCallback = contactCallback;
    }

    @Override
    public void beginContact(Contact contact) {
        Object firstBody = contact.getFixtureA().getBody().getUserData();
        Object secondBody = contact.getFixtureB().getBody().getUserData();
        if (firstBody == null || secondBody == null) return;
        if (firstBody instanceof Bullet || secondBody instanceof Bullet)
            attackIfBullet(firstBody, secondBody);
        if (mContactCallback != null) {
            mContactCallback.contact(contact);
        }
    }

    protected void attackIfBullet(Object firstBody, Object secondBody) {
        if (firstBody == null || secondBody == null) return;
        if (firstBody instanceof Bullet && secondBody instanceof Bullet) {
            bulletColliedWithBullet((Bullet) firstBody, (Bullet) secondBody);
            return;
        }
        if (firstBody instanceof Bullet) {
            bulletColliedWithObject((Bullet) firstBody, secondBody);
        } else if (secondBody instanceof Bullet) {
            bulletColliedWithObject((Bullet) secondBody, firstBody);
        }
    }

    protected void bulletColliedWithBullet(Bullet firstBody, Bullet secondBody) {
        if (!firstBody.getAndSetFalseIsObjectAlive() || !secondBody.getAndSetFalseIsObjectAlive()) {
            return;
        }
        firstBody.destroyBullet();
        secondBody.destroyBullet();
    }

    private void bulletColliedWithObject(Bullet bullet, Object object) {
        if (!bullet.getAndSetFalseIsObjectAlive()) {
            return;
        }
        if (object instanceof GameObject) {
            ((GameObject) object).damageObject(bullet.getDamage());
        } else {
            return;
        }
        bullet.destroyBullet();
    }

    @Override
    public void endContact(Contact contact) {
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {
    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {
        if (mContactCallback != null) {
            mContactCallback.contact(contact);
        }
    }

    /** called after beginContact() and postSolve() methods */
    public static interface ContactCallback {
        void contact(Contact contact);
    }
}
