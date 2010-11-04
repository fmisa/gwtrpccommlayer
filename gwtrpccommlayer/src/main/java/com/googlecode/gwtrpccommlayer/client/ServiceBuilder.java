package com.googlecode.gwtrpccommlayer.client;

import org.apache.http.cookie.Cookie;

import java.net.URL;
import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: dan
 * Date: 10/31/10
 * Time: 2:42 PM
 */
public class ServiceBuilder<T> {

    public final URL url;
    public final Class<T> serviceClass;

    public final ArrayList<Cookie> lstCookies = new ArrayList<Cookie>();

    public ServiceBuilder(URL url, Class<T> serviceClass) {
        this.url = url;
        this.serviceClass = serviceClass;
    }

    public ServiceBuilder<T> cookie(Cookie cookie) {
        lstCookies.add(cookie);
        return this;
    }

    public T build() {
        return null;
    }
}
