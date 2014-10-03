package com.gmail.yaroslavlancelot.spaceinvaders.network.adt.messages.client;

import com.gmail.yaroslavlancelot.spaceinvaders.network.MessagesConstants;

import org.andengine.extension.multiplayer.protocol.adt.message.client.ClientMessage;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class BuildingCreationClientMessage extends ClientMessage implements MessagesConstants {
    private int mBuildingId;
    private String mTeamName;

    @Deprecated
    public BuildingCreationClientMessage() {
    }

    public BuildingCreationClientMessage(String teamName, int buildingId) {
        mBuildingId = buildingId;
        mTeamName = teamName;
    }

    public int getBuildingId() {
        return mBuildingId;
    }

    public String getTeamName() {
        return mTeamName;
    }

    @Override
    protected void onReadTransmissionData(final DataInputStream pDataInputStream) throws IOException {
        mTeamName = pDataInputStream.readUTF();
        mBuildingId = pDataInputStream.readInt();
    }

    @Override
    protected void onWriteTransmissionData(final DataOutputStream pDataOutputStream) throws IOException {
        pDataOutputStream.writeUTF(mTeamName);
        pDataOutputStream.writeInt(mBuildingId);
    }

    @Override
    public short getFlag() {
        return FLAG_MESSAGE_CLIENT_WANT_CREATE_BUILDING;
    }
}
