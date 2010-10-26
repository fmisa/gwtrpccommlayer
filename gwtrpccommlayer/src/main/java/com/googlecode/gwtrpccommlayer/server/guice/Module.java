package com.googlecode.gwtrpccommlayer.server.guice;

import com.google.inject.AbstractModule;
import com.google.inject.name.Names;
import com.google.inject.servlet.ServletModule;
import com.googlecode.gwtrpccommlayer.server.GwtRpcCommLayerServlet;
import com.googlecode.gwtrpccommlayer.shared.GwtRpcCommLayerPojoConstants;

/**
 * Created by IntelliJ IDEA.
 * User: Owner
 * Date: 10/22/10
 * Time: 10:32 AM
 * To change this template use File | Settings | File Templates.
 */
public class Module extends ServletModule {

    public void configureServlets() {
        serve("/*").with(GwtRpcCommLayerServlet.class);

/*        bind(String.class).annotatedWith(Names.named(
                GwtRpcCommLayerPojoConstants.GWT_RPC_COMM_LAYER_SERVLET_IMPL_CLASS)
                ).toInstance("org.gwtplatform.server.DispatchServlet");*/
    }
}
