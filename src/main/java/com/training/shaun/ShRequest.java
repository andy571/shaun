package com.training.shaun;

import org.apache.http.*;
import org.apache.http.client.methods.*;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.entity.ByteArrayEntity;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * User: andy
 * Date: 2018/8/30
 * Description: 借鉴字httpclient fluent
 */
public class ShRequest {

    public final HttpRequestBase request;

    public static ShRequest Get(final String uri) {
        return new ShRequest(new HttpGet(uri));
    }

    public static ShRequest Head(final String uri) {
        return new ShRequest(new HttpHead(uri));
    }

    public static ShRequest Post(final String uri) {
        return new ShRequest(new HttpPost(uri));
    }

    public static ShRequest Patch(final String uri) {
        return new ShRequest(new HttpPatch(uri));
    }

    public static ShRequest Put(final String uri) {
        return new ShRequest(new HttpPut(uri));
    }

    public static ShRequest Trace(final String uri) {
        return new ShRequest(new HttpTrace(uri));
    }

    public static ShRequest Delete(final String uri) {
        return new ShRequest(new HttpDelete(uri));
    }

    public static ShRequest Options(final String uri) {
        return new ShRequest(new HttpOptions(uri));
    }

    public ShRequest(final HttpRequestBase request) {
        super();
        this.request = request;
    }

//    HttpResponse internalExecute(
//            final HttpClient client,
//            final HttpContext localContext) throws IOException {
//        return client.execute(this.request, localContext);
//    }

    public ShResponse execute() {
        ShExecutor e = new ShExecutor(null);
        ShResponse response = e.execute(this);
        e.close();
        return response;
    }

    public ShResponse execute(ShExecutor sExecutor) {
        return sExecutor.execute(this);
    }

    //// HTTP header operations
    public ShRequest addHeader(final String name, final String value) {
        this.request.addHeader(name, value);
        return this;
    }

    public ShRequest setHeader(final String name, final String value) {
        this.request.setHeader(name, value);
        return this;
    }

    public ShRequest removeHeaders(final String name) {
        this.request.removeHeaders(name);
        return this;
    }

    public ShRequest setHeader(final Header header) {
        this.request.setHeader(header);
        return this;
    }

    public ShRequest setHeaders(final Header[] headers) {
        this.request.setHeaders(headers);
        return this;
    }

    public ShRequest setHeaders(final Map<String, String> headers) {
        for (String name: headers.keySet()) {
            this.request.setHeader(name, headers.get(name));
        }
        return this;
    }

    public ShRequest removeHeader(final Header header) {
        this.request.removeHeader(header);
        return this;
    }

    //// HTTP entity operations

    public ShRequest body(final HttpEntity entity) {
        if (this.request instanceof HttpEntityEnclosingRequest) {
            ((HttpEntityEnclosingRequest) this.request).setEntity(entity);
        } else {
            throw new IllegalStateException(this.request.getMethod() + " request cannot enclose an entity");
        }
        return this;
    }

    public ShRequest bodyByteArray(final byte[] b) {
        return body(new ByteArrayEntity(b));
    }

    public ShRequest bodyByteArray(final byte[] b, final int off, final int len) {
        return body(new ByteArrayEntity(b, off, len));
    }

    public ShRequest bodyString(final String s) {
        return bodyString(s, "UTF-8");
    }

    public ShRequest bodyString(final String s, final String charset) {
        final Charset cs = Charset.forName(charset);
        final byte[] raw = cs != null ? s.getBytes(cs) : s.getBytes();
        return bodyByteArray(raw);
//        final Charset charset = contentType != null ? contentType.getCharset() : null;
//        final byte[] raw = charset != null ? s.getBytes(charset) : s.getBytes();
//        return body(new InternalByteArrayEntity(raw, contentType));
    }


    public ShRequest bodyForm(final Iterable <? extends NameValuePair> formParams, final String charset) {
        final Charset cs = Charset.forName(charset);
        final List<NameValuePair> paramList = new ArrayList<NameValuePair>();
        for (final NameValuePair param : formParams) {
            paramList.add(param);
        }

        final String s = URLEncodedUtils.format(paramList, charset);

        setHeader(HttpHeaders.CONTENT_TYPE, URLEncodedUtils.CONTENT_TYPE);

        return bodyString(s, charset);
    }

    public ShRequest bodyForm(final Iterable <? extends NameValuePair> formParams) {
        return bodyForm(formParams, "UTF-8");
    }

    @Override
    public String toString() {
        return this.request.getRequestLine().toString();
    }

}
