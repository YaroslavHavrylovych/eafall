package com.gmail.yaroslavlancelot.spaceinvaders.network.adt.messages.server;

import com.gmail.yaroslavlancelot.spaceinvaders.network.util.constants.PongConstants;
import org.andengine.extension.multiplayer.protocol.adt.message.server.ServerMessage;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;


/**
 * (c) 2010 Nicolas Gramlich 
 * (c) 2011 Zynga Inc.
 * 
 * @author Nicolas Gramlich
 * @since 02:02:12 - 01.03.2011
 */
public class UpdateScoreServerMessage extends ServerMessage implements PongConstants {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	public int mPaddleID;
	public int mScore;

	// ===========================================================
	// Constructors
	// ===========================================================

	public UpdateScoreServerMessage() {

	}

	public UpdateScoreServerMessage(final int pPaddleID, final int pScore) {
		this.mPaddleID = pPaddleID;
		this.mScore = pScore;
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	public void set(final int pPaddleID, final int pScore) {
		this.mPaddleID = pPaddleID;
		this.mScore = pScore;
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	public short getFlag() {
		return FLAG_MESSAGE_SERVER_UPDATE_SCORE;
	}

	@Override
	protected void onReadTransmissionData(DataInputStream pDataInputStream) throws IOException {
		this.mPaddleID = pDataInputStream.readInt();
		this.mScore = pDataInputStream.readInt();
	}

	@Override
	protected void onWriteTransmissionData(final DataOutputStream pDataOutputStream) throws IOException {
		pDataOutputStream.writeInt(this.mPaddleID);
		pDataOutputStream.writeInt(this.mScore);
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}