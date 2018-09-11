package com.training.shaun.message;

import org.apache.http.Header;
import org.apache.http.message.BasicHeader;

import java.util.ArrayList;
import java.util.List;

/**
 * User: andy
 * Date: 2018/8/30
 * Description:
 */
public class ShHead {

    private final List<Header> params;

    public static ShHead head() {
        return new ShHead();
    }

    ShHead() {
        super();
        this.params = new ArrayList<Header>();
    }

    public ShHead add(final String name, final String value) {
        this.params.add(new BasicHeader(name, value));
        return this;
    }

    public Header[] build() {
        int size = params.size();
        return (Header[])params.toArray(new Header[size]);
    }

}
