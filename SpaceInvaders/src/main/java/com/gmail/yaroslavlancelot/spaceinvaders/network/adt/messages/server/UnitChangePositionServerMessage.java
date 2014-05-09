package com.gmail.yaroslavlancelot.spaceinvaders.network.adt.messages.server;

import com.badlogic.gdx.math.Vector2;
import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.objects.dynamicobjects.Unit;
import com.gmail.yaroslavlancelot.spaceinvaders.network.MessagesConstants;

import org.andengine.extension.multiplayer.protocol.adt.message.server.ServerMessage;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class UnitChangePositionServerMessage extends ServerMessage implements MessagesConstants {
    private float mX, mY;
    private long mUnitUniqueId;
    private float mVelocityX, mVelocityY;
    private float mRotationAngle;

    @Deprecated
    public UnitChangePositionServerMessage() {
    }

    public UnitChangePositionServerMessage(Unit unit) {
        Vector2 positionVector = unit.getBody().getPosition();
        mX = positionVector.x;
        mY = positionVector.y;
        mUnitUniqueId = unit.getObjectUniqueId();
        Vector2 velocityVector = unit.getBody().getLinearVelocity();
        mVelocityX = velocityVector.x;
        mVelocityY = velocityVector.y;
        mRotationAngle = unit.getRotationAngle();
    }

    @Override
    protected void onReadTransmissionData(final DataInputStream pDataInputStream) throws IOException {
        mX = pDataInputStream.readFloat();
        mY = pDataInputStream.readFloat();
        mUnitUniqueId = pDataInputStream.readLong();
        mVelocityX = pDataInputStream.readFloat();
        mVelocityY = pDataInputStream.readFloat();
        mRotationAngle = pDataInputStream.readFloat();
    }

    @Override
    protected void onWriteTransmissionData(final DataOutputStream pDataOutputStream) throws IOException {
        pDataOutputStream.writeFloat(mX);
        pDataOutputStream.writeFloat(mY);
        pDataOutputStream.writeLong(mUnitUniqueId);
        pDataOutputStream.writeFloat(mVelocityX);
        pDataOutputStream.writeFloat(mVelocityY);
        pDataOutputStream.writeFloat(mRotationAngle);
    }

    @Override
    public short getFlag() {
        return FLAG_MESSAGE_SERVER_UNIT_MOVED;
    }

    public float getY() {
        return mY;
    }

    public float getX() {
        return mX;
    }

    public float getRotationAngle() { return mRotationAngle; }

    public long getUnitUniqueId() {
        return mUnitUniqueId;
    }

    public float getVelocityX() {
        return mVelocityX;
    }

    public float getVelocityY() {
        return mVelocityY;
    }
}
