package com.gmail.yaroslavlancelot.eafall.network.server.messages;

import com.gmail.yaroslavlancelot.eafall.network.server.messages.constants.ServerMessagesConstants;

import org.andengine.extension.multiplayer.adt.message.server.ServerMessage;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class StartingGameServerMessage extends ServerMessage implements ServerMessagesConstants {
    public StartingGameServerMessage() {
    }

    @Override
    public short getFlag() {
        return STARTING_GAME;
    }

    @Override
    protected void onReadTransmissionData(final DataInputStream pDataInputStream) throws IOException {
    }

    @Override
    protected void onWriteTransmissionData(final DataOutputStream pDataOutputStream) throws IOException {
    }
}
