package org.andengine.extension.multiplayer.server.connector;

import java.io.IOException;

import org.andengine.extension.multiplayer.adt.message.server.IServerMessage;
import org.andengine.extension.multiplayer.server.IClientMessageReader;
import org.andengine.extension.multiplayer.shared.BluetoothSocketConnection;
import org.andengine.extension.multiplayer.util.MessagePool;
import org.andengine.util.Bluetooth;
import org.andengine.util.debug.Debug;
import org.andengine.util.exception.BluetoothException;

/**
 * (c) 2010 Nicolas Gramlich
 * (c) 2011 Zynga Inc.
 *
 * @author Nicolas Gramlich
 * @since 15:44:42 - 04.03.2011
 */
public class BluetoothSocketConnectionClientConnector extends ClientConnector<BluetoothSocketConnection> {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	// ===========================================================
	// Constructors
	// ===========================================================

	public BluetoothSocketConnectionClientConnector(final BluetoothSocketConnection pBluetoothSocketConnection) throws IOException, BluetoothException {
		super(pBluetoothSocketConnection);

		if (Bluetooth.isSupportedByAndroidVersion() == false) {
			throw new BluetoothException();
		}
	}

	public BluetoothSocketConnectionClientConnector(final BluetoothSocketConnection pBluetoothSocketConnection, final IClientMessageReader<BluetoothSocketConnection> pClientMessageReader) throws IOException, BluetoothException {
		super(pBluetoothSocketConnection, pClientMessageReader);

		if (Bluetooth.isSupportedByAndroidVersion() == false) {
			throw new BluetoothException();
		}
	}

	public BluetoothSocketConnectionClientConnector(final BluetoothSocketConnection pBluetoothSocketConnection, final MessagePool<IServerMessage> pServerMessagePool) throws IOException, BluetoothException {
		super(pBluetoothSocketConnection, pServerMessagePool);

		if (Bluetooth.isSupportedByAndroidVersion() == false) {
			throw new BluetoothException();
		}
	}

	public BluetoothSocketConnectionClientConnector(final BluetoothSocketConnection pBluetoothSocketConnection, final IClientMessageReader<BluetoothSocketConnection> pClientMessageReader, final MessagePool<IServerMessage> pServerMessagePool) throws IOException, BluetoothException {
		super(pBluetoothSocketConnection, pClientMessageReader, pServerMessagePool);

		if (Bluetooth.isSupportedByAndroidVersion() == false) {
			throw new BluetoothException();
		}
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================

	public static interface IBluetoothSocketConnectionClientConnectorListener extends IClientConnectorListener<BluetoothSocketConnection> {

	}

	public static class DefaultBluetoothSocketClientConnectorListener implements IBluetoothSocketConnectionClientConnectorListener {
		@Override
		public void onStarted(ClientConnector<BluetoothSocketConnection> pClientConnector) {
			Debug.d("Accepted Client-Connection from: '" + pClientConnector.getConnection().getBluetoothSocket().getRemoteDevice().getAddress());
		}

		@Override
		public void onTerminated(ClientConnector<BluetoothSocketConnection> pClientConnector) {
			Debug.d("Closed Client-Connection from: '" + pClientConnector.getConnection().getBluetoothSocket().getRemoteDevice().getAddress());
		}
	}
}
