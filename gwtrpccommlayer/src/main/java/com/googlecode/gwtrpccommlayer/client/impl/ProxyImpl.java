package com.googlecode.gwtrpccommlayer.client.impl;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.googlecode.gwtrpccommlayer.shared.GwtRpcCommLayerPojoConstants;
import com.googlecode.gwtrpccommlayer.shared.GwtRpcCommLayerPojoRequest;
import com.googlecode.gwtrpccommlayer.shared.GwtRpcCommLayerPojoResponse;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.cookie.Cookie;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.*;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.HashMap;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: dan
 * Date: 10/30/10
 * Time: 10:04 PM
 */
public class ProxyImpl implements IGwtRpcClientSideProxy{

	static int DEFAULT_STANDARD_PORT 	= 80;
	static int DEFAULT_SECURE_PORT 		= 443;

	private HashMap<String,Cookie> cookies = null;


	private boolean showResponseHeaders = false;
    private URL url;

    public ProxyImpl()
	{
		cookies = new HashMap<String, Cookie>();
	}

	public ProxyImpl(URL url)
	{
		this();

        this.url = url;
    }

    @Inject
    public ProxyImpl(URL url, @Assisted HashMap<String,Cookie> cookies) {
        this.url = url;
        this.cookies = cookies;
    }

	boolean doesListIncludeGwtAsynchCallback(Class[] interfaces)
	{
		Class target = AsyncCallback.class;
		for (Class intf : interfaces)
		{
			if ( intf.getName().equals(target.getName()))
			{
				return true;
			}
		}
		return false;
	}

	Object invoke_asynchronousMode(Object proxy, Method method, Object[] args) throws Throwable
	{
		if ( args.length == 0 )
		{
			throw new RuntimeException("A minimum of (1) object is required for asynchronous mode");
		}

		AsyncCallback theCallback = (AsyncCallback) args[args.length-1];

		/*
		 * Wrap the Method (and Method arguments)
		 */
		GwtRpcCommLayerPojoRequest pojoRequest = new GwtRpcCommLayerPojoRequest();

		pojoRequest.setMethodName(method.getName());
		if ( args != null )
		{

			for (Object object : args)
			{
				if ( object != theCallback )//the last object is always going to be the callback implementation
				{
					pojoRequest.getMethodParameters().add( (Serializable) object);
				}
			}
		}

		/*
		 * This block now makes it possible to execute in a "asynchronous" mode
		 */

		GwtRpcCommLayerPojoResponse pojoResp = null;
		try
		{
			pojoResp = executeRequest(pojoRequest);
			if ( pojoResp != null )
			{
				Object result = pojoResp.getResponseInstance();
				AsynchCallBackRunnable runnable = new AsynchCallBackRunnable(theCallback, result);
				Thread thread = new Thread(runnable);
				thread.start();
				return null;
			}
			else
			{
				throw new RuntimeException("No valid GwtRpcCommLayerPojoResponse returned. Check for http errors in log.");
			}
		}
		catch(Throwable caught)
		{
			AsynchCallBackRunnable runnable = new AsynchCallBackRunnable(theCallback, caught);
			Thread thread = new Thread(runnable);
			thread.start();
			return null;
		}
	}

