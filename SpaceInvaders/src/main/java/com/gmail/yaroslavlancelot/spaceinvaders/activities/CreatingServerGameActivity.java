package com.gmail.yaroslavlancelot.spaceinvaders.activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import com.gmail.yaroslavlancelot.spaceinvaders.R;
import com.gmail.yaroslavlancelot.spaceinvaders.network.discovery.SocketDiscoveryServer;
import org.andengine.extension.multiplayer.protocol.util.WifiUtils;

public class CreatingServerGameActivity extends Activity {
    public static final int FOR_CONVERT_IP = 256;
    private SocketDiscoveryServer mSocketDiscoveryServer;
    private byte[] mServerIp;
    private TextView mServerIpTextView;

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
        initDiscoveryServer();
        updateIpValue();
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopDiscoveryServer();
        mServerIpTextView.setVisibility(View.INVISIBLE);
    }

    @Override
    protected void onStop() {
        super.onStop();
        stopDiscoveryServer();
    }

    private void stopDiscoveryServer() {
        if (mServerIp == null) return;
        mSocketDiscoveryServer.terminate();
        mServerIp = null;
    }

    private void initDiscoveryServer() {
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
    }
}