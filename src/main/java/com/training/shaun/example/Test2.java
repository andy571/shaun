package com.training.shaun.example;

import com.training.shaun.ShExecutor;
import com.training.shaun.ShHttpClientBuild;
import com.training.shaun.ShRequest;
import com.training.shaun.proxy.ShProxyInfo;

import java.io.IOException;

/**
 * User: andy
 * Date: 2018/9/11
 * Description:
 */
public class Test2 {

    public static void main(String[] args) throws IOException {
        ShExecutor ex = ShHttpClientBuild.create().proxy(ShProxyInfo.httpProxy("127.0.0.1:8888")).retry(2, true).newExecutor();
        String aaaa = ShRequest.Get("http://www.baidu.com").addHeader("user-a", "aaaa").execute(ex).getEntityAsString();
        System.out.println(aaaa);
        ex.close();
    }
}
