package com.gmail.yaroslavlancelot.spaceinvaders.network.server.messages;

import com.gmail.yaroslavlancelot.spaceinvaders.network.server.messages.constants.ServerMessagesConstants;

import org.andengine.extension.multiplayer.protocol.adt.message.server.ServerMessage;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class MoneyChangedServerMessage extends ServerMessage implements ServerMessagesConstants {
    private String mTeamName;
    private int mMoney;

    @Deprecated
    public MoneyChangedServerMessage() {
    }

    public MoneyChangedServerMessage(String teamName, int money) {
        mTeamName = teamName;
        mMoney = money;
    }

    @Override
    protected void onReadTransmissionData(DataInputStream pDataInputStream) throws IOException {
        mMoney = pDataInputStream.readInt();
        mTeamName = pDataInputStream.readUTF();
    }

    @Override
    protected void onWriteTransmissionData(DataOutputStream pDataOutputStream) throws IOException {
        pDataOutputStream.writeInt(mMoney);
        pDataOutputStream.writeUTF(mTeamName);
    }

    @Override
    public short getFlag() {
        return MONEY_CHANGED;
    }

    public String getTeamName() {
        return mTeamName;
    }

    public int getMoney() {
        return mMoney;
    }
}
