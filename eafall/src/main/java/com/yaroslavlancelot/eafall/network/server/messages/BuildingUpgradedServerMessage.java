package com.yaroslavlancelot.eafall.network.server.messages;

import com.yaroslavlancelot.eafall.game.entity.gameobject.building.BuildingId;
import com.yaroslavlancelot.eafall.network.server.messages.constants.ServerMessagesConstants;

import org.andengine.extension.multiplayer.adt.message.server.ServerMessage;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class BuildingUpgradedServerMessage extends ServerMessage implements ServerMessagesConstants {
    private BuildingId mBuildingId;
    private String mPlayerName;

    @Deprecated
    public BuildingUpgradedServerMessage() {
    }

    public BuildingUpgradedServerMessage(int buildingId, int upgrade, String playerName) {
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
    public short getFlag() {
        return BUILDING_UPGRADED;
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
}
