package com.yaroslavlancelot.eafall.network.server.messages;

import com.yaroslavlancelot.eafall.network.server.messages.constants.ServerMessagesConstants;

import org.andengine.extension.multiplayer.adt.message.server.ServerMessage;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * Created by Merle on 16-01-2015.
 */
public class GameStartedServerMessage extends ServerMessage implements ServerMessagesConstants {

    @Override
    protected void onReadTransmissionData(DataInputStream pDataInputStream) throws IOException {
    }

    @Override
    protected void onWriteTransmissionData(DataOutputStream pDataOutputStream) throws IOException {
    }

    @Override
    public short getFlag() {
        return GAME_STARTED;
    }
}
