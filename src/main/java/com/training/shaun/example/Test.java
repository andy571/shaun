package com.training.shaun.example;

import com.training.shaun.socks.ThreadLocalProxyAuthenticator;

import java.net.*;
import java.util.concurrent.TimeUnit;

/**
 * User: andy
 * Date: 2018/9/11
 * Description:
 */
public class Test {

    public static void main(String[] args) throws UnknownHostException, InterruptedException {

        new Thread(new Runnable() {
            public void run() {
                ThreadLocalProxyAuthenticator.getInstance().setCredentials("aaaa", "bb");


                try {
                    TimeUnit.SECONDS.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                ThreadLocalProxyAuthenticator.getInstance().clearCredential();
                while (true) {
                    PasswordAuthentication aa = null;
                    try {
                        aa = Authenticator.requestPasswordAuthentication(
                                "aaa", InetAddress.getByName("www.baidu.com"), 2222, "SOCKS5", "SOCKS authentication", null);
                    } catch (UnknownHostException e) {
                        e.printStackTrace();
                    }
                    System.out.println(aa.getUserName());
                }
//                ThreadLocalProxyAuthenticator.getInstance().clearCredential();
/*
setDefault(new Authenticator() {
protected PasswordAuthentication getPasswordAuthentication() {
return new PasswordAuthentication("aaaa", "bb"
.toCharArray());
}
});
Authenticator.setDefault(new Authenticator() {
protected PasswordAuthentication getPasswordAuthentication() {
return new PasswordAuthentication("aaaa", "bb"
.toCharArray());
}
});
*/

//                cc.clearCredentials();
//
//                PasswordAuthentication aa1 = null;
//                try {
//                    aa1 = Authenticator.requestPasswordAuthentication(
//                            "aaa", InetAddress.getByName("www.baidu.com"), 2222, "SOCKS5", "SOCKS authentication", null);
//                } catch (UnknownHostException e) {
//                    e.printStackTrace();
//                }
//                System.out.println(aa1.getUserName());
//
            }
        }).start();


        new Thread(new Runnable() {
            public void run() {
                ThreadLocalProxyAuthenticator.getInstance().setCredentials("cccc", "bb");
                while (true) {
                    PasswordAuthentication aa = null;
                    try {
                        aa = Authenticator.requestPasswordAuthentication(
                                "aaa", InetAddress.getByName("www.baidu.com"), 2222, "SOCKS5", "SOCKS authentication", null);
                    } catch (UnknownHostException e) {
                        e.printStackTrace();
                    }
                    try {
                        TimeUnit.SECONDS.sleep(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println(aa.getUserName());
                }
//                cc.clearCredentials();
//
//                PasswordAuthentication aa1 = null;
//                try {
//                    aa1 = Authenticator.requestPasswordAuthentication(
//                            "aaa", InetAddress.getByName("www.baidu.com"), 2222, "SOCKS5", "SOCKS authentication", null);
//                } catch (UnknownHostException e) {
//                    e.printStackTrace();
//                }
//                System.out.println(aa1.getUserName());

            }
        }).start();

//        TimeUnit.SECONDS.sleep(5);
//        PasswordAuthentication aa = Authenticator.requestPasswordAuthentication(
//                "aaa", InetAddress.getByName("www.baidu.com"), 2222, "SOCKS5", "SOCKS authentication", null);
//        System.out.println(aa.getUserName());

    }
}
