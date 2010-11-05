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
package com.googlecode.gwtrpccommlayer.shared;

import com.google.gwt.user.client.rpc.AsyncCallback;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * Abstracts the Request made from the GwtRpcCommLayer-Client
 * @author jeff mchugh
 *
 */
public class GwtRpcCommLayerPojoRequest implements Serializable
{
	/**
	 * 
	 */
	private static final long	serialVersionUID	= 6032112000081674584L;

	private ArrayList<Serializable> methodParameters 	= null;
	private String methodName							= null;
	private HashMap<String,String> metaData 			= null;

    //Need to set method parameter classes
    private List<String> lstParamClassNames = new LinkedList<String>();

	
	public GwtRpcCommLayerPojoRequest()
	{
		setMethodParameters( new ArrayList<Serializable>() );
		setMetaData(new HashMap<String, String>());
	}

	/**
	 * @param methodParameters the methodParameters to set
	 */
	public void setMethodParameters(ArrayList<Serializable> methodParameters)
	{
		this.methodParameters = methodParameters;
	}

	/**
	 * @return the methodParameters
	 */
	public ArrayList<Serializable> getMethodParameters()
	{
		return methodParameters;
	}

	/**
	 * @param methodName the methodName to set
	 */
	public void setMethodName(String methodName)
	{
		this.methodName = methodName;
	}

	/**
	 * @return the methodName
	 */
	public String getMethodName()
	{
		return methodName;
	}

	/**
	 * @param metaData the metaData to set
	 */
	public void setMetaData(HashMap<String,String> metaData)
	{
		this.metaData = metaData;
	}

	/**
	 * @return the metaData
	 */
	public HashMap<String,String> getMetaData()
	{
		return metaData;
	}

    public void setMethod(Method method) {
        setMethodName(method.getName());
        Class<?>[] classes;
        for (Class<?> clazz: method.getParameterTypes()) {
            //todo: nasty hack, we know enough to clean this up
            if (clazz != AsyncCallback.class)
             lstParamClassNames.add(clazz.getCanonicalName());
        }
    }

    public List<String> getParameterClassNames() {
        return lstParamClassNames;
    }
	
}