	Object invoke_synchronousMode(Object proxy, Method method, Object[] args) throws Throwable
	{
		/*
		 * Wrap the Method (and Method arguments)
		 */
		GwtRpcCommLayerPojoRequest pojoRequest = new GwtRpcCommLayerPojoRequest();

		pojoRequest.setMethodName(method.getName());
		if ( args != null )
		{
			for (Object object : args)
			{
				//pojoRequest.getMethodParameters().add( (Serializable) object);
                pojoRequest.getMethodParameters().add( object.getClass());
			}
		}


		/*
		 * This is the original block of code that executed for the "synchronous" mode
		 */
		GwtRpcCommLayerPojoResponse pojoResp = executeRequest(pojoRequest);
		if ( pojoResp != null )
		{
			return pojoResp.getResponseInstance();
		}
		else
		{
			throw new RuntimeException("No valid GwtRpcCommLayerPojoResponse returned. Check for http errors in log.");
		}


	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable
	{
		int mode = 0;//default to synchronous mode

		if ( args != null && args.length > 0 )
		{
			Object last = args[args.length-1];
			if ( doesListIncludeGwtAsynchCallback(last.getClass().getInterfaces()))
			{
				mode = 1;
			}
		}

		if ( mode == 0 )
		{
			return invoke_synchronousMode(proxy, method, args);
		}
		else
		{
			return invoke_asynchronousMode(proxy, method, args);
		}
	}

	/**
	 * The URL that this client shall interact with
	 */
	public void setUrl(URL url)
	{

	}

	/**
	 * Set this to true for debugging
	 */
	public void setShowResponseHeaders(boolean b)
	{
		this.showResponseHeaders = b;
	}

	/**
	 * Main method that executes to make call to remote server
	 * this method uses an HTTP POST method and can keep state if needed (using cookies)
	 * @throws IOException
	 */
	protected synchronized GwtRpcCommLayerPojoResponse executeRequest(GwtRpcCommLayerPojoRequest pojoRequest) throws IOException
	{
		/*
		 * Create HTTP CLIENT
		 */
		DefaultHttpClient httpclient = new DefaultHttpClient();

		/*
		 * Add Cookies to the request
		 * this enables a state-full set of transactions
		 * to take place. While this is NOT required for the
		 * GwtRpcCommLayer framework, the actually developer might have this
		 * requirement embedded into his/her servlet(s) and thus
		 * this makes to possible to communicate just like
		 * a browser would
		 *
		 */
		if ( getCookies().size() > 0 )
		{
			for (Cookie cookie : getCookies().values())
			{
				httpclient.getCookieStore().addCookie(cookie);
			}
		}


		/*
		 * SERIALZED THE POJO-REQUEST OBJECT INTO BYTES
		 */
		byte[] pojoByteArray = serializeIntoBytes(pojoRequest);
		long length = pojoByteArray.length;
        ByteArrayInputStream in = new ByteArrayInputStream(pojoByteArray);
        InputStreamEntity reqEntity = new InputStreamEntity(in, length);
        reqEntity.setContentType("binary/octet-stream");
        reqEntity.setChunked(false);


		/*
		 * CONSTRUCT THE URL
		 */
		String url = createFullyQualifiedURL();

		/*
		 * Create POST instance
		 */
		HttpPost httppost = new HttpPost(url);
		httppost.setEntity(reqEntity);

		/*
		 * Add the correct user-agent
		 */
        httppost.addHeader(GwtRpcCommLayerPojoConstants.GWT_RPC_COMM_LAYER_CLIENT_KEY, GwtRpcCommLayerPojoConstants.GWT_RPC_COMM_LAYER_POJO_CLIENT);

		/*
		 * Notify any last minute listener
		 */
        onBeforeRequest(httppost);
        HttpResponse response = httpclient.execute(httppost);

        /*
         * Provide a call back for timing, error handling, etc
         */
        onAfterRequest(httppost, response);

        /*
         * Check for server error
         */
        if ( response.getStatusLine().getStatusCode() >= 400 && response.getStatusLine().getStatusCode() <= 599 )
        {
        	onResponseError(response.getStatusLine().getStatusCode(), response);
        	return null;
        }
        else if (response.getFirstHeader("Content-Type") != null && !response.getFirstHeader("Content-Type").getValue().equals("binary/octet-stream"))
        {
        	onResponseError("Content-Type must be 'binary/octet-stream'");
        	return null;
        }
        else
        {
        	/*
        	 * Provide a call-back for examining the response headers
        	 */
        	if ( isShowResponseHeaders() && response.getAllHeaders() != null )
        	{
        		dumpResponseHeaders(response.getAllHeaders());
        	}

        	/*
        	 * Provide a call-back for examining the response cookies
        	 */
        	List<Cookie> cookies = httpclient.getCookieStore().getCookies();
        	if ( cookies != null )
        	{
        		recordCookies(cookies);
        	}


        	HttpEntity resEntity = response.getEntity();
        	byte[] respData = deserializeIntoBytes(resEntity);
        	try
        	{
        		GwtRpcCommLayerPojoResponse pojoResp = createInstanceFromBytes(respData);
        		return pojoResp;
        	}
        	catch(ClassNotFoundException e)
        	{
        		throw new IOException(e);
        	}

        }

	}

	/*
	 * ---------------- METHODS THAT CAN BE EXTENDED BY SUB-CLASSES -------------
	 */

	protected String createFullyQualifiedURL()
	{
		/*
		 * Configure the URL and PATH
		 */
		StringBuffer buff = new StringBuffer();
        buff.append(url.getProtocol());
		buff.append("://");
		buff.append(url.getHost());
		if ( url.getPort() != DEFAULT_STANDARD_PORT && url.getPort() != DEFAULT_SECURE_PORT )
		{
			buff.append(":"+url.getPort());
		}
		buff.append(url.getPath());
		if ( url.getQuery() != null )
		{
			buff.append("?");
			buff.append(url.getQuery());
		}
		return buff.toString();
	}

	protected byte[] serializeIntoBytes(GwtRpcCommLayerPojoRequest pojoRequest) throws IOException
	{
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream objOut = new ObjectOutputStream(bos);
        objOut.writeObject(pojoRequest);
        objOut.flush();
        objOut.close();

        byte[] all = bos.toByteArray();
        return all;
	}

	protected byte[] deserializeIntoBytes(HttpEntity respEntity ) throws IOException
	{
    	byte[] b = new byte[512];
    	ByteArrayOutputStream buff = new ByteArrayOutputStream();
    	InputStream respIn = respEntity.getContent();
    	while(true)
    	{
    		int vv = respIn.read(b);
    		if ( vv == -1 )
    		{
    			break;
    		}
    		buff.write(b, 0, vv);
    	}
    	return buff.toByteArray();
	}

	protected GwtRpcCommLayerPojoResponse createInstanceFromBytes(byte[] data) throws IOException, ClassNotFoundException
	{
    	ObjectInputStream objIn = new ObjectInputStream(new ByteArrayInputStream(data));
    	Object instance = objIn.readObject();
    	GwtRpcCommLayerPojoResponse pojoResp = (GwtRpcCommLayerPojoResponse) instance;

    	return pojoResp;
	}

	protected void recordCookies(List<Cookie> cookies)
	{
		 for (Cookie cookie : cookies)
         {
         	getCookies().put(cookie.getName(), cookie);
 		}
	}

	protected void dumpResponseHeaders(Header header[])
	{
    	for (Header header2 : header)
    	{
    		System.out.println("" + header2.getName() + ":" + header2.getValue() );
		}
	}

	/**
	 * Provides a call-back for future functionality. Will get called before the actual HTTP Request is executed
	 * @param httppost
	 */
	protected void onBeforeRequest(HttpPost httppost)
	{
	}

	/**
	 * Provides a call-back for future functionality. Will get called after the response is returned
	 * @param httppost
	 * @param response
	 */
	protected void onAfterRequest(HttpPost httppost, HttpResponse response)
	{
	}

	/**
	 * Called in the event of an HTTP Response Error (400 through 599)
	 * @param errorCode
	 * @param response
	 */
	protected void onResponseError(int errorCode, HttpResponse response)
	{
		System.out.println("HTTP_ERROR code=" + errorCode + " " + response.getStatusLine().getReasonPhrase() );
	}

	/**
	 * Called in the event of an general error outside of the http protocol
	 * @param errorCode
	 * @param response
	 */
	protected void onResponseError(String error)
	{
		System.out.println("Response Error=" + error);
	}











	/*
	 * ---------------------------- GETTER/SETTER METHODS ---------------------------
	 */

	/**
	 * @param cookies the cookies to set
	 */
	public void setCookies(HashMap<String,Cookie> cookies)
	{
		this.cookies = cookies;
	}

	/**
	 * @return the cookies
	 */
	public HashMap<String,Cookie> getCookies()
	{
		return cookies;
	}




   /**
    * @return the showResponseHeaders
    */
   boolean isShowResponseHeaders()
   {
       return showResponseHeaders;
   }


   /*
    * This class is for use with the asynchronous mode
    */
   static class AsynchCallBackRunnable<T> implements Runnable
   {

       AsyncCallback<T> callback;
       Throwable caughtThrowable;
       T result;

       public AsynchCallBackRunnable(AsyncCallback<T> callback, T result)
       {
           this.callback = callback;
           this.result = result;
       }

       public AsynchCallBackRunnable(AsyncCallback<T> callback, Throwable caughtThrowable)
       {
           this.callback = callback;
           this.caughtThrowable = caughtThrowable;
       }

       public void run()
       {
           try
           {
               if ( this.result != null )
               {
                   this.callback.onSuccess(this.result);
               }
               else if ( this.caughtThrowable != null )
               {
                   this.callback.onFailure(this.caughtThrowable);
               }
               else
               {
                   throw new RuntimeException("Both 'success' and 'failure' payload objects are null. This should never occur.");
               }
           }
           catch(Throwable caught)
           {
               System.err.println("Failure in Asynch dispatch thread. " + caught.toString() );
               caught.printStackTrace();
           }
       }

   }


}
