package com.googlecode.gwtrpccommlayer.gwtp.test;

import com.gwtplatform.dispatch.server.guice.HandlerModule;

/**
 * Created by IntelliJ IDEA.
 * User: dan
 * Date: 11/16/10
 * Time: 8:12 PM
 */
public class THandlerModule extends HandlerModule{
    @Override
    protected void configureHandlers() {
        bindHandler(TAction.class, THandler.class);
    }
}
