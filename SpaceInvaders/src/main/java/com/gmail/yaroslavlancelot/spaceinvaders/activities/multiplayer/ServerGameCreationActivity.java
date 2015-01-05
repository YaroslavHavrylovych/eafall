package com.gmail.yaroslavlancelot.spaceinvaders.activities.multiplayer;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.gmail.yaroslavlancelot.spaceinvaders.R;
import com.gmail.yaroslavlancelot.spaceinvaders.activities.BaseNonGameActivity;
import com.gmail.yaroslavlancelot.spaceinvaders.activities.ingame.ServerGameActivity;
import com.gmail.yaroslavlancelot.spaceinvaders.alliances.imperials.Imperials;
import com.gmail.yaroslavlancelot.spaceinvaders.alliances.rebels.Rebels;
import com.gmail.yaroslavlancelot.spaceinvaders.constants.StringsAndPathUtils;
import com.gmail.yaroslavlancelot.spaceinvaders.constants.TeamControlBehaviourType;
import com.gmail.yaroslavlancelot.spaceinvaders.network.GameSocketServer;
import com.gmail.yaroslavlancelot.spaceinvaders.network.adt.messages.server.StartingGameServerMessage;
import com.gmail.yaroslavlancelot.spaceinvaders.network.callbacks.server.PreGameStartServer;
import com.gmail.yaroslavlancelot.spaceinvaders.network.connector.ClientConnectorListener;
import com.gmail.yaroslavlancelot.spaceinvaders.network.discovery.SocketDiscoveryServer;
import com.gmail.yaroslavlancelot.spaceinvaders.utils.LoggerHelper;

import org.andengine.extension.multiplayer.protocol.util.WifiUtils;

import java.io.IOException;

public class ServerGameCreationActivity extends BaseNonGameActivity implements PreGameStartServer {
    public final static String TAG = ServerGameCreationActivity.class.getCanonicalName();
    public static final int FOR_CONVERT_IP = 256;
    private SocketDiscoveryServer mSocketDiscoveryServer;
    private byte[] mServerIp;
    private TextView mServerIpTextView;
    private GameSocketServer mGameSocketServer;
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
        initStartGameButton(findViewById(R.id.start_game));
    }

    private void initBackButton(View exitButton) {
        if (exitButton == null) return;
        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                ServerGameCreationActivity.this.finish();
            }
        });
    }

    private void initNoOpponentsTextView(View view) {
        if (view == null) return;
        mNoOpponentsTextView = (TextView) view;
    }

    private void initClientConnectedTextView(View view) {
        if (view == null) return;
        mClientConnectedTextView = (TextView) view;
    }

    private void initClientIpTextView(View view) {
        if (view == null) return;
        mClientIpTextView = (TextView) view;
    }

    private void initStartGameButton(View startGameButton) {
        if (startGameButton == null) return;
        startGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                GameSocketServer.setGameSocketServer(mGameSocketServer);
                try {
                    mGameSocketServer.sendBroadcastServerMessage(new StartingGameServerMessage());
                } catch (IOException ioException) {
                    LoggerHelper.printErrorMessage(TAG, ioException.getMessage());
                    return;
                }
                mGameSocketServer.removePreGameStartCallback(ServerGameCreationActivity.this);
                mGameSocketServer = null;
                Intent startServerIntent = new Intent(ServerGameCreationActivity.this, ServerGameActivity.class);
                startServerIntent.
                        putExtra(StringsAndPathUtils.SECOND_TEAM_CONTROL_BEHAVIOUR_TYPE, TeamControlBehaviourType.REMOTE_CONTROL_ON_SERVER_SIDE.toString()).
                        putExtra(StringsAndPathUtils.SECOND_TEAM_ALLIANCE, Rebels.ALLIANCE_NAME).
                        putExtra(StringsAndPathUtils.FIRST_TEAM_ALLIANCE, Imperials.ALLIANCE_NAME).
                        putExtra(StringsAndPathUtils.FIRST_TEAM_CONTROL_BEHAVIOUR_TYPE, TeamControlBehaviourType.USER_CONTROL_ON_SERVER_SIDE.toString());
                startActivity(startServerIntent);
            }
        });
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

    private void stopServer() {
        if (mGameSocketServer != null) {
            mGameSocketServer.removePreGameStartCallback(ServerGameCreationActivity.this);
            mGameSocketServer.terminate();
            mGameSocketServer = null;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        initServer();
        initDiscoveryServer();
        updateIpValue();
    }

    private void initServer() {
        mGameSocketServer = new GameSocketServer(SocketDiscoveryServer.SERVER_PORT, new ClientConnectorListener());
        mGameSocketServer.addPreGameStartCallback(ServerGameCreationActivity.this);
        mGameSocketServer.start();
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

    @Override
    public void clientConnectionEstablished(final String clientIp) {
        mNoOpponentsTextView.post(new Runnable() {
            @Override
            public void run() {
                mNoOpponentsTextView.setVisibility(View.GONE);
                mClientConnectedTextView.setVisibility(View.VISIBLE);
                mClientIpTextView.setText(mGameSocketServer.getClientIp());
                mClientIpTextView.setVisibility(View.VISIBLE);
            }
        });
    }
}