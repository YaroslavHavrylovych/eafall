package com.gmail.yaroslavlancelot.spaceinvaders.activities;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import com.gmail.yaroslavlancelot.spaceinvaders.R;
import com.gmail.yaroslavlancelot.spaceinvaders.network.GameServerConnector;
import com.gmail.yaroslavlancelot.spaceinvaders.network.callbacks.PreGameStartCallback;
import com.gmail.yaroslavlancelot.spaceinvaders.network.discovery.SocketDiscoveryServer;
import com.gmail.yaroslavlancelot.spaceinvaders.utils.LoggerHelper;
import org.andengine.extension.multiplayer.protocol.client.SocketServerDiscoveryClient;
import org.andengine.extension.multiplayer.protocol.client.connector.ServerConnector;
import org.andengine.extension.multiplayer.protocol.client.connector.SocketConnectionServerConnector;
import org.andengine.extension.multiplayer.protocol.exception.WifiException;
import org.andengine.extension.multiplayer.protocol.shared.IDiscoveryData;
import org.andengine.extension.multiplayer.protocol.shared.SocketConnection;
import org.andengine.extension.multiplayer.protocol.util.IPUtils;
import org.andengine.extension.multiplayer.protocol.util.WifiUtils;

import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

public class NetworkGameActivity extends Activity implements
        SocketServerDiscoveryClient.ISocketServerDiscoveryClientListener,
        SocketConnectionServerConnector.ISocketConnectionServerConnectorListener,
        PreGameStartCallback {
    public static final String TAG = NetworkGameActivity.class.getCanonicalName();
    private SocketServerDiscoveryClient mSocketServerDiscoveryClient;
    private ListView mServersListView;
    private ArrayAdapter<String> mArrayAdapter;
    private List<GameServerConnector> mServerConnectorList;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.network_game_layout);
        initBackButton(findViewById(R.id.back));
        initGamesList((ListView) findViewById(R.id.games_list_list_view));
        initCreateServerButton(findViewById(R.id.create_game));
        initDirectIpButton(findViewById(R.id.direct_ip));
    }

    private void initCreateServerButton(View createServerButton) {
        if (createServerButton == null) return;
        createServerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                Intent singleGameIntent = new Intent(NetworkGameActivity.this, CreatingServerGameActivity.class);
                startActivity(singleGameIntent);
            }
        });
    }

    private void initDirectIpButton(View directIpButton) {
        if (directIpButton == null) return;
        directIpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                final Dialog directIpDialog = new Dialog(NetworkGameActivity.this);
                directIpDialog.setContentView(R.layout.direct_ip_layout);
                directIpDialog.findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(final View v) {
                        directIpDialog.dismiss();
                    }
                });

                directIpDialog.findViewById(R.id.connect).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(final View v) {
                        EditText ipAddressEditText = (EditText) directIpDialog.findViewById(R.id.ip_address_edit_text);
                        String ipAddress = ipAddressEditText.getText().toString();

                        initClient(ipAddress, SocketDiscoveryServer.SERVER_PORT);

                        directIpDialog.dismiss();
                    }
                });

                directIpDialog.show();
            }
        });
    }

    private void initBackButton(View exitButton) {
        if (exitButton == null) return;
        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                NetworkGameActivity.this.finish();
            }
        });
    }

    private void initGamesList(ListView listView) {
        if (listView == null) return;
        mServersListView = listView;
        mArrayAdapter = new ArrayAdapter<String>(this, R.layout.server_item_view, R.id.server_name);
        mServersListView.setAdapter(mArrayAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            initSocketDiscoveryClient();
        } catch (WifiException e) {
            e.printStackTrace();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

        mSocketServerDiscoveryClient.discoverAsync();
    }

    private void initSocketDiscoveryClient() throws WifiException, UnknownHostException {
        mServerConnectorList = new ArrayList<GameServerConnector>(5);
        //ListView
        mServersListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(final AdapterView<?> parent, final View view, final int position, final long id) {
                //TODO start game
            }
        });
        //create SocketServerDiscoveryClient
        mSocketServerDiscoveryClient = new SocketServerDiscoveryClient(WifiUtils.getBroadcastIPAddressRaw(this),
                IDiscoveryData.DefaultDiscoveryData.class, this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopSocketDiscoveryClient();
    }

    private void stopSocketDiscoveryClient() {
        if (mSocketServerDiscoveryClient != null) {
            mSocketServerDiscoveryClient.terminate();
            mSocketServerDiscoveryClient = null;
            if (mServerConnectorList != null) {
                synchronized (mServerConnectorList) {
                    for (GameServerConnector serverConnector : mServerConnectorList) {
                        serverConnector.terminate();
                    }
                }
                mServerConnectorList = null;
            }
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        stopSocketDiscoveryClient();
    }

    @Override
    public void onTimeout(final SocketServerDiscoveryClient socketServerDiscoveryClient, final SocketTimeoutException socketTimeoutException) {
        LoggerHelper.printErrorMessage(TAG, "DiscoveryClient: Timeout: " + socketTimeoutException);
    }

    @Override
    public void onException(final SocketServerDiscoveryClient socketServerDiscoveryClient, final Throwable throwable) {
        LoggerHelper.printErrorMessage(TAG, "DiscoveryClient: Exception: " + throwable);
    }

    private void initClient(final String ipAddress, final int port) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    synchronized (mServerConnectorList) {
                        GameServerConnector serverConnector = new GameServerConnector(ipAddress, port, NetworkGameActivity.this);
                        serverConnector.addPreGameStartCallbacks(NetworkGameActivity.this);
                        serverConnector.start();
                        mServerConnectorList.add(serverConnector);
                    }
                } catch (final Throwable t) {
                    LoggerHelper.printErrorMessage(TAG, t.toString());
                }
            }
        }).start();
    }

    @Override
    public void onStarted(final ServerConnector<SocketConnection> serverConnector) {
        LoggerHelper.printInformationMessage(TAG, "CLIENT: Connected to server.");
    }

    @Override
    public void onTerminated(final ServerConnector<SocketConnection> serverConnector) {
        LoggerHelper.printInformationMessage(TAG, "CLIENT: Disconnected from Server.");
    }

    @Override
    public void gameStart(final String serverIP) {
        gameStop(serverIP);
    }

    @Override
    public void gameStop(final String serverIP) {
        synchronized (mServersListView) {
            mArrayAdapter.remove(serverIP);
        }
    }

    @Override
    public void gameWaitingForPlayers(final String serverIP) {
        mServersListView.post(new Runnable() {
            @Override
            public void run() {
                synchronized (mServersListView) {
                    mArrayAdapter.add(serverIP);
                    mArrayAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    @Override
    public void onDiscovery(final SocketServerDiscoveryClient pSocketServerDiscoveryClient, final IDiscoveryData pDiscoveryData) {
        IDiscoveryData.DefaultDiscoveryData discoveryData = (IDiscoveryData.DefaultDiscoveryData) pDiscoveryData;
        try {
            final String ipAddressAsString = IPUtils.ipAddressToString(discoveryData.getServerIP());
            LoggerHelper.printInformationMessage(TAG, "DiscoveryClient: Server discovered at: " + ipAddressAsString + ":" + discoveryData.getServerPort());
            initClient(ipAddressAsString, discoveryData.getServerPort());
        } catch (final UnknownHostException e) {
            LoggerHelper.printErrorMessage(TAG, "DiscoveryClient: IPException: " + e);
        }
    }
}
