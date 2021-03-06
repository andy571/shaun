package com.training.shaun;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.charset.Charset;

/**
 * User: andy
 * Date: 2018/8/30
 * Description:
 */
public class ShResponse {

    public final HttpResponse response;

    public final Exception exception;

    private final Logger log = LoggerFactory.getLogger(ShResponse.class);

    public StatusLine getStatusLine() {
        if (response != null) {
            return response.getStatusLine();
        }
        return null;
    }

    public int getStatusCode() {
        if (response != null) {
            return response.getStatusLine().getStatusCode();
        }
        return -1;
    }

    public byte[] getEntityAsBytes() {

        if (response != null) {
            try {
                return EntityUtils.toByteArray(response.getEntity());
            } catch (Exception e) {
                log.debug("response", e);
            }
        }
        return null;
    }

    public String getEntityAsString() {
        return getEntityAsString("utf-8");
    }

    public String getEntityAsString(String charset) {
        final Charset cs = Charset.forName(charset);
        byte[] b = getEntityAsBytes();
        if (b != null) {
            return new String(b, cs);
        }
        return null;
    }

    ShResponse(final HttpResponse response) {
        super();
        this.response = response;
        this.exception = null;
    }

    ShResponse(final HttpResponse response, final Exception exception) {
        super();
        this.response = response;
        this.exception = exception;
    }

    public HttpResponse returnResponse() throws IOException {
//        assertNotConsumed();
        try {
            final HttpEntity entity = this.response.getEntity();
            if (entity != null) {
                final ByteArrayEntity byteArrayEntity = new ByteArrayEntity(
                        EntityUtils.toByteArray(entity));
                final ContentType contentType = ContentType.getOrDefault(entity);
                byteArrayEntity.setContentType(contentType.toString());
                this.response.setEntity(byteArrayEntity);
            }
            return this.response;
        }  finally {
//            this.consumed = true;
        }
    }

}
