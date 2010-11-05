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
package com.googlecode.gwtrpccommlayer.server;


import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.googlecode.gwtrpccommlayer.shared.GwtRpcCommLayerPojoConstants;
import com.googlecode.gwtrpccommlayer.shared.GwtRpcCommLayerPojoRequest;
import com.googlecode.gwtrpccommlayer.shared.GwtRpcCommLayerPojoResponse;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.LinkedList;

/**
 * This class can either sit above your servlet (i.e. your servlet extends this) or it can wrap
 * your servlet (i.e. it will receive the calls and delegate it to the respective methods
 * @author jeff mchugh
 *
 */
public abstract class AbstractGwtRpcCommLayerServlet extends RemoteServiceServlet
{

	private static final long	serialVersionUID	= -8829760243946113688L;

	Object servletInstance = null;

    public AbstractGwtRpcCommLayerServlet() { }
    //@Inject
    //Can be extended and injected Guice

	public AbstractGwtRpcCommLayerServlet(Object servletImplementation)
	{
        this.servletInstance = servletImplementation;

        //this.servletImplementation = servletImplementation;
	}

	/**
	 * Initializes the servlet
	 */
	public void init(ServletConfig config) throws ServletException
	{
		super.init(config);


		/*
		 * The default is to assume this instance is the selected servlet
		 */
		servletInstance = this;


		/*
		 * Check to see another servlet should be the target
		 */
        //todo: this is currently configured using an init param in web.xml.  We need to change this to use something guice-friendly!
        //maybe use @Inject(optional=true) @Named("CommLayerImplClass") String strImplmentationClass;

		String servletImplClassName = config.getInitParameter(GwtRpcCommLayerPojoConstants.GWT_RPC_COMM_LAYER_SERVLET_IMPL_CLASS);
        //Use guice injected name instead of init parameter
        //if ( servletImplementation != null)
		if ( servletImplClassName != null && !servletImplClassName.trim().equals(""))
		{
			try
			{
				Class clzz = Class.forName(servletImplClassName);
				servletInstance = clzz.newInstance();
			}
			catch(Throwable t)
			{
				throw new ServletException(t);
			}
		}
	}


	/**
	 * any servlet that extends can opt to deny these request
	 * either explicitly or using web.xml
	 * @return
	 */
	protected boolean allowGwtRpcPojoRequest()
	{
		return true;
	}

	protected void initalizePerRequestPerResponseIfNeeded()
	{
		if ( super.perThreadRequest == null )
		{
			super.perThreadRequest = new ThreadLocal<HttpServletRequest>();
		}
		if ( super.perThreadResponse == null )
		{
			super.perThreadResponse = new ThreadLocal<HttpServletResponse>();
		}
	}


	/**
	 * Override the main service call and implement the special handling here
	 */
	public void service(ServletRequest req, ServletResponse resp) throws ServletException, IOException
	{

		HttpServletRequest request 		= (HttpServletRequest) req;
		HttpServletResponse response 	= (HttpServletResponse) resp;

		/*
		 * If the GwtRpcCommLayerClient has made this request, this header should be provided
		 */
		String gwtClientUserAgent 		= request.getHeader(GwtRpcCommLayerPojoConstants.GWT_RPC_COMM_LAYER_CLIENT_KEY);


		/*
		 * Only execute this block if:
		 *  - allowGwtRpcPojoRequest() is true
		 *  - is POST
		 *  - gwtClientUserAgent = 'GwtRpcCommLayerPojoRequest'
		 */
		if ( allowGwtRpcPojoRequest()
                && request.getMethod().equals("POST")
                && gwtClientUserAgent != null
                && gwtClientUserAgent.equalsIgnoreCase(GwtRpcCommLayerPojoConstants.GWT_RPC_COMM_LAYER_POJO_CLIENT))
		{
			try
			{

				/*
				 * This might not be initialized.
				 * So do it if needed
				 */
				initalizePerRequestPerResponseIfNeeded();

				super.perThreadRequest.set(request);
			    super.perThreadResponse.set(response);

				doServicePojoRequest(request,response);
			}
			finally
			{
			    super.perThreadRequest.set(null);
			    super.perThreadResponse.set(null);
			}

		}
		else
		{
			/*
			 * Call up the chain and allow one of the implementations handle the request accordingly.
			 * For an
			 */
			super.service(req, resp);
		}
	}

