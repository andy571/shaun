package com.training.shaun;

import com.training.shaun.socks.ThreadLocalProxyAuthenticator;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.impl.client.CloseableHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * User: andy
 * Date: 2018/8/30
 * Description:
 */
public class ShExecutor {

    private final CloseableHttpClient client;

    private final Logger log = LoggerFactory.getLogger(ShExecutor.class);

    public ShExecutor(final CloseableHttpClient httpclient) {
        super();
        if (httpclient == null) {
            this.client = ShHttpClientBuild.create().defaut().build();
        } else {
            this.client = httpclient;
        }
    }

    public HttpClient getHttpclient() {
        return client;
    }

    public ShResponse execute(final ShRequest shRequest) {
        CloseableHttpResponse cResponse = null;
        Exception exception = null;
        try {
            cResponse = client.execute(shRequest.request);
            ShResponse shResponse = new ShResponse(cResponse);
            shResponse.returnResponse();

        } catch (Exception e) {
            log.debug("execute fail", e);
            exception = e;
        } finally {
            if (cResponse != null) {
                try {
                    cResponse.close();
                } catch (IOException e) {
                    log.debug("execute fail", e);
                }
            }
            return new ShResponse(null, exception);
        }

    }
//        try {
//            CloseableHttpResponse cResponse = client.execute(shRequest.request);
//
//            ShResponse shResponse = new ShResponse(cResponse);
//            shResponse.returnResponse();
//
//            cResponse.close();
//            return shResponse;
//        } catch (Exception e) {
//            e.printStackTrace();
//            return new ShResponse(null, e);
//        }
//    }

//    public ShResponse execute(final ShRequest shRequest) throws IOException {
//
//        CloseableHttpResponse cresponse = client.execute(shRequest.request);
//
//        ShResponse sresponse = new ShResponse(cresponse);
//        sresponse.returnResponse();
//
//        cresponse.close();
//        return sresponse;
//    }

    public void close() {
        try {
            ThreadLocalProxyAuthenticator.getInstance().clearCredential();
            client.close();
        } catch (Exception e) {
            log.debug("close fail" ,e);
        }
    }

}
