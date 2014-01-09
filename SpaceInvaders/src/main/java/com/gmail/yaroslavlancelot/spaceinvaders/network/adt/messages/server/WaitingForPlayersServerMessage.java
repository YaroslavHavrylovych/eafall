package com.gmail.yaroslavlancelot.spaceinvaders.network.adt.messages.server;

import com.gmail.yaroslavlancelot.spaceinvaders.network.MessagesConstants;
import com.gmail.yaroslavlancelot.spaceinvaders.network.example.common.ICommonMessages;
import org.andengine.extension.multiplayer.protocol.adt.message.server.ServerMessage;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class WaitingForPlayersServerMessage extends ServerMessage implements MessagesConstants {
    public WaitingForPlayersServerMessage() {
    }

    @Override
    public short getFlag() {
        return FLAG_MESSAGE_SERVER_WAITING_FOR_PLAYERS;
    }

    @Override
    protected void onReadTransmissionData(final DataInputStream pDataInputStream) throws IOException {
    }

    @Override
    protected void onWriteTransmissionData(final DataOutputStream pDataOutputStream) throws IOException {
    }
}
