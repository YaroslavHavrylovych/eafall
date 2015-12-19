package com.yaroslavlancelot.eafall.network.server.messages;

import com.yaroslavlancelot.eafall.game.entity.gameobject.building.BuildingId;
import com.yaroslavlancelot.eafall.network.server.messages.constants.ServerMessagesConstants;

import org.andengine.extension.multiplayer.adt.message.server.ServerMessage;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class BuildingCreatedServerMessage extends ServerMessage implements ServerMessagesConstants {
    private BuildingId mBuildingId;
    private String mPlayerName;

    @Deprecated
    public BuildingCreatedServerMessage() {
    }

    public BuildingCreatedServerMessage(int buildingId, int upgrade, String playerName) {
        mBuildingId = BuildingId.makeId(buildingId, upgrade);
        mPlayerName = playerName;
    }

    @Override
    public short getFlag() {
        return BUILDING_CREATED;
    }

    @Override
    protected void onReadTransmissionData(final DataInputStream pDataInputStream) throws IOException {
        int id = pDataInputStream.readInt();
        int upgrade = pDataInputStream.readInt();
        mBuildingId = BuildingId.makeId(id, upgrade);
        mPlayerName = pDataInputStream.readUTF();
    }

    @Override
    protected void onWriteTransmissionData(final DataOutputStream pDataOutputStream) throws IOException {
        pDataOutputStream.writeInt(mBuildingId.getId());
        pDataOutputStream.writeInt(mBuildingId.getUpgrade());
        pDataOutputStream.writeUTF(mPlayerName);
    }

    public BuildingId getBuildingId() {
        return mBuildingId;
    }

    public String getPlayerName() {
        return mPlayerName;
    }
}
