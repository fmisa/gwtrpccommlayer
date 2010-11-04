package com.googlecode.gwtrpccommlayer.gwtp.client;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.googlecode.gwtrpccommlayer.client.impl.GwtRpcCommLayerClient;
import org.apache.http.cookie.Cookie;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.net.URL;
import java.util.HashMap;

/**
 * Created by IntelliJ IDEA.
 * User: dan
 * Date: 10/30/10
 * Time: 11:04 PM
 */
public class ClientWithCookies extends GwtRpcCommLayerClient {

    private HashMap<String,Cookie> cookies;

    @Inject
    public ClientWithCookies(URL url, @Assisted HashMap<String, Cookie> cookies) {
        super(url);
        this.cookies = cookies;
    }

    @Override
    public <T> T createRemoteServicePojoProxy(Class<T> serviceInterface) throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        		InvocationHandler handler = createGwtRpcClientSideProxy();
		T proxy = (T) Proxy.newProxyInstance(
                serviceInterface.getClassLoader(),
                new Class[]{serviceInterface},
                handler);

		return proxy;
    }

}
