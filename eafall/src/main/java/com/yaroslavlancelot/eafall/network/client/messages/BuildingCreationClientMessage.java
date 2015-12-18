package com.yaroslavlancelot.eafall.network.client.messages;

import com.yaroslavlancelot.eafall.network.client.messages.constants.ClientMessagesConstants;
import com.yaroslavlancelot.eafall.game.entity.gameobject.building.BuildingId;

import org.andengine.extension.multiplayer.adt.message.client.ClientMessage;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class BuildingCreationClientMessage extends ClientMessage implements ClientMessagesConstants {
    private BuildingId mBuildingId;
    private String mPlayerName;

    @Deprecated
    public BuildingCreationClientMessage() {
    }

    public BuildingCreationClientMessage(String playerName, int buildingId, int upgrade) {
        mBuildingId = BuildingId.makeId(buildingId, upgrade);
        mPlayerName = playerName;
    }

    public BuildingId getBuildingId() {
        return mBuildingId;
    }

    public String getPlayerName() {
        return mPlayerName;
    }

    @Override
    protected void onReadTransmissionData(final DataInputStream pDataInputStream) throws IOException {
        mPlayerName = pDataInputStream.readUTF();
        int id = pDataInputStream.readInt();
        int upgrade = pDataInputStream.readInt();
        mBuildingId = BuildingId.makeId(id, upgrade);
    }

    @Override
    protected void onWriteTransmissionData(final DataOutputStream pDataOutputStream) throws IOException {
        pDataOutputStream.writeUTF(mPlayerName);
        pDataOutputStream.writeInt(mBuildingId.getId());
        pDataOutputStream.writeInt(mBuildingId.getUpgrade());
    }

    @Override
    public short getFlag() {
        return BUILDING_CREATION;
    }
}
