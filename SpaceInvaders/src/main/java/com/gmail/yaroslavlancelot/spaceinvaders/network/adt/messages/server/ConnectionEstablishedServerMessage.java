package com.gmail.yaroslavlancelot.spaceinvaders.network.adt.messages.server;

import com.gmail.yaroslavlancelot.spaceinvaders.network.example.common.ICommonMessages;
import org.andengine.extension.multiplayer.protocol.adt.message.server.ServerMessage;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class ConnectionEstablishedServerMessage extends ServerMessage implements ICommonMessages {
    // ===========================================================
    // Constants
    // ===========================================================

    // ===========================================================
    // Fields
    // ===========================================================

    // ===========================================================
    // Constructors
    // ===========================================================

    public ConnectionEstablishedServerMessage() {

    }

    // ===========================================================
    // Getter & Setter
    // ===========================================================

    // ===========================================================
    // Methods for/from SuperClass/Interfaces
    // ===========================================================

    @Override
    public short getFlag() {
        return FLAG_MESSAGE_SERVER_CONNECTION_ESTABLISHED;
    }

    @Override
    protected void onReadTransmissionData(final DataInputStream pDataInputStream) throws IOException {
                /* Nothing to read. */
    }

    @Override
    protected void onWriteTransmissionData(final DataOutputStream pDataOutputStream) throws IOException {
                /* Nothing to write. */
    }

    // ===========================================================
    // Methods
    // ===========================================================

    // ===========================================================
    // Inner and Anonymous Classes
    // ===========================================================
}
