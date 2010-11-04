package com.googlecode.gwtrpccommlayer.gwtp.server;

import com.google.inject.servlet.ServletModule;
import com.gwtplatform.dispatch.server.guice.DispatchModule;
import com.gwtplatform.dispatch.shared.ActionImpl;

/**
 * Created by IntelliJ IDEA.
 * User: dan
 * Date: 10/30/10
 * Time: 3:15 PM
 */
public class Module extends ServletModule {

    private String strModuleName;

    /**
     * cannot use Inject because we are creating Modules!
     * @param moduleName
     */
    public Module(String moduleName){
        this.strModuleName = moduleName;
    }

    @Override
    protected void configureServlets() {
        install(new DispatchModule());
        serve("/" + strModuleName + "/" + ActionImpl.DEFAULT_SERVICE_NAME);


    }
}
