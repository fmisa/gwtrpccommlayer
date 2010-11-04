package com.googlecode.gwtrpccommlayer.client;

import org.apache.http.cookie.Cookie;

import java.net.URL;
import java.util.Collection;

/**
 *
 * Meant to accept Service Interface Classes and return an implementation.
 *
 * Created by IntelliJ IDEA.
 * User: dan
 * Date: 10/30/10
 * Time: 2:52 PM
 */
public interface GwtRpcService {

    /**
     * This weakly specified generic T kinda sucks because the "async" GWT interface is not identified by anything.
     *
     * @param url
     * @param serviceClass
     * @param <T>
     * @return
     */
    <T> T create(URL url, Class<T> serviceClass);

    <T> T create(URL url, Class<T> serviceClass, Collection<Cookie> cookies);

}
