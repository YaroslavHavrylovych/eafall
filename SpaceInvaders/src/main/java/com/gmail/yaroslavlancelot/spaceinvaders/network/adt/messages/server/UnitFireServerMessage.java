package com.gmail.yaroslavlancelot.spaceinvaders.network.adt.messages.server;

import com.gmail.yaroslavlancelot.spaceinvaders.network.MessagesConstants;

import org.andengine.extension.multiplayer.protocol.adt.message.server.ServerMessage;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class UnitFireServerMessage extends ServerMessage implements MessagesConstants {
    private long mUnitUniqueId;

    private long mAttackedGameObjectUniqueId;

    @Deprecated
    public UnitFireServerMessage() {
    }

    public UnitFireServerMessage(long unitUniqueId, long attackedGameObjectUniqueId) {
        mUnitUniqueId = unitUniqueId;
        mAttackedGameObjectUniqueId = attackedGameObjectUniqueId;
    }

    @Override
    protected void onReadTransmissionData(final DataInputStream pDataInputStream) throws IOException {
        mUnitUniqueId = pDataInputStream.readLong();
        mAttackedGameObjectUniqueId = pDataInputStream.readLong();
    }

    @Override
    protected void onWriteTransmissionData(final DataOutputStream pDataOutputStream) throws IOException {
        pDataOutputStream.writeLong(mUnitUniqueId);
        pDataOutputStream.writeLong(mAttackedGameObjectUniqueId);
    }

    @Override
    public short getFlag() {
        return FLAG_MESSAGE_SERVER_UNIT_FIRE;
    }

    public long getAttackedGameObjectUniqueId() {
        return mAttackedGameObjectUniqueId;
    }

    public long getUnitUniqueId() {
        return mUnitUniqueId;
    }
}
