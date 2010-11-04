package com.googlecode.gwtrpccommlayer.client.internal.factory;

import com.googlecode.gwtrpccommlayer.shared.GwtRpcCommLayerPojoRequest;
import com.googlecode.gwtrpccommlayer.shared.GwtRpcCommLayerPojoResponse;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;

import java.net.URL;

/**
 * Created by IntelliJ IDEA.
 * User: dan
 * Date: 10/31/10
 * Time: 9:30 PM
 */
public interface PostFactory {
    HttpPost create(URL url, GwtRpcCommLayerPojoRequest request);
    GwtRpcCommLayerPojoResponse create(HttpResponse response);
}
