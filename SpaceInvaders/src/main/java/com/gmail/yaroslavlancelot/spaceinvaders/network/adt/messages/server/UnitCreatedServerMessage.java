package com.gmail.yaroslavlancelot.spaceinvaders.network.adt.messages.server;

import com.gmail.yaroslavlancelot.spaceinvaders.network.MessagesConstants;
import org.andengine.extension.multiplayer.protocol.adt.message.server.ServerMessage;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class UnitCreatedServerMessage extends ServerMessage implements MessagesConstants {
    private int mUnitId;
    private String mTeamName;
    private float mX, mY;

    @Deprecated
    public UnitCreatedServerMessage() {
    }

    public UnitCreatedServerMessage(String teamName, int unitId, float x, float y) {
        mUnitId = unitId;
        mTeamName = teamName;
        mX = x;
        mY = y;
    }

    @Override
    protected void onReadTransmissionData(final DataInputStream pDataInputStream) throws IOException {
        mUnitId = pDataInputStream.readInt();
        mTeamName = pDataInputStream.readUTF();
        mX = pDataInputStream.readFloat();
        mY = pDataInputStream.readFloat();
    }

    @Override
    protected void onWriteTransmissionData(final DataOutputStream pDataOutputStream) throws IOException {
        pDataOutputStream.writeInt(mUnitId);
        pDataOutputStream.writeUTF(mTeamName);
        pDataOutputStream.writeFloat(mX);
        pDataOutputStream.writeFloat(mY);
    }

    @Override
    public short getFlag() {
        return FLAG_MESSAGE_SERVER_UNIT_CREATED;
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
}
