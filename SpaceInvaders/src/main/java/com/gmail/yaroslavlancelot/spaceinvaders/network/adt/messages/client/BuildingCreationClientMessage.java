package com.gmail.yaroslavlancelot.spaceinvaders.network.adt.messages.client;

import com.gmail.yaroslavlancelot.spaceinvaders.objects.objects.buildings.BuildingId;
import com.gmail.yaroslavlancelot.spaceinvaders.network.MessagesConstants;

import org.andengine.extension.multiplayer.protocol.adt.message.client.ClientMessage;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class BuildingCreationClientMessage extends ClientMessage implements MessagesConstants {
    private BuildingId mBuildingId;
    private String mTeamName;

    @Deprecated
    public BuildingCreationClientMessage() {
    }

    public BuildingCreationClientMessage(String teamName, int buildingId, int upgrade) {
        mBuildingId = BuildingId.makeId(buildingId, upgrade);
        mTeamName = teamName;
    }

    public BuildingId getBuildingId() {
        return mBuildingId;
    }

    public String getTeamName() {
        return mTeamName;
    }

    @Override
    protected void onReadTransmissionData(final DataInputStream pDataInputStream) throws IOException {
        mTeamName = pDataInputStream.readUTF();
        int id = pDataInputStream.readInt();
        int upgrade = pDataInputStream.readInt();
        mBuildingId = BuildingId.makeId(id, upgrade);
    }

    @Override
    protected void onWriteTransmissionData(final DataOutputStream pDataOutputStream) throws IOException {
        pDataOutputStream.writeUTF(mTeamName);
        pDataOutputStream.writeInt(mBuildingId.getId());
        pDataOutputStream.writeInt(mBuildingId.getUpgrade());
    }

    @Override
    public short getFlag() {
        return FLAG_MESSAGE_CLIENT_WANT_CREATE_BUILDING;
    }
}
