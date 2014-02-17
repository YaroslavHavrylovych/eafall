package com.gmail.yaroslavlancelot.spaceinvaders.network.adt.messages.client;

import com.gmail.yaroslavlancelot.spaceinvaders.network.MessagesConstants;
import org.andengine.extension.multiplayer.protocol.adt.message.client.ClientMessage;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class BuildingCreatedClientMessage extends ClientMessage implements MessagesConstants {
    private int mBuildingId;

    public BuildingCreatedClientMessage(int buildingId) {
        mBuildingId = buildingId;
    }

    public int getBuildingId() {
        return mBuildingId;
    }

    @Override
    protected void onReadTransmissionData(final DataInputStream pDataInputStream) throws IOException {
        mBuildingId = pDataInputStream.readInt();
    }

    @Override
    protected void onWriteTransmissionData(final DataOutputStream pDataOutputStream) throws IOException {
        pDataOutputStream.writeInt(mBuildingId);
    }

    @Override
    public short getFlag() {
        return 0;
    }
}
