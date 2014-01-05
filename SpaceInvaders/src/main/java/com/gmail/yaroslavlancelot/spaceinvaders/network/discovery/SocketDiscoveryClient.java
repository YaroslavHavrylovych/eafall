package com.gmail.yaroslavlancelot.spaceinvaders.network.discovery;

import com.gmail.yaroslavlancelot.spaceinvaders.utils.LoggerHelper;
import org.andengine.extension.multiplayer.protocol.client.SocketServerDiscoveryClient;
import org.andengine.extension.multiplayer.protocol.shared.IDiscoveryData;
import org.andengine.extension.multiplayer.protocol.util.IPUtils;

import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

public class SocketDiscoveryClient implements SocketServerDiscoveryClient.ISocketServerDiscoveryClientListener {
    public static final String TAG = SocketDiscoveryClient.class.getCanonicalName();

    @Override
    public void onDiscovery(final SocketServerDiscoveryClient pSocketServerDiscoveryClient, final IDiscoveryData pDiscoveryData) {
        IDiscoveryData.DefaultDiscoveryData discoveryData = (IDiscoveryData.DefaultDiscoveryData) pDiscoveryData;
        String ipAddressAsString = "?????????";
        try {
            ipAddressAsString = IPUtils.ipAddressToString(discoveryData.getServerIP());
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

        LoggerHelper.printInformationMessage(TAG, "DiscoveryClient: Server discovered at: " + ipAddressAsString + ":" + discoveryData.getServerPort());
    }

    @Override
    public void onTimeout(final SocketServerDiscoveryClient pSocketServerDiscoveryClient, final SocketTimeoutException pSocketTimeoutException) {
        LoggerHelper.printInformationMessage(TAG, "DiscoveryClient: Timeout: " + pSocketTimeoutException);
    }

    @Override
    public void onException(final SocketServerDiscoveryClient pSocketServerDiscoveryClient, final Throwable pThrowable) {
        LoggerHelper.printInformationMessage(TAG, "DiscoveryClient: Exception: " + pThrowable);
    }
}
