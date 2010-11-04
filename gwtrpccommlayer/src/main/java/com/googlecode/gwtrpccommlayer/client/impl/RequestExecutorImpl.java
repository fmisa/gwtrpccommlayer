package com.googlecode.gwtrpccommlayer.client.impl;

import com.googlecode.gwtrpccommlayer.client.RequestExecutor;
import com.googlecode.gwtrpccommlayer.client.internal.factory.PostFactory;
import com.googlecode.gwtrpccommlayer.shared.GwtRpcCommLayerPojoRequest;
import com.googlecode.gwtrpccommlayer.shared.GwtRpcCommLayerPojoResponse;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.*;
import java.net.URL;
import java.util.Collection;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: dan
 * Date: 10/31/10
 * Time: 8:11 PM
 */
public class RequestExecutorImpl implements RequestExecutor{

    private final PostFactory postFactory;
    private final URL url;
    private final Collection<Cookie> cookies;

    public RequestExecutorImpl(PostFactory postFactory, URL url, Collection<Cookie> cookies) {
        this.postFactory = postFactory;
        this.url = url;

        this.cookies = cookies;
    }
    @Override
    public GwtRpcCommLayerPojoResponse executeRequest(GwtRpcCommLayerPojoRequest request) {
        /*
           * Create HTTP CLIENT
           */
        DefaultHttpClient httpclient = new DefaultHttpClient();


        try {
            //ALGO:
            //we should be getting a Client with a cookie

            //httpclient = clientFactory.create(cookie);

            //get a POST with the url & request
            //post = postFactory.create(url, request);

            //tell client to execute the post
            //response = client.ex(post)

            //parse HttpResponse
            //pojoResponse = responseFactory.create(Response);

            //return PojoResponse
            /*
            * Add Cookies to the request
            * this enables a state-full set of transactions
            * to take place. While this is NOT required for the
            * GwtRpcCommLayer framework, the actually developer might have this
            * requirement embedded into his/her servlet(s) and thus
            * this makes to possible to communicate just like
            * a browser would
            *
            *
            *
            */
            if (cookies!=null) {
                Date now = new Date();
                for (Cookie cookie: cookies){
                    if (cookie.isExpired(now))
                        throw new IllegalStateException("Expired Cookie: " + cookie.getName());
                    httpclient.getCookieStore().addCookie(cookie);
                }
            }

            HttpPost post = postFactory.create(url, request);


            /*
            * Notify any last minute listener
            */
            HttpResponse response = httpclient.execute(post);

            if (!validateResponse(response))
                return null;

            //todo: tell a listener we have a response
            //listener.onResponse(Response)

            /*
            * Provide a call-back for examining the response cookies
            */
            //todo: tell listener about cookies
            //listener.onResponse(httpclient.getCookieStore().getCookies();

            //GwtRpcCommLayerPojoResponse pojoResp = createInstanceFromBytes(respData);
            GwtRpcCommLayerPojoResponse pojoResp = postFactory.create(response);
            return pojoResp;
        } catch (ClientProtocolException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        return null;
    }

    private boolean validateResponse(HttpResponse response) {
        /*
         * Check for server error
         */
        if ( response.getStatusLine().getStatusCode() >= 400 && response.getStatusLine().getStatusCode() <= 599 )
        {
        	//todo:listener.onResponseError(response.getStatusLine().getStatusCode(), response);
        	return false;
        }
        else if (response.getFirstHeader("Content-Type") != null && !response.getFirstHeader("Content-Type").getValue().equals("binary/octet-stream"))
        {
        	//todo: listener.onResponseError("Content-Type must be 'binary/octet-stream'");
        	return false;
        }
        return true;
    }


    private GwtRpcCommLayerPojoResponse createInstanceFromBytes(byte[] data) throws ClassNotFoundException, IOException {
        ObjectInputStream objIn = new ObjectInputStream(new ByteArrayInputStream(data));
        Object instance = objIn.readObject();
        GwtRpcCommLayerPojoResponse pojoResp = (GwtRpcCommLayerPojoResponse) instance;

        return pojoResp;
    }

    private byte[] deserializeIntoBytes(HttpEntity respEntity) throws IOException {
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

}
