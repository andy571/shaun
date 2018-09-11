package com.training.shaun.proxy;

import com.google.common.base.Preconditions;

/**
 * User: andy
 * Date: 2018/2/27
 * Description:
 */
public class ShProxyInfo {

    private String proxyIp;
    private int proxyPort;
    private String proxyUsername;
    private String proxyPassword;
    private ShProxyType proxyType;

    public ShProxyInfo(ShProxyType pType, String pIp, int pPort, String pUsername, String pPassword) {
        this.proxyType = pType;
        this.proxyIp = pIp;
        this.proxyPort = pPort;
        this.proxyUsername = pUsername;
        this.proxyPassword = pPassword;
    }

    public static ShProxyInfo noProxy() {
        return new ShProxyInfo(ShProxyType.NONE, null, 0, "", "");
    }

    public static ShProxyInfo httpProxy(final String pIpPort) {
        String[] i = pIpPort.split(":");
        Preconditions.checkArgument(i.length == 2, "args is not ip:port");

        String ip = i[0];
        int port = Integer.parseInt(i[1]);
        return httpProxy(ip, port);
    }

    public static ShProxyInfo httpProxy(final String pIp, final int pPort) {
        return new ShProxyInfo(ShProxyType.HTTP, pIp, pPort, "", "");
    }

    public static ShProxyInfo socks4Proxy(final String pIpPort) {
        String[] i = pIpPort.split(":");
        Preconditions.checkArgument(i.length == 2, "args is not ip:port");

        String ip = i[0];
        int port = Integer.parseInt(i[1]);
        return socks4Proxy(ip, port);
    }

    public static ShProxyInfo socks4Proxy(final String pIp, final int pPort) {
        return new ShProxyInfo(ShProxyType.SOCKS4, pIp, pPort, "", "");
    }

    public static ShProxyInfo socks5Proxy(final String pIpPort) {
        String[] i = pIpPort.split(":");
        Preconditions.checkArgument(i.length == 2, "args is not ip:port");

        String ip = i[0];
        int port = Integer.parseInt(i[1]);
        return socks5Proxy(ip, port);
    }

    public static ShProxyInfo socks5Proxy(final String pIp, final int pPort) {
        return new ShProxyInfo(ShProxyType.SOCKS5, pIp, pPort, "", "");
    }

    public ShProxyInfo set(final String pUsername, final String pPassword) {
        this.proxyUsername = pUsername;
        this.proxyPassword = pPassword;
        return this;
    }

    public ShProxyType getProxyType() {
        return proxyType;
    }

    public String getProxyIp() {
        return proxyIp;
    }

    public int getProxyPort() {
        return proxyPort;
    }

    public String getProxyUsername() {
        return proxyUsername;
    }

    public String getProxyPassword() {
        return proxyPassword;
    }

}
