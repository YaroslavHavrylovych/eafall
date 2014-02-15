package com.gmail.yaroslavlancelot.spaceinvaders.activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import com.gmail.yaroslavlancelot.spaceinvaders.R;
import com.gmail.yaroslavlancelot.spaceinvaders.network.GameSocketServer;
import com.gmail.yaroslavlancelot.spaceinvaders.network.callbacks.PreGameStartCallbacksFromClient;
import com.gmail.yaroslavlancelot.spaceinvaders.network.connector.ClientConnectorListener;
import com.gmail.yaroslavlancelot.spaceinvaders.network.discovery.SocketDiscoveryServer;
import com.gmail.yaroslavlancelot.spaceinvaders.utils.LoggerHelper;
import org.andengine.extension.multiplayer.protocol.util.WifiUtils;

public class CreatingServerGameActivity extends Activity implements PreGameStartCallbacksFromClient {
    public final static String TAG = CreatingServerGameActivity.class.getCanonicalName();
    public static final int FOR_CONVERT_IP = 256;
    private SocketDiscoveryServer mSocketDiscoveryServer;
    private byte[] mServerIp;
    private TextView mServerIpTextView;
    private GameSocketServer mSocketServer;
    private TextView mNoOpponentsTextView;
    private TextView mClientConnectedTextView;
    private TextView mClientIpTextView;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.creating_server_layout);
        initBackButton(findViewById(R.id.back));
        mServerIpTextView = (TextView) findViewById(R.id.your_ip_address_value);
        initNoOpponentsTextView(findViewById(R.id.no_opponents_text_view));
        initClientConnectedTextView(findViewById(R.id.client_connected_text_view));
        initClientIpTextView(findViewById(R.id.client_ip_text_view));
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
        String ipValue = getConvertedServerIp(mServerIp);
        mServerIpTextView.setText(ipValue);
        mServerIpTextView.setVisibility(View.VISIBLE);
        LoggerHelper.printDebugMessage(TAG, "server ip = " + ipValue);
    }

    private String getConvertedServerIp(byte[] serverIp) {
        String ipValue = "";
        for (int i = 0; i < serverIp.length; i++) {
            ipValue += serverIp[i] >= 0 ? serverIp[i] : FOR_CONVERT_IP + serverIp[i];
            if (i < serverIp.length - 1) {
                ipValue += getString(R.string.ip_address_separator);
            }
        }
        return ipValue;
    }

    private void initServer() {
        mSocketServer = new GameSocketServer(SocketDiscoveryServer.SERVER_PORT, new ClientConnectorListener());
        mSocketServer.addPreGameStartCallbacks(CreatingServerGameActivity.this);
        mSocketServer.start();
    }

    private void stopServer() {
        if (mSocketServer != null) {
            mSocketServer.removePreGameStartCallbacks(CreatingServerGameActivity.this);
            mSocketServer.terminate();
            mSocketServer = null;
        }
    }

    private void initNoOpponentsTextView(View view) {
        if (view == null) return;
        mNoOpponentsTextView = (TextView) view;
    }

    @Override
    public void clientConnectionEstablished(final String clientIp) {
        mNoOpponentsTextView.post(new Runnable() {
            @Override
            public void run() {
                mNoOpponentsTextView.setVisibility(View.GONE);
                mClientConnectedTextView.setVisibility(View.VISIBLE);
                mClientIpTextView.setText(mSocketServer.getClientIp());
                mClientIpTextView.setVisibility(View.VISIBLE);
            }
        });
    }

    private void initClientConnectedTextView(View view) {
        if (view == null) return;
        mClientConnectedTextView = (TextView) view;
    }

    private void initClientIpTextView(View view) {
        if (view == null) return;
        mClientIpTextView = (TextView) view;
    }
}