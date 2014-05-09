package com.gmail.yaroslavlancelot.spaceinvaders.network.adt.messages.server;

import com.gmail.yaroslavlancelot.spaceinvaders.network.MessagesConstants;

import org.andengine.extension.multiplayer.protocol.adt.message.server.ServerMessage;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class MoneyChangedServerMessage extends ServerMessage implements MessagesConstants {
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
        return FLAG_MESSAGE_SERVER_MONEY_CHANGED;
    }

    public String getTeamName() {
        return mTeamName;
    }

    public int getMoney() {
        return mMoney;
    }
}
