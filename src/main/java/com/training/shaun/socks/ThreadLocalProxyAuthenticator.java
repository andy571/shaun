package com.training.shaun.socks;

import java.net.Authenticator;
import java.net.PasswordAuthentication;

/**
 * User: andy
 * Date: 2018/2/28
 * Description:
 */
public class ThreadLocalProxyAuthenticator extends Authenticator{

    private ThreadLocal<PasswordAuthentication> credential = new ThreadLocal<PasswordAuthentication>();

    private static class SingletonHolder {
        private static final ThreadLocalProxyAuthenticator instance = new ThreadLocalProxyAuthenticator();
    }

    public static final ThreadLocalProxyAuthenticator getInstance() {
        return SingletonHolder.instance;
    }

    public void setCredentials(String user, String password) {
        credential.set(new PasswordAuthentication(user, password.toCharArray()));
        Authenticator.setDefault(this);
    }

    public void clearCredential() {
        credential.set(null);
    }

    public PasswordAuthentication getPasswordAuthentication() {
        return credential.get();
    }

}
