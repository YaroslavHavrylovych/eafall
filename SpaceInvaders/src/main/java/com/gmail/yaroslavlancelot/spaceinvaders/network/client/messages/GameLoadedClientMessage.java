package com.gmail.yaroslavlancelot.spaceinvaders.network.client.messages;

import com.gmail.yaroslavlancelot.spaceinvaders.network.client.messages.constants.ClientMessagesConstants;

import org.andengine.extension.multiplayer.protocol.adt.message.client.ClientMessage;

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
