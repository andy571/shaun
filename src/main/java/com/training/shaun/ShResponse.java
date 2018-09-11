package com.training.shaun;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.nio.charset.Charset;

/**
 * User: andy
 * Date: 2018/8/30
 * Description:
 */
public class ShResponse {

    public final HttpResponse response;

    public StatusLine getStatusLine() {
        return response.getStatusLine();
    }

    public int getStatusCode() {
        return response.getStatusLine().getStatusCode();
    }

    public byte[] getEntityAsBytes() {

        if (response != null) {
            try {
                return EntityUtils.toByteArray(response.getEntity());
            } catch (Exception e) {
                e.printStackTrace();
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
