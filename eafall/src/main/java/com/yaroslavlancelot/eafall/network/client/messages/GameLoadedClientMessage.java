package com.yaroslavlancelot.eafall.network.client.messages;

import com.yaroslavlancelot.eafall.network.client.messages.constants.ClientMessagesConstants;

import org.andengine.extension.multiplayer.adt.message.client.ClientMessage;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * Created by Merle on 17-01-2015.
 */
public class GameLoadedClientMessage extends ClientMessage implements ClientMessagesConstants {
    @Override
    protected void onReadTransmissionData(DataInputStream pDataInputStream) throws IOException {
    }

    @Override
    protected void onWriteTransmissionData(DataOutputStream pDataOutputStream) throws IOException {
    }

    @Override
    public short getFlag() {
        return GAME_LOADED;
    }
}
