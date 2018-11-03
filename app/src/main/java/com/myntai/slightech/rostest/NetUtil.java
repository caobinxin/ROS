package com.myntai.slightech.rostest;

import android.util.Log;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Collections;
import java.util.Enumeration;

public class NetUtil {

    public static String getEthernetIpAddress() {
        loopPrintInterface();
        String host = null;
        try {
            for (NetworkInterface networkInterface : Collections.list(NetworkInterface.getNetworkInterfaces())) {
                String interfaceName = networkInterface.getName();
                if (interfaceName != null && interfaceName.contains("eth")) {
                    host = getNonLoopbackIpv4ForNetworkInterface(networkInterface);
                    Log.i("getEthernetIpAddress: %s", host);
                }
            }
        } catch (SocketException ignored) {
        }
        return host;
    }

    public static String getWifiIpAddress() {
        loopPrintInterface();
        String host = null;
        try {
            for (NetworkInterface networkInterface : Collections.list(NetworkInterface.getNetworkInterfaces())) {
                String interfaceName = networkInterface.getName();
                if (interfaceName != null && interfaceName.contains("wlan")) {
                    host = getNonLoopbackIpv4ForNetworkInterface(networkInterface);
                    Log.i("getWifiIpAddress: %s", host);
                }
            }
        } catch (SocketException ignored) {
        }
        return host;
    }

    private static String getNonLoopbackIpv4ForNetworkInterface(NetworkInterface networkInterface) {
        Enumeration<InetAddress> internetAddresses = networkInterface.getInetAddresses();
        InetAddress address = null;
        while (internetAddresses.hasMoreElements()) {
            address = internetAddresses.nextElement();
            //is ipv4 and not loopback
            if (address.getAddress().length == 4 && !address.isLoopbackAddress()) {
                break;
            } else {
                address = null;
            }
        }
        if (address != null) {
            return address.getHostAddress();
        }
        return null;
    }

    private static void loopPrintInterface() {
        try {
            for (NetworkInterface networkInterface : Collections.list(NetworkInterface.getNetworkInterfaces())) {
//                Log.i(networkInterface.getName());
            }
        } catch (SocketException ignored) {
        }
    }
}
