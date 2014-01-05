package com.gmail.yaroslavlancelot.spaceinvaders.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import com.gmail.yaroslavlancelot.spaceinvaders.R;
import com.gmail.yaroslavlancelot.spaceinvaders.network.discovery.SocketDiscoveryClient;
import org.andengine.extension.multiplayer.protocol.client.SocketServerDiscoveryClient;
import org.andengine.extension.multiplayer.protocol.exception.WifiException;
import org.andengine.extension.multiplayer.protocol.shared.IDiscoveryData;
import org.andengine.extension.multiplayer.protocol.util.WifiUtils;

import java.net.UnknownHostException;

public class NetworkGameActivity extends Activity {
    private SocketServerDiscoveryClient mSocketServerDiscoveryClient;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.network_game_layout);
        initBackButton(findViewById(R.id.back));
        initGamesList((ListView) findViewById(R.id.games_list_list_view));
        initCreateServerButton(findViewById(R.id.create_game));
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

    @Override
    protected void onPause() {
        super.onPause();
        stopSocketDiscoveryClient();
    }

    @Override
    protected void onStop() {
        super.onStop();
        stopSocketDiscoveryClient();
    }

    private void stopSocketDiscoveryClient() {
        if (mSocketServerDiscoveryClient != null) {
            mSocketServerDiscoveryClient.terminate();
            mSocketServerDiscoveryClient = null;
        }
    }

    private void initSocketDiscoveryClient() throws WifiException, UnknownHostException {
        mSocketServerDiscoveryClient = new SocketServerDiscoveryClient(WifiUtils.getBroadcastIPAddressRaw(this),
                IDiscoveryData.DefaultDiscoveryData.class, new SocketDiscoveryClient());
    }

    private void initGamesList(ListView listView) {
        if (listView == null) return;
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
}
