package com.yaroslavlancelot.eafall.game.entity;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.Manifold;

public class ContactListener implements com.badlogic.gdx.physics.box2d.ContactListener {
    private ContactCallback mContactCallback;

    public void setContactCallback(ContactCallback contactCallback) {
        mContactCallback = contactCallback;
    }

    @Override
    public void beginContact(Contact contact) {
        if (mContactCallback != null) {
            mContactCallback.contact(contact);
        }
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
    public interface ContactCallback {
        void contact(Contact contact);
    }
}
