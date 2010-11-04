package com.googlecode.gwtrpccommlayer.client.internal.factory.impl;

import com.googlecode.gwtrpccommlayer.client.internal.factory.PostFactory;
import com.googlecode.gwtrpccommlayer.shared.GwtRpcCommLayerPojoConstants;
import com.googlecode.gwtrpccommlayer.shared.GwtRpcCommLayerPojoRequest;
import com.googlecode.gwtrpccommlayer.shared.GwtRpcCommLayerPojoResponse;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.InputStreamEntity;

import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;

/**
 * Created by IntelliJ IDEA.
 * User: dan
 * Date: 10/31/10
 * Time: 9:31 PM
 */
public class PostFactoryImpl implements PostFactory{
    @Override
    public HttpPost create(URL url, GwtRpcCommLayerPojoRequest request) {
        /*
        * SERIALZED THE POJO-REQUEST OBJECT INTO BYTES
        */
        try {
            byte[] pojoByteArray = serializeIntoBytes(request);
            long length = pojoByteArray.length;
            ByteArrayInputStream in = new ByteArrayInputStream(pojoByteArray);
            InputStreamEntity reqEntity = new InputStreamEntity(in, length);
            reqEntity.setContentType("binary/octet-stream");
            reqEntity.setChunked(false);


            /*
            * CONSTRUCT THE URL
            */
            //String url = createFullyQualifiedURL();

            /*
            * Create POST instance
            */
            //HttpPost httppost = new HttpPost(url);
            HttpPost httppost = null;
            httppost = new HttpPost(url.toURI());
            httppost.setEntity(reqEntity);

            /*
            * Add the correct user-agent
            */
            httppost.addHeader(GwtRpcCommLayerPojoConstants.GWT_RPC_COMM_LAYER_CLIENT_KEY, GwtRpcCommLayerPojoConstants.GWT_RPC_COMM_LAYER_POJO_CLIENT);

            return httppost;
        } catch (URISyntaxException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        return null;
    }

    @Override
    public GwtRpcCommLayerPojoResponse create(HttpResponse response) {
        HttpEntity resEntity = response.getEntity();
        byte[] respData = new byte[0];
        try {
            respData = deserializeIntoBytes(resEntity);
            GwtRpcCommLayerPojoResponse pojoResp = createInstanceFromBytes(respData);
            return pojoResp;
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (ClassNotFoundException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        return null;
    }
    private GwtRpcCommLayerPojoResponse createInstanceFromBytes(byte[] data) throws ClassNotFoundException, IOException {
        ObjectInputStream objIn = new ObjectInputStream(new ByteArrayInputStream(data));
        Object instance = objIn.readObject();
        GwtRpcCommLayerPojoResponse pojoResp = (GwtRpcCommLayerPojoResponse) instance;

        return pojoResp;
    }

    private byte[] serializeIntoBytes(GwtRpcCommLayerPojoRequest request) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream objOut = new ObjectOutputStream(bos);
        objOut.writeObject(request);
        objOut.flush();
        objOut.close();

        byte[] all = bos.toByteArray();
        return all;
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
