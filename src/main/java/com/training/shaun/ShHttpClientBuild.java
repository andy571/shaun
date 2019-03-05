package com.training.shaun;

import com.training.shaun.proxy.ShProxyInfo;
import com.training.shaun.socks.SocksPlainConnectionSocketFactory;
import com.training.shaun.socks.SocksSSLConnectionSocketFactory;
import com.training.shaun.socks.ThreadLocalProxyAuthenticator;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.NoHttpResponseException;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.Credentials;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustAllStrategy;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.protocol.HttpContext;
import org.apache.http.ssl.SSLContextBuilder;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLException;
import javax.net.ssl.SSLHandshakeException;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;

/**
 * User: andy
 * Date: 2018/2/27
 * Description:
 */
public class ShHttpClientBuild extends HttpClientBuilder {

    public static ShHttpClientBuild create() {
        return new ShHttpClientBuild();
    }

    public ShExecutor newExecutor() {
        return new ShExecutor(this.build());
    }

    public ShHttpClientBuild defaut() {
        return  this.timeout(30000, true).proxy(ShProxyInfo.noProxy()).retry(3, true);
    }

    public ShHttpClientBuild timeout(int timeout){
        return timeout(timeout, true);
    }

    public ShHttpClientBuild timeout(int timeout, boolean redirectEnable) {
        // 配置请求的超时设置
        RequestConfig config = RequestConfig.custom()
                .setConnectionRequestTimeout(timeout/3)
                .setConnectTimeout(timeout/3)
                .setSocketTimeout(timeout/3)
                .setRedirectsEnabled(redirectEnable)
                .build();
        return (ShHttpClientBuild) this.setDefaultRequestConfig(config);
    }

    public PoolingHttpClientConnectionManager connManager = null;

    public ShHttpClientBuild proxy(ShProxyInfo proxyInfo) {
        SSLContext sslContext = null;
        try {
            sslContext = new SSLContextBuilder().loadTrustMaterial(null, TrustAllStrategy.INSTANCE).build();
//            sslContext = new SSLContextBuilder().loadTrustMaterial(null, new TrustStrategy() {
//                    @Override
//                    public boolean isTrusted(X509Certificate[] chain, String authType)
//                            throws CertificateException {
//                        return true;
//                    }
//
//                }).build();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        }

        switch (proxyInfo.getProxyType()) {
            case NONE:
                return this;
            case HTTP:
                Registry<ConnectionSocketFactory> httpFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
                        .register("http", PlainConnectionSocketFactory.INSTANCE)
                        .register("https", new SSLConnectionSocketFactory(sslContext, NoopHostnameVerifier.INSTANCE))
                        .build();

                //设置连接池大小
                connManager = new PoolingHttpClientConnectionManager(httpFactoryRegistry);
                connManager.setMaxTotal(200);
//                connManager.setDefaultMaxPerRoute(50);

                //        connManager.setMaxTotal(maxTotal);// Increase max total connection to $maxTotal
//        connManager.setDefaultMaxPerRoute(defaultMaxPerRoute);// Increase default max connection per route to $defaultMaxPerRoute
                //connManager.setMaxPerRoute(route, max);// Increase max connections for $route(eg：localhost:80) to 50

                AuthScope authScope = new AuthScope(proxyInfo.getProxyIp(), proxyInfo.getProxyPort());

                Credentials credentials = new UsernamePasswordCredentials(proxyInfo.getProxyUsername(), proxyInfo.getProxyPassword());
                CredentialsProvider credsProvider = new BasicCredentialsProvider();
                credsProvider.setCredentials(authScope, credentials);

                HttpHost proxy = new HttpHost(proxyInfo.getProxyIp(), proxyInfo.getProxyPort());
                return (ShHttpClientBuild) this.setConnectionManager(connManager).setProxy(proxy).setDefaultCredentialsProvider(credsProvider);

            case SOCKS4:
            case SOCKS5:
                Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
                        .register("http", new SocksPlainConnectionSocketFactory(proxyInfo))
                        .register("https", new SocksSSLConnectionSocketFactory(sslContext, NoopHostnameVerifier.INSTANCE, proxyInfo))
                        .build();


                //设置连接池大小
                connManager = new PoolingHttpClientConnectionManager(socketFactoryRegistry);

                connManager.setMaxTotal(200);
//                connManager.setDefaultMaxPerRoute(50);

                this.setConnectionManager(connManager);

//                InetSocketAddress socksaddr = new InetSocketAddress(proxyInfo.getProxyAddress(), proxyInfo.getProxyPort());

                if (proxyInfo.getProxyUsername() != null && !proxyInfo.getProxyUsername().equals("")) {
                    ThreadLocalProxyAuthenticator.getInstance().setCredentials(proxyInfo.getProxyUsername(), proxyInfo.getProxyPassword());//设置授权信息
                }

                return this;
            default:
                return this;

        }
    }

    public ShHttpClientBuild retry(final int tryTimes, final boolean retryWhenInterruptedIO){
        // 请求重试处理
        HttpRequestRetryHandler httpRequestRetryHandler = new HttpRequestRetryHandler() {
            public boolean retryRequest(IOException exception, int executionCount, HttpContext context) {
                if (executionCount >= tryTimes) {// 如果已经重试了n次，就放弃
                    return false;
                }
                if (exception instanceof NoHttpResponseException) {// 如果服务器丢掉了连接，那么就重试
                    return true;
                }
                if (exception instanceof SSLHandshakeException) {// 不要重试SSL握手异常
                    return false;
                }
                if (exception instanceof InterruptedIOException) {// 超时
                    //return false;
                    return retryWhenInterruptedIO;
                }
                if (exception instanceof UnknownHostException) {// 目标服务器不可达
                    return true;
                }
                if (exception instanceof ConnectTimeoutException) {// 连接被拒绝
                    return false;
                }
                if (exception instanceof SSLException) {// SSL握手异常
                    return false;
                }

                HttpClientContext clientContext = HttpClientContext.adapt(context);
                HttpRequest request = clientContext.getRequest();
                // 如果请求是幂等的，就再次尝试
                if (!(request instanceof HttpEntityEnclosingRequest)) {
                    return true;
                }
                return false;
            }
        };
        this.setRetryHandler(httpRequestRetryHandler);
        return this;
    }

}
