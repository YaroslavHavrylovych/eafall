package com.gmail.yaroslavlancelot.spaceinvaders.network.adt.messages.server;

import com.gmail.yaroslavlancelot.spaceinvaders.network.MessagesConstants;
import org.andengine.extension.multiplayer.protocol.adt.message.server.ServerMessage;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class BuildingCreatedServerMessage extends ServerMessage implements MessagesConstants {
    private int mBuildingId;
    private String mTeamName;

    @Deprecated
    public BuildingCreatedServerMessage() {
    }

    public BuildingCreatedServerMessage(int buildingId, String teamName) {
        mBuildingId = buildingId;
        mTeamName = teamName;
    }

    @Override
    public short getFlag() {
        return FLAG_MESSAGE_SERVER_BUILDING_CREATED;
    }

    @Override
    protected void onReadTransmissionData(final DataInputStream pDataInputStream) throws IOException {
        mBuildingId = pDataInputStream.readInt();
        mTeamName = pDataInputStream.readUTF();
    }

    @Override
    protected void onWriteTransmissionData(final DataOutputStream pDataOutputStream) throws IOException {
        pDataOutputStream.writeInt(mBuildingId);
        pDataOutputStream.writeUTF(mTeamName);
    }

    public int getBuildingId() {
        return mBuildingId;
    }

    public String getTeamName() {
        return mTeamName;
    }
}
