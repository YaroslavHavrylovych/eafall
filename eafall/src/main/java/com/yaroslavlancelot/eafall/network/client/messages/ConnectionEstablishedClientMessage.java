package com.yaroslavlancelot.eafall.network.client.messages;

import com.yaroslavlancelot.eafall.network.client.messages.constants.ClientMessagesConstants;

import org.andengine.extension.multiplayer.adt.message.client.ClientMessage;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class ConnectionEstablishedClientMessage extends ClientMessage implements ClientMessagesConstants {
    public static final short PROTOCOL_VERSION = 1;

    private short mProtocolVersion;

    @Deprecated
    public ConnectionEstablishedClientMessage() {
    }

    public ConnectionEstablishedClientMessage(final short pProtocolVersion) {
        this.mProtocolVersion = pProtocolVersion;
    }

    public short getProtocolVersion() {
        return this.mProtocolVersion;
    }

    @Override
    public short getFlag() {
        return CONNECTION_ESTABLISHED;
    }

    @Override
    protected void onReadTransmissionData(final DataInputStream pDataInputStream) throws IOException {
        this.mProtocolVersion = pDataInputStream.readShort();
    }

    @Override
    protected void onWriteTransmissionData(final DataOutputStream pDataOutputStream) throws IOException {
        pDataOutputStream.writeShort(this.mProtocolVersion);
    }
}
