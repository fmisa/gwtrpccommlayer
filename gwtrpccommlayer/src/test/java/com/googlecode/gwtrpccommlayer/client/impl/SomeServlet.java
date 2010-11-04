package com.googlecode.gwtrpccommlayer.client.impl;

import com.google.inject.Singleton;
import com.googlecode.gwtrpccommlayer.server.GwtRpcCommLayerServlet;

import java.util.Set;
import java.util.concurrent.CountDownLatch;

/**
* Created by IntelliJ IDEA.
* User: dan
* Date: 11/4/10
* Time: 12:39 AM
*/
@Singleton
public class SomeServlet extends GwtRpcCommLayerServlet implements TestService{
    CountDownLatch easyCountdownLatch = new CountDownLatch(1);
    CountDownLatch mediumCountdownLatch = new CountDownLatch(1);
    CountDownLatch hardCountdownLatch = new CountDownLatch(1);
    @Override
    public void easy() {
        easyCountdownLatch.countDown();
    }

    @Override
    public void medium(String test, Integer another) throws Exception {
        throw new Exception("whatever");
    }

    @Override
    public String mediumHard(String test) {
        return test;
    }

    @Override
    public <E> Set<E> hard(Set<E> input) {
        hardCountdownLatch.countDown();
        return input;
    }
}
