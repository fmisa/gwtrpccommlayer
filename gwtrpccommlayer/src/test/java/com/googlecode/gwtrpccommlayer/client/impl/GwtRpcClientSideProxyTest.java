package com.googlecode.gwtrpccommlayer.client.impl;

import com.googlecode.gwtrpccommlayer.client.impl.GwtRpcClientSideProxy;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by IntelliJ IDEA.
 * User: dan
 * Date: 10/30/10
 * Time: 9:43 PM
 */
public class GwtRpcClientSideProxyTest {
    @Before
    public void setUp() throws Exception {

    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void testInvoke() throws Exception {

    }

    @Test
    public void testSetUrl() throws Exception {

    }

    @Test
    public void testSetShowResponseHeaders() throws Exception {

    }

    @Test
    public void testSetCookies() throws Exception {

    }

    @Test
    public void testGetCookies() throws Exception {

    }

    @Test
    public void testSetScheme() throws Exception {

    }

    @Test
    public void testGetScheme() throws Exception {

    }

    @Test
    public void testSetHost() throws Exception {

    }

    @Test
    public void testGetHost() throws Exception {

    }

    @Test
    public void testSetPort() throws Exception {

    }

    @Test
    public void testGetPortWhenUnprovided() throws Exception {
        GwtRpcClientSideProxy proxy = new GwtRpcClientSideProxy(new URL("http://www.google.com"));
        Assert.assertEquals(new Integer(80), proxy.getPort());
    }

    @Test
    public void testGetPortProvided() throws MalformedURLException {
        Integer PORT = 8080;
        GwtRpcClientSideProxy proxy =
                new GwtRpcClientSideProxy(new URL("http://www.google.com:" + PORT));
        Assert.assertEquals(PORT, proxy.getPort());
    }

    @Test
    public void testSetPath() throws Exception {

    }

    @Test
    public void testGetPath() throws Exception {

    }

    @Test
    public void testSetQueryString() throws Exception {

    }

    @Test
    public void testGetQueryString() throws Exception {

    }
}
