package com.gmail.yaroslavlancelot.spaceinvaders.network.server.discovery;

import org.andengine.extension.multiplayer.protocol.server.SocketServerDiscoveryServer;
import org.andengine.extension.multiplayer.protocol.shared.IDiscoveryData;

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
