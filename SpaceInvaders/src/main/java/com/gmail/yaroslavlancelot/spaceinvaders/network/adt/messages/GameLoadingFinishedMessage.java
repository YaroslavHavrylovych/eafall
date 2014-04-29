package com.gmail.yaroslavlancelot.spaceinvaders.network.adt.messages;

import com.gmail.yaroslavlancelot.spaceinvaders.network.MessagesConstants;
import org.andengine.extension.multiplayer.protocol.adt.message.Message;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * Message
 */
public class GameLoadingFinishedMessage extends Message implements MessagesConstants {
    public GameLoadingFinishedMessage() {
    }

    @Override
    public short getFlag() {
        return FLAG_MESSAGE_GAME_LOADING_FINISHED;
    }

    @Override
    protected void onReadTransmissionData(final DataInputStream pDataInputStream) throws IOException {
    }

    @Override
    protected void onWriteTransmissionData(final DataOutputStream pDataOutputStream) throws IOException {
    }
}
