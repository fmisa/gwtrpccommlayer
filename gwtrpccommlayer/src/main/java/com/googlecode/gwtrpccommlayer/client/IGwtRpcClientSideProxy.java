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
package com.googlecode.gwtrpccommlayer.client;

import java.lang.reflect.InvocationHandler;
import java.net.URL;

/**
 * Provides a interface layer to the proxy implementation
 * @author jeff mchugh
 *
 */
public interface IGwtRpcClientSideProxy extends InvocationHandler
{
	/**
	 * The URL of the GwtRpc servlet
	 * @param url
	 */
	public void setUrl(URL url);
	
	/**
	 * Used for development/debugging
	 * @param b
	 */
	public void setShowResponseHeaders(boolean b);
}
