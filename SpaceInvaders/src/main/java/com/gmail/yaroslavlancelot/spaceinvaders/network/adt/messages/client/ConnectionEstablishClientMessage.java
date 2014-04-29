package com.gmail.yaroslavlancelot.spaceinvaders.network.adt.messages.client;

import com.gmail.yaroslavlancelot.spaceinvaders.network.MessagesConstants;
import org.andengine.extension.multiplayer.protocol.adt.message.client.ClientMessage;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class ConnectionEstablishClientMessage extends ClientMessage implements MessagesConstants {
    public static final short PROTOCOL_VERSION = 1;

    private short mProtocolVersion;

    @Deprecated
    public ConnectionEstablishClientMessage() {
    }

    public ConnectionEstablishClientMessage(final short pProtocolVersion) {
        this.mProtocolVersion = pProtocolVersion;
    }

    public short getProtocolVersion() {
        return this.mProtocolVersion;
    }

    @Override
    public short getFlag() {
        return FLAG_MESSAGE_CLIENT_CONNECTION_ESTABLISHED;
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
