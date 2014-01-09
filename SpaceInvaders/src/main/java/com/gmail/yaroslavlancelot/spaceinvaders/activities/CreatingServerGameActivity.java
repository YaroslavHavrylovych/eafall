package com.gmail.yaroslavlancelot.spaceinvaders.activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import com.gmail.yaroslavlancelot.spaceinvaders.R;
import com.gmail.yaroslavlancelot.spaceinvaders.network.GameSocketServer;
import com.gmail.yaroslavlancelot.spaceinvaders.network.adt.messages.server.WaitingForPlayersServerMessage;
import com.gmail.yaroslavlancelot.spaceinvaders.network.discovery.SocketDiscoveryServer;
import com.gmail.yaroslavlancelot.spaceinvaders.utils.LoggerHelper;
import org.andengine.extension.multiplayer.protocol.server.SocketServerDiscoveryServer;
import org.andengine.extension.multiplayer.protocol.server.connector.ClientConnector;
import org.andengine.extension.multiplayer.protocol.server.connector.SocketConnectionClientConnector;
import org.andengine.extension.multiplayer.protocol.shared.IDiscoveryData;
import org.andengine.extension.multiplayer.protocol.shared.SocketConnection;
import org.andengine.extension.multiplayer.protocol.util.WifiUtils;

import java.io.IOException;
import java.net.InetAddress;

public class CreatingServerGameActivity extends Activity {
    public final static String TAG = CreatingServerGameActivity.class.getCanonicalName();
    public static final int FOR_CONVERT_IP = 256;
    private SocketDiscoveryServer mSocketDiscoveryServer;
    private byte[] mServerIp;
    private TextView mServerIpTextView;
    private GameSocketServer mSocketServer;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.creating_server_layout);
        initBackButton(findViewById(R.id.back));
        mServerIpTextView = (TextView) findViewById(R.id.your_ip_address_value);
    }

    private void initBackButton(View exitButton) {
        if (exitButton == null) return;
        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                CreatingServerGameActivity.this.finish();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        initServer();
        initDiscoveryServer();
        updateIpValue();
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopDiscoveryServer();
        stopServer();
        mServerIpTextView.setVisibility(View.INVISIBLE);
    }

    @Override
    protected void onStop() {
        super.onStop();
        stopDiscoveryServer();
        stopServer();
    }

    private void stopDiscoveryServer() {
        if (mSocketDiscoveryServer != null) {
            mSocketDiscoveryServer.terminate();
            mSocketDiscoveryServer = null;
        }
        if (mServerIp != null) {
            mServerIp = null;
        }
    }

    private void initDiscoveryServer() {
        LoggerHelper.printDebugMessage(TAG, "init discovery server");
        mServerIp = WifiUtils.getWifiIPv4AddressRaw(this);

        mSocketDiscoveryServer = new SocketDiscoveryServer(mServerIp);
        mSocketDiscoveryServer.start();
    }

    private void updateIpValue() {
        String ipValue = "";
        for (int i = 0; i < mServerIp.length; i++) {
            ipValue += mServerIp[i] >= 0 ? mServerIp[i] : FOR_CONVERT_IP + mServerIp[i];
            if (i < mServerIp.length - 1) {
                ipValue += getString(R.string.ip_address_separator);
            }
        }
        mServerIpTextView.setText(ipValue);
        mServerIpTextView.setVisibility(View.VISIBLE);
        LoggerHelper.printDebugMessage(TAG, "server ip = " + ipValue);
    }

    private void initServer() {
        mSocketServer = new GameSocketServer(SocketDiscoveryServer.SERVER_PORT, new ClientConnectorListener());
        mSocketServer.start();
    }

    private void stopServer() {
        if (mSocketServer != null) {
            mSocketServer.terminate();
            mSocketServer = null;
        }
    }

    private class ClientConnectorListener implements SocketConnectionClientConnector.ISocketConnectionClientConnectorListener {
        @Override
        public void onStarted(final ClientConnector<SocketConnection> pClientConnector) {
            LoggerHelper.printInformationMessage(TAG, "SERVER: Client connected: " + pClientConnector.getConnection().getSocket().getInetAddress().getHostAddress());
            try {
                mSocketServer.sendBroadcastServerMessage(new WaitingForPlayersServerMessage());
            } catch (IOException e) {
                LoggerHelper.printErrorMessage(TAG, "Error while sending message to client: " + e.getMessage());
            }
        }

        @Override
        public void onTerminated(final ClientConnector<SocketConnection> pClientConnector) {
            LoggerHelper.printInformationMessage(TAG, "SERVER: Client disconnected: " + pClientConnector.getConnection().getSocket().getInetAddress().getHostAddress());
        }
    }
}