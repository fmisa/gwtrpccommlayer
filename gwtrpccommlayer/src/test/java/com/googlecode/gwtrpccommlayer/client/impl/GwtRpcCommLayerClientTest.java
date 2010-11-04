package com.googlecode.gwtrpccommlayer.client.impl;

import com.googlecode.gwtrpccommlayer.client.impl.GwtRpcCommLayerClient;
import org.junit.Before;
import org.junit.Test;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by IntelliJ IDEA.
 * User: dan
 * Date: 10/30/10
 * Time: 9:42 PM
 */
public class GwtRpcCommLayerClientTest {

    private GwtRpcCommLayerClient client;

    @Before
    public void setup() {
        try {
            this.client = new GwtRpcCommLayerClient(new URL("http://localhost"));
        } catch (MalformedURLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

    }

    @Test
    public void testCreateRemoteServicePojoProxy() throws Exception {
        //client.createRemoteServicePojoProxy(DispatchAsyn)

    }

    @Test
    public void testGetUrl() throws Exception {

    }

    @Test
    public void testSetGwtRpcClientSizeProxyImplClass() throws Exception {

    }

    @Test
    public void testGetGwtRpcClientSizeProxyImplClass() throws Exception {

    }
}
