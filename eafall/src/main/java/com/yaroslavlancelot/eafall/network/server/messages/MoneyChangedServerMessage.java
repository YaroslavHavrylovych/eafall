package com.yaroslavlancelot.eafall.network.server.messages;

import com.yaroslavlancelot.eafall.network.server.messages.constants.ServerMessagesConstants;

import org.andengine.extension.multiplayer.adt.message.server.ServerMessage;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class MoneyChangedServerMessage extends ServerMessage implements ServerMessagesConstants {
    private String mPlayerName;
    private int mMoney;

    @Deprecated
    public MoneyChangedServerMessage() {
    }

    public MoneyChangedServerMessage(String playerName, int money) {
        mPlayerName = playerName;
        mMoney = money;
    }

    @Override
    protected void onReadTransmissionData(DataInputStream pDataInputStream) throws IOException {
        mMoney = pDataInputStream.readInt();
        mPlayerName = pDataInputStream.readUTF();
    }

    @Override
    protected void onWriteTransmissionData(DataOutputStream pDataOutputStream) throws IOException {
        pDataOutputStream.writeInt(mMoney);
        pDataOutputStream.writeUTF(mPlayerName);
    }

    @Override
    public short getFlag() {
        return MONEY_CHANGED;
    }

    public String getPlayerName() {
        return mPlayerName;
    }

    public int getMoney() {
        return mMoney;
    }
}
