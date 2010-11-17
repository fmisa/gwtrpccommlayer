package com.googlecode.gwtrpccommlayer.gwtp.server;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.servlet.GuiceServletContextListener;

/**
 * Created by IntelliJ IDEA.
 * User: dan
 * Date: 11/4/10
 * Time: 2:59 PM
 */
public class GuiceListener extends GuiceServletContextListener {


    @Override
    protected Injector getInjector() {
        return Guice.createInjector(new Module());
    }
}
