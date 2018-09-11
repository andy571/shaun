package com.training.shaun.message;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

/**
 * User: andy
 * Date: 2018/8/30
 * Description:
 */
public class ShForm {

    private final List<NameValuePair> params;

    public static ShForm form() {
        return new ShForm();
    }

    ShForm() {
        super();
        this.params = new ArrayList<NameValuePair>();
    }

    public ShForm add(final String name, final String value) {
        this.params.add(new BasicNameValuePair(name, value));
        return this;
    }

    public List<NameValuePair> build() {
        return new ArrayList<NameValuePair>(this.params);
    }
}
