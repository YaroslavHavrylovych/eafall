package com.yaroslavlancelot.eafall.network.server.messages;

import com.yaroslavlancelot.eafall.network.server.messages.constants.ServerMessagesConstants;

import org.andengine.extension.multiplayer.adt.message.server.ServerMessage;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class WaitingForPlayersServerMessage extends ServerMessage implements ServerMessagesConstants {
    public WaitingForPlayersServerMessage() {
    }

    @Override
    public short getFlag() {
        return WAITING_FOR_PLAYERS;
    }

    @Override
    protected void onReadTransmissionData(final DataInputStream pDataInputStream) throws IOException {
    }

    @Override
    protected void onWriteTransmissionData(final DataOutputStream pDataOutputStream) throws IOException {
    }
}