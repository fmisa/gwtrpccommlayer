package com.googlecode.gwtrpccommlayer.client;

import com.google.inject.AbstractModule;
import com.googlecode.gwtrpccommlayer.client.impl.GwtRpcServiceImpl;

public class Module extends AbstractModule {
    @Override
    protected void configure() {
        bind(GwtRpcService.class).to(GwtRpcServiceImpl.class);
    }


}