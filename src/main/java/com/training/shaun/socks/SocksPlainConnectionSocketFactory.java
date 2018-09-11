package com.training.shaun.socks;

import com.training.shaun.proxy.ShProxyType;
import com.training.shaun.proxy.ShProxyInfo;
import org.apache.http.HttpHost;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.protocol.HttpContext;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.Socket;

/**
 * User: andy
 * Date: 2018/2/27
 * Description:
 */
public class SocksPlainConnectionSocketFactory extends PlainConnectionSocketFactory {

    private ShProxyInfo proxyInfo;

    public SocksPlainConnectionSocketFactory(ShProxyInfo proxyInfo) {
        super();
        this.proxyInfo = proxyInfo;
    }

    @Override
    public Socket createSocket(final HttpContext context) throws IOException {

        InetSocketAddress socksaddr = new InetSocketAddress(proxyInfo.getProxyIp(), proxyInfo.getProxyPort());

        Proxy proxy = new Proxy(Proxy.Type.SOCKS, socksaddr);
        Socket socket = new Socket(proxy);

        if (proxyInfo.getProxyType() == ShProxyType.SOCKS4) {
            try {
                Class cls = socket.getClass();
                Field field = cls.getDeclaredField("impl");
                field.setAccessible(true);
                Object value = field.get(socket);
                Class cls2 = value.getClass();
                Method method = cls2.getDeclaredMethod("setV4");
                method.setAccessible(true);
                method.invoke(value);

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {

        }
        return socket;
    }

    @Override
    public Socket connectSocket(int connectTimeout, Socket socket, HttpHost host, InetSocketAddress remoteAddress,
                                InetSocketAddress localAddress, HttpContext context) throws IOException {
        InetSocketAddress unresolvedRemote = null;
        if (proxyInfo.getProxyType() == ShProxyType.SOCKS4) {
            // SOCKS Protocol version 4 doesn't know how to deal with
            // DOMAIN type of addresses (unresolved addresses here)
            unresolvedRemote = new InetSocketAddress(host.getHostName(), remoteAddress.getPort());
        } else {
            // Convert address to unresolved
            unresolvedRemote = InetSocketAddress.createUnresolved(host.getHostName(), remoteAddress.getPort());
        }
        return super.connectSocket(connectTimeout, socket, host, unresolvedRemote, localAddress, context);

    }
}
