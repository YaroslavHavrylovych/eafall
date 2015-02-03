package com.gmail.yaroslavlancelot.eafall.network.server.messages;

import com.gmail.yaroslavlancelot.eafall.network.server.messages.constants.ServerMessagesConstants;

import org.andengine.extension.multiplayer.protocol.adt.message.server.ServerMessage;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class GameObjectHealthChangedServerMessage extends ServerMessage implements ServerMessagesConstants {
    private int mObjectHealth;
    private long mGameObjectUniqueId;

    @Deprecated
    public GameObjectHealthChangedServerMessage() {
    }

    public GameObjectHealthChangedServerMessage(long gameObjectUniqueId, int health) {
        mGameObjectUniqueId = gameObjectUniqueId;
        mObjectHealth = health;
    }

    public long getGameObjectUniqueId() {
        return mGameObjectUniqueId;
    }

    public int getObjectHealth() {
        return mObjectHealth;
    }

    @Override
    protected void onReadTransmissionData(DataInputStream pDataInputStream) throws IOException {
        mGameObjectUniqueId = pDataInputStream.readLong();
        mObjectHealth = pDataInputStream.readInt();
    }

    @Override
    protected void onWriteTransmissionData(DataOutputStream pDataOutputStream) throws IOException {
        pDataOutputStream.writeLong(mGameObjectUniqueId);
        pDataOutputStream.writeInt(mObjectHealth);
    }

    @Override
    public short getFlag() {
        return GAME_OBJECT_HEALTH_CHANGED;
    }
}
