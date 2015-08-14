package com.gmail.yaroslavlancelot.eafall.network.server.discovery;

import org.andengine.extension.multiplayer.server.SocketServerDiscoveryServer;
import org.andengine.extension.multiplayer.shared.IDiscoveryData;

public class SocketDiscoveryServer extends SocketServerDiscoveryServer {
    public static final int SERVER_PORT = 8899;
    private byte[] mServerIpAddress;

    public SocketDiscoveryServer(byte[] ipAddress) {
        mServerIpAddress = ipAddress;
    }

    @Override
    protected IDiscoveryData onCreateDiscoveryResponse() {
        return new IDiscoveryData.DefaultDiscoveryData(mServerIpAddress, SERVER_PORT);
    }
}
