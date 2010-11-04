/*
 * Copyright 2010 Jeff McHugh (Segue Development LLC)
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package com.googlecode.gwtrpccommlayer.client.impl;

import com.googlecode.gwtrpccommlayer.client.factory.InvocationHandlerFactory;
import org.apache.http.cookie.Cookie;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.net.URL;
import java.util.Collection;

/**
 * This class is the main client-side hook for all RPC interactions.
 * It hides the proxy class slightly and makes it easy for the developer
 * to quickly initiate a RPC class
 * @author jeff mchugh
 *
 */
public class RefactoredClient
{
    private final InvocationHandlerFactory handlerFactory;
    private URL url = null;
	private String gwtRpcClientSizeProxyImplClass = null;
    private Collection<Cookie> cookies;

    public RefactoredClient(InvocationHandlerFactory handlerFactory, URL url)
	{
        this.handlerFactory = handlerFactory;
        this.url = url;
	}

	/**
	 * This method is the main method through which a GWT interface shall be "wrapped" into a working pojo-proxy-client object.
	 * 
	 * @param serviceInterface (this class is your basic interface class which the developer should have already defined according the GWT-RPC specification 
	 * @return a PojoProxy class that uses standard Java Object serialization instead of GWT-RPC servialization
	 * @throws ClassNotFoundException
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 */
	public <T> T createRemoteServicePojoProxy(Class<T> serviceInterface) throws ClassNotFoundException, IllegalAccessException, InstantiationException
	{
        //Get the Invocation Handler
        //All we Really Need is an Invocation HAndler!

        //Use a factory with a URL to obtain an INvocation HAndler
        //handler = factory.create(url)

        //todo: REMOVE and replace with above factory
		InvocationHandler handler = handlerFactory.create(url);

		T proxy = (T) Proxy.newProxyInstance(
                serviceInterface.getClassLoader(),
                new Class[] { serviceInterface },
                handler);
		return proxy;
	}



}
