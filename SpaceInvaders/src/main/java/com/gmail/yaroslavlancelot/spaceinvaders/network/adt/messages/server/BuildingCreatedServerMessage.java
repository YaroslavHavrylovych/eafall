package com.gmail.yaroslavlancelot.spaceinvaders.network.adt.messages.server;

import com.gmail.yaroslavlancelot.spaceinvaders.gameobjects.objects.buildings.BuildingId;
import com.gmail.yaroslavlancelot.spaceinvaders.network.MessagesConstants;

import org.andengine.extension.multiplayer.protocol.adt.message.server.ServerMessage;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class BuildingCreatedServerMessage extends ServerMessage implements MessagesConstants {
    private BuildingId mBuildingId;
    private String mTeamName;

    @Deprecated
    public BuildingCreatedServerMessage() {
    }

    public BuildingCreatedServerMessage(int buildingId, int upgrade, String teamName) {
        mBuildingId = BuildingId.makeId(buildingId, upgrade);
        mTeamName = teamName;
    }

    @Override
    public short getFlag() {
        return FLAG_MESSAGE_SERVER_BUILDING_CREATED;
    }

    @Override
    protected void onReadTransmissionData(final DataInputStream pDataInputStream) throws IOException {
        int id = pDataInputStream.readInt();
        int upgrade = pDataInputStream.readInt();
        mBuildingId = BuildingId.makeId(id, upgrade);
        mTeamName = pDataInputStream.readUTF();
    }

    @Override
    protected void onWriteTransmissionData(final DataOutputStream pDataOutputStream) throws IOException {
        pDataOutputStream.writeInt(mBuildingId.getId());
        pDataOutputStream.writeInt(mBuildingId.getUpgrade());
        pDataOutputStream.writeUTF(mTeamName);
    }

    public BuildingId getBuildingId() {
        return mBuildingId;
    }

    public String getTeamName() {
        return mTeamName;
    }
}
