package com.googlecode.gwtrpccommlayer.client.factory;

import java.lang.reflect.InvocationHandler;
import java.net.URL;

/**
 * Created by IntelliJ IDEA.
 * User: dan
 * Date: 10/31/10
 * Time: 3:30 PM
 */
public interface InvocationHandlerFactory {
    InvocationHandler create(URL url);
}
