package com.gmail.yaroslavlancelot.spaceinvaders.network.client.messages;

import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.objects.buildings.BuildingId;
import com.gmail.yaroslavlancelot.spaceinvaders.network.client.messages.constants.ClientMessagesConstants;

import org.andengine.extension.multiplayer.protocol.adt.message.client.ClientMessage;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class BuildingCreationClientMessage extends ClientMessage implements ClientMessagesConstants {
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
        return BUILDING_CREATION;
    }
}
