package com.gmail.yaroslavlancelot.eafall.network.server.messages;

import com.gmail.yaroslavlancelot.eafall.game.entity.gameobject.unit.Unit;

import com.gmail.yaroslavlancelot.eafall.network.server.messages.constants.ServerMessagesConstants;

import org.andengine.extension.multiplayer.protocol.adt.message.server.ServerMessage;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class UnitCreatedServerMessage extends ServerMessage implements ServerMessagesConstants {
    private int mUnitId;
    private String mTeamName;
    private float mX, mY;
    private long mUnitUniqueId;

    @Deprecated
    public UnitCreatedServerMessage() {
    }

    public UnitCreatedServerMessage(String teamName, int unitId, Unit unit) {
        mUnitId = unitId;
        mTeamName = teamName;
        mX = unit.getX();
        mY = unit.getY();
        mUnitUniqueId = unit.getObjectUniqueId();
    }

    @Override
    protected void onReadTransmissionData(final DataInputStream pDataInputStream) throws IOException {
        mUnitId = pDataInputStream.readInt();
        mTeamName = pDataInputStream.readUTF();
        mX = pDataInputStream.readFloat();
        mY = pDataInputStream.readFloat();
        mUnitUniqueId = pDataInputStream.readLong();
    }

    @Override
    protected void onWriteTransmissionData(final DataOutputStream pDataOutputStream) throws IOException {
        pDataOutputStream.writeInt(mUnitId);
        pDataOutputStream.writeUTF(mTeamName);
        pDataOutputStream.writeFloat(mX);
        pDataOutputStream.writeFloat(mY);
        pDataOutputStream.writeLong(mUnitUniqueId);
    }

    @Override
    public short getFlag() {
        return UNIT_CREATED;
    }

    public float getY() {
        return mY;
    }

    public float getX() {
        return mX;
    }

    public String getTeamName() {
        return mTeamName;
    }

    public int getUnitId() {
        return mUnitId;
    }

    public long getUnitUniqueId() {
        return mUnitUniqueId;
    }
}