	/**
	 * Reads the request's InputStream and cast it to the correct Object type(s)
	 * @param req
	 * @param resp
	 * @throws javax.servlet.ServletException
	 * @throws java.io.IOException
	 */
	protected abstract void doServicePojoRequest(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException;

    protected Object getImplementation() {
       return this.servletInstance;
    }

	/**
	 * Execute the actual method within the GwtRpc servlet
	 * @param pojoRequest
	 * @param req
	 * @param resp
	 * @throws Throwable
	 */
	public void executePojoRequest(GwtRpcCommLayerPojoRequest pojoRequest,HttpServletRequest req, HttpServletResponse resp) throws Throwable
	{
		try
		{
			/*
			 * extract the appropriate method
			 */
			Method method = getMethod(pojoRequest);

			/*
			 * invoke the method
			 */
			Object returnedInstance = invokeMethod(pojoRequest, method);

			/*
			 * wrap response within a response object
			 */
			GwtRpcCommLayerPojoResponse pojoResp = new GwtRpcCommLayerPojoResponse();
			pojoResp.setResponseInstance( (Serializable) returnedInstance);

			/*
			 * send back the result as a stream set of bytes
			 */
			serializeResponse(req,resp,pojoRequest,pojoResp);
		}
		catch(NoSuchMethodException e)
		{
			log("Method does not found.", e);
			resp.sendError(500, e.toString() );
		}
		catch(InvocationTargetException e)
		{
			log("Error executing method.", e.getCause());
			resp.sendError(500, e.toString() );
		}
		catch(IllegalAccessException e)
		{
			log("Error accessing method.", e);
			resp.sendError(500, e.toString() );
		}
	}

	/**
	 * Write the appropriate response
	 * @param req
	 * @param resp
	 * @param pojoRequest
	 * @param pojoResp
	 * @throws java.io.IOException
	 */
	protected void serializeResponse(HttpServletRequest req, HttpServletResponse resp, GwtRpcCommLayerPojoRequest pojoRequest, GwtRpcCommLayerPojoResponse pojoResp) throws IOException
	{
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ObjectOutputStream objOut = new ObjectOutputStream(bos);
		objOut.writeObject(pojoResp);
		objOut.flush();
		objOut.close();

		resp.setContentType("binary/octet-stream");
		resp.setContentLength(bos.toByteArray().length);
		OutputStream out = resp.getOutputStream();
		out.write(bos.toByteArray());
		out.flush();
	}

	/**
	 *
	 * @param stressTestRequest
	 * @return
	 * @throws NoSuchMethodException
	 */
	protected Method getMethod(GwtRpcCommLayerPojoRequest stressTestRequest) throws NoSuchMethodException, ClassNotFoundException {
		int count = 0;
		Class<?> paramClasses[] = new Class[stressTestRequest.getMethodParameters().size()];

        LinkedList<Class<?>> lstParameterClasses = new LinkedList<Class<?>>();
        for (String methodName: stressTestRequest.getParameterClassNames()) {
            lstParameterClasses.add(Class.forName(methodName));
        }

        Class[] arrParameterClasses = lstParameterClasses.toArray(new Class[0]);
        return getClass().getMethod(stressTestRequest.getMethodName(), arrParameterClasses);
    }

	/**
	 *
	 * @param stressTestRequest
	 * @param method
	 * @return
	 * @throws java.lang.reflect.InvocationTargetException
	 * @throws IllegalAccessException
	 */
	protected Object invokeMethod(GwtRpcCommLayerPojoRequest stressTestRequest, Method method) throws InvocationTargetException, IllegalAccessException
	{
        //todo: call toArray with correct size Object Array
		Object paramsAsObjects[] = stressTestRequest.getMethodParameters().toArray( new Object[0]);
		Object instance = method.invoke(servletInstance, paramsAsObjects);
		return instance;
	}
	
	
}
