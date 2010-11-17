package com.googlecode.gwtrpccommlayer.gwtp.server;

import com.google.inject.Scopes;
import com.google.inject.servlet.ServletModule;
import com.gwtplatform.dispatch.server.Dispatch;
import com.gwtplatform.dispatch.server.DispatchImpl;
import com.gwtplatform.dispatch.server.guice.DispatchModule;
import com.gwtplatform.dispatch.shared.ActionImpl;

/**
 * Created by IntelliJ IDEA.
 * User: dan
 * Date: 10/30/10
 * Time: 3:15 PM
 */
public class Module extends ServletModule {


    /**
     */
    public Module(){
    }

    @Override
    protected void configureServlets() {
        install(new DispatchModule());
        serve("/" + ActionImpl.DEFAULT_SERVICE_NAME).with(DispatchingPOJOServlet.class);
        bind(DispatchingPOJOServlet.class).in(Scopes.SINGLETON);

        //bind(Dispatch.class).to(DispatchImpl.class);

    }
}
