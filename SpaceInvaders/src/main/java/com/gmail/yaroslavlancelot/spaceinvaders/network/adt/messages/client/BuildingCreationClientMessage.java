package com.gmail.yaroslavlancelot.spaceinvaders.network.adt.messages.client;

import com.gmail.yaroslavlancelot.spaceinvaders.network.MessagesConstants;
import org.andengine.extension.multiplayer.protocol.adt.message.client.ClientMessage;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class BuildingCreationClientMessage extends ClientMessage implements MessagesConstants {
    private int mBuildingId;

    public BuildingCreationClientMessage(int buildingId) {
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
    public short getFlag() {
        return FLAG_MESSAGE_CLIENT_WANT_CREATE_BUILDING;
    }

    @Override
    protected void onWriteTransmissionData(final DataOutputStream pDataOutputStream) throws IOException {
        pDataOutputStream.writeInt(mBuildingId);
    }
}
