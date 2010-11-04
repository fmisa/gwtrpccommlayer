package com.googlecode.gwtrpccommlayer.client.impl;

import com.googlecode.gwtrpccommlayer.client.GwtRpcService;
import org.apache.http.cookie.Cookie;

import java.net.URL;
import java.util.Collection;

/**
 * Created by IntelliJ IDEA.
 * User: dan
 * Date: 10/30/10
 * Time: 2:55 PM
 */
public class GwtRpcServiceImpl implements GwtRpcService {

    @Override
    public <T> T create(URL url, Class<T> serviceClass) {
        GwtRpcCommLayerClient gwtClient = new GwtRpcCommLayerClient(url);
        return returnService(serviceClass, gwtClient);
    }

    @Override
    public <T> T create(URL url, Class<T> serviceClass, Collection<Cookie> cookies) {
        GwtRpcCommLayerClient gwtClient = new GwtRpcCommLayerClient(url, cookies);
        return returnService(serviceClass, gwtClient);
    }

    private <T> T returnService(Class<T> serviceClass, GwtRpcCommLayerClient gwtClient) {
        try {
            T service = gwtClient.createRemoteServicePojoProxy(serviceClass);
            return service;
        } catch (ClassNotFoundException e) {
            //todo: do something interesting with this
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (IllegalAccessException e) {
            //todo: do something interesting with this
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (InstantiationException e) {
            //todo: do something interesting with this
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        return null;
    }

}
