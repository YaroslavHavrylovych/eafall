package com.gmail.yaroslavlancelot.spaceinvaders.network.adt.messages.client;

import com.gmail.yaroslavlancelot.spaceinvaders.network.MessagesConstants;
import org.andengine.extension.multiplayer.protocol.adt.message.client.ClientMessage;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class ConnectionEstablishClientMessage extends ClientMessage implements MessagesConstants {
    // ===========================================================
    // Constants
    // ===========================================================

    public static final short PROTOCOL_VERSION = 1;

    // ===========================================================
    // Fields
    // ===========================================================

    private short mProtocolVersion;

    // ===========================================================
    // Constructors
    // ===========================================================

    @Deprecated
    public ConnectionEstablishClientMessage() {

    }

    public ConnectionEstablishClientMessage(final short pProtocolVersion) {
        this.mProtocolVersion = pProtocolVersion;
    }

    // ===========================================================
    // Getter & Setter
    // ===========================================================

    public short getProtocolVersion() {
        return this.mProtocolVersion;
    }

    // ===========================================================
    // Methods for/from SuperClass/Interfaces
    // ===========================================================

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

    // ===========================================================
    // Methods
    // ===========================================================

    // ===========================================================
    // Inner and Anonymous Classes
    // ===========================================================
}
